/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static java.util.Optional.of;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.OUTPUT;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.extension.api.ExtensionConstants.TARGET_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.TARGET_PARAMETER_DISPLAY_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TARGET_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TARGET_VALUE_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.TARGET_VALUE_PARAMETER_DISPLAY_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TARGET_VALUE_PARAMETER_NAME;
import static org.mule.runtime.extension.api.annotation.param.Optional.PAYLOAD;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.VoidType;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.util.collection.SmallMap;
import org.mule.runtime.extension.api.ExtensionConstants;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.exception.IllegalOperationModelDefinitionException;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.IdempotentDeclarationEnricherWalkDelegate;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher;
import org.mule.runtime.extension.internal.property.TargetModelProperty;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link DeclarationEnricher} which adds a {@link ExtensionConstants#TARGET_PARAMETER_NAME} parameter to all non void
 * operations
 *
 * @since 1.0
 */
public final class TargetParameterDeclarationEnricher implements WalkingDeclarationEnricher {

  /**
   * This map holds as key the names of the extensions that have Operations that will not be enriched and as value a {@link Set}
   * with the names of the Operations.
   */
  private static final Map<String, Set<String>> BLOCK_LIST;

  static {
    Map<String, Set<String>> block = new SmallMap<>();
    block.put("ee", singleton("transform"));
    block.put("cxf", unmodifiableSet(new HashSet<>(asList("simpleService", "jaxwsService", "proxyService",
                                                          "simpleClient", "jaxwsClient", "proxyClient"))));

    BLOCK_LIST = unmodifiableMap(block);
  }

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  @Override
  public Optional<DeclarationEnricherWalkDelegate> getWalker(ExtensionLoadingContext extensionLoadingContext) {
    String extensionName = extensionLoadingContext.getExtensionDeclarer().getDeclaration().getName();
    return of(new WalkDelegateDelegateDeclaration(BLOCK_LIST.getOrDefault(extensionName, emptySet())));
  }

  private class WalkDelegateDelegateDeclaration extends IdempotentDeclarationEnricherWalkDelegate {

    private final Set<String> blockedOperationsNames;
    private MetadataType attributeType;
    private MetadataType targetValue;

    private WalkDelegateDelegateDeclaration(Set<String> blockedOperationsNames) {
      ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
      this.attributeType = typeLoader.load(String.class);
      this.targetValue = typeLoader.load(String.class);
      this.blockedOperationsNames = blockedOperationsNames;
    }

    @Override
    protected void onOperation(OperationDeclaration declaration) {
      if (blockedOperationsNames.contains(declaration.getName())) {
        return;
      }
      final MetadataType outputType = declaration.getOutput().getType();
      if (outputType == null) {
        throw new IllegalOperationModelDefinitionException(format("Operation '%s' does not specify an output type",
                                                                  declaration.getName()));
      }

      if (!(outputType instanceof VoidType)) {
        declaration.getParameterGroup(OUTPUT).addParameter(declareTarget()).addParameter(declareTargetValue());
      }
    }

    private ParameterDeclaration declareTarget() {
      ParameterDeclaration parameter = new ParameterDeclaration(TARGET_PARAMETER_NAME);
      parameter.setDescription(TARGET_PARAMETER_DESCRIPTION);
      parameter.setExpressionSupport(NOT_SUPPORTED);
      parameter.setRequired(false);
      parameter.setParameterRole(BEHAVIOUR);
      parameter.setType(attributeType, false);
      parameter.setDisplayModel(DisplayModel.builder().displayName(TARGET_PARAMETER_DISPLAY_NAME).build());
      parameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());
      return parameter;
    }

    private ParameterDeclaration declareTargetValue() {
      ParameterDeclaration parameter = new ParameterDeclaration(TARGET_VALUE_PARAMETER_NAME);
      parameter.setDescription(TARGET_VALUE_PARAMETER_DESCRIPTION);
      parameter.setExpressionSupport(REQUIRED);
      parameter.setRequired(false);
      parameter.setDefaultValue(PAYLOAD);
      parameter.setParameterRole(BEHAVIOUR);
      parameter.setType(targetValue, false);
      parameter.setDisplayModel(DisplayModel.builder().displayName(TARGET_VALUE_PARAMETER_DISPLAY_NAME).build());
      parameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());
      parameter.addModelProperty(new TargetModelProperty());
      return parameter;
    }
  }
}
