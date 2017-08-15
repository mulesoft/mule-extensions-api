/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.lang.String.format;
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
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.VoidType;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.extension.api.ExtensionConstants;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.exception.IllegalOperationModelDefinitionException;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.internal.property.LiteralModelProperty;

/**
 * A {@link DeclarationEnricher} which adds a {@link ExtensionConstants#TARGET_PARAMETER_NAME} parameter
 * to all non void operations
 *
 * @since 1.0
 */
public final class TargetParameterDeclarationEnricher implements DeclarationEnricher {

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    new EnricherDelegate().enrich(extensionLoadingContext);
  }

  private class EnricherDelegate implements DeclarationEnricher {

    private MetadataType attributeType;
    private MetadataType targetValue;

    private EnricherDelegate() {
      ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
      this.attributeType = typeLoader.load(String.class);
      this.targetValue = typeLoader.load(String.class);
    }

    @Override
    public void enrich(ExtensionLoadingContext extensionLoadingContext) {
      new IdempotentDeclarationWalker() {

        @Override
        protected void onOperation(OperationDeclaration declaration) {
          final MetadataType outputType = declaration.getOutput().getType();
          if (outputType == null) {
            throw new IllegalOperationModelDefinitionException(format("Operation '%s' does not specify an output type",
                                                                      declaration.getName()));
          }

          if (!(outputType instanceof VoidType)) {
            declaration.getParameterGroup(OUTPUT).addParameter(declareTarget()).addParameter(declareTargetValue());
          }
        }
      }.walk(extensionLoadingContext.getExtensionDeclarer().getDeclaration());
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
      parameter.addModelProperty(new LiteralModelProperty());
      return parameter;
    }
  }
}
