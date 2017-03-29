/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.lang.String.format;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.OPERATION_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.SOURCE_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.TRANSACTIONAL_ACTION_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TRANSACTIONAL_TAB_NAME;
import static org.mule.runtime.extension.api.tx.OperationTransactionalAction.JOIN_IF_POSSIBLE;
import static org.mule.runtime.extension.api.tx.SourceTransactionalAction.NONE;
import static org.mule.runtime.extension.api.util.NameUtils.getComponentDeclarationTypeName;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.declaration.fluent.ComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.tx.OperationTransactionalAction;
import org.mule.runtime.extension.api.tx.SourceTransactionalAction;

public final class TransactionalDeclarationEnricher implements DeclarationEnricher {

  private final MetadataType operationTransactionalActionType;
  private final MetadataType sourceTransactionalActionType;

  public TransactionalDeclarationEnricher() {
    ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
    operationTransactionalActionType = typeLoader.load(OperationTransactionalAction.class);
    sourceTransactionalActionType = typeLoader.load(SourceTransactionalAction.class);
  }

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    new IdempotentDeclarationWalker() {

      @Override
      protected void onSource(SourceDeclaration declaration) {
        //TODO - MULE-12066 : Support XA transactions in SDK extensions at source level
        addTxParameter(TRANSACTIONAL_ACTION_PARAMETER_NAME, sourceTransactionalActionType, NONE,
                       SOURCE_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION, declaration, extensionLoadingContext);
      }

      @Override
      protected void onOperation(OperationDeclaration declaration) {
        addTxParameter(TRANSACTIONAL_ACTION_PARAMETER_NAME, operationTransactionalActionType, JOIN_IF_POSSIBLE,
                       OPERATION_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION, declaration, extensionLoadingContext);
      }
    }.walk(extensionLoadingContext.getExtensionDeclarer().getDeclaration());
  }

  private void addTxParameter(String parameterName, MetadataType metadataType, Object defaultValue, String description,
                              ComponentDeclaration<?> declaration, ExtensionLoadingContext extensionLoadingContext) {
    if (declaration.isTransactional()) {
      validateParameterName(parameterName, declaration, extensionLoadingContext);

      ParameterDeclaration transactionParameter = new ParameterDeclaration(parameterName);
      transactionParameter.setType(metadataType, false);
      transactionParameter.setExpressionSupport(NOT_SUPPORTED);
      transactionParameter.setRequired(false);
      transactionParameter.setDefaultValue(defaultValue);
      transactionParameter.setDescription(description);
      transactionParameter.setLayoutModel(LayoutModel.builder().tabName(TRANSACTIONAL_TAB_NAME).build());

      declaration.getParameterGroup(DEFAULT_GROUP_NAME).addParameter(transactionParameter);
    }
  }

  private void validateParameterName(String parameterName, ComponentDeclaration<?> declaration,
                                     ExtensionLoadingContext extensionLoadingContext) {
    declaration.getAllParameters().stream()
        .filter(parameter -> parameterName.equals(parameter.getName()))
        .findAny().ifPresent(p -> {
          throw new IllegalModelDefinitionException(format("%s '%s' from extension '%s' defines a parameter named '%s' which is a reserved word",
                                                           getComponentDeclarationTypeName(declaration),
                                                           declaration.getName(), extensionLoadingContext
                                                               .getExtensionDeclarer().getDeclaration()
                                                               .getName(),
                                                           parameterName));
        });
  }
}
