/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.lang.String.format;
import static java.util.Optional.of;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.api.tx.TransactionType.LOCAL;
import static org.mule.runtime.extension.api.ExtensionConstants.OPERATION_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.SOURCE_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.TRANSACTIONAL_ACTION_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TRANSACTIONAL_TYPE_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TRANSACTION_TYPE_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;
import static org.mule.runtime.extension.api.tx.OperationTransactionalAction.JOIN_IF_POSSIBLE;
import static org.mule.runtime.extension.api.tx.SourceTransactionalAction.NONE;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.declaration.fluent.ComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExecutableComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.tx.TransactionType;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.IdempotentDeclarationEnricherWalkDelegate;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher;
import org.mule.runtime.extension.api.tx.OperationTransactionalAction;
import org.mule.runtime.extension.api.tx.SourceTransactionalAction;
import org.mule.runtime.extension.internal.property.NoTransactionalActionModelProperty;
import org.mule.runtime.extension.internal.property.TransactionalActionModelProperty;
import org.mule.runtime.extension.internal.property.TransactionalTypeModelProperty;

import java.util.Optional;

/**
 * {@link DeclarationEnricher} which enrich transactional {@link ComponentModel component models} adding required transactional
 * parameters to the correspondent model.
 * <p>
 * If the given {@link ComponentModel} already contains the parameter, this one will be enriched to ensure a cross components
 * transactional parameters UX.
 *
 * @since 1.0
 */
public final class TransactionalDeclarationEnricher implements WalkingDeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  @Override
  public Optional<DeclarationEnricherWalkDelegate> getWalkDelegate(ExtensionLoadingContext extensionLoadingContext) {
    return of(new IdempotentDeclarationEnricherWalkDelegate() {

      final ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
      final MetadataType operationTransactionalActionType = typeLoader.load(OperationTransactionalAction.class);
      final MetadataType sourceTransactionalActionType = typeLoader.load(SourceTransactionalAction.class);
      final MetadataType sdkOperationTransactionalActionType =
          typeLoader.load(org.mule.sdk.api.tx.OperationTransactionalAction.class);
      final MetadataType sdkSourceTransactionalActionType = typeLoader.load(org.mule.sdk.api.tx.SourceTransactionalAction.class);
      final MetadataType transactionType = typeLoader.load(TransactionType.class);

      @Override
      protected void onSource(SourceDeclaration declaration) {
        addTxParameter(TRANSACTIONAL_ACTION_PARAMETER_NAME, sourceTransactionalActionType, sdkSourceTransactionalActionType,
                       NONE, org.mule.sdk.api.tx.SourceTransactionalAction.NONE,
                       SOURCE_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION, declaration, new TransactionalActionModelProperty());
        addTxParameter(TRANSACTIONAL_TYPE_PARAMETER_NAME, null, transactionType, null, LOCAL,
                       TRANSACTION_TYPE_PARAMETER_DESCRIPTION,
                       declaration,
                       new TransactionalTypeModelProperty());
      }

      @Override
      protected void onOperation(OperationDeclaration declaration) {
        addTxParameter(TRANSACTIONAL_ACTION_PARAMETER_NAME, operationTransactionalActionType,
                       sdkOperationTransactionalActionType, JOIN_IF_POSSIBLE,
                       org.mule.sdk.api.tx.OperationTransactionalAction.JOIN_IF_POSSIBLE,
                       OPERATION_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION, declaration,
                       new TransactionalActionModelProperty());
      }
    });
  }


  private void addTxParameter(String parameterName, MetadataType metadataType, MetadataType sdkMetadataType,
                              Object defaultValue, Object sdkDefaultValue, String description,
                              ExecutableComponentDeclaration<?> declaration, ModelProperty modelProperty) {
    if (!declaration.isTransactional() || declaration.getModelProperty(NoTransactionalActionModelProperty.class).isPresent()) {
      return;
    }
    Optional<ParameterDeclaration> parameterDeclaration = isPresent(declaration, metadataType);
    Optional<ParameterDeclaration> sdkParameterDeclaration = isPresent(declaration, sdkMetadataType);
    if (parameterDeclaration.isPresent() && sdkParameterDeclaration.isPresent()) {
      throw new IllegalModelDefinitionException(format("Component '%s' has transactional parameters from different APIs. Offending parameters are '%s' and '%s'.",
                                                       declaration.getName(),
                                                       parameterDeclaration.get().getName(),
                                                       sdkParameterDeclaration.get().getName()));
    } else if (parameterDeclaration.isPresent()) {
      enrichTransactionParameter(defaultValue, description, parameterDeclaration.get(), modelProperty);
    } else if (sdkParameterDeclaration.isPresent()) {
      enrichTransactionParameter(sdkDefaultValue, description, sdkParameterDeclaration.get(), modelProperty);
    } else {
      ParameterDeclaration transactionParameter = new ParameterDeclaration(parameterName);
      transactionParameter.setType(sdkMetadataType, false);
      enrichTransactionParameter(sdkDefaultValue, description, transactionParameter, modelProperty);
      declaration.getParameterGroup(DEFAULT_GROUP_NAME).addParameter(transactionParameter);
    }
  }

  private void enrichTransactionParameter(Object defaultValue, String description, ParameterDeclaration transactionParameter,
                                          ModelProperty modelProperty) {
    transactionParameter.setExpressionSupport(NOT_SUPPORTED);
    transactionParameter.setRequired(false);
    transactionParameter.setDefaultValue(defaultValue);
    transactionParameter.setDescription(description);
    transactionParameter.addModelProperty(modelProperty);
    transactionParameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());
  }

  private Optional<ParameterDeclaration> isPresent(ComponentDeclaration<?> declaration, MetadataType metadataType) {
    if (metadataType == null) {
      return Optional.empty();
    }
    return declaration.getParameterGroups()
        .stream()
        .flatMap(group -> group.getParameters().stream())
        .filter(parameterDeclaration -> parameterDeclaration.getType().equals(metadataType))
        .findAny();
  }
}
