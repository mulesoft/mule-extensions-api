/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.connection.ConnectionManagementType.CACHED;
import static org.mule.runtime.api.meta.model.connection.ConnectionManagementType.POOLING;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.CONNECTION;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.extension.api.ExtensionConstants.DISABLE_CONNECTION_VALIDATION_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.DISABLE_CONNECTION_VALIDATION_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.POOLING_PROFILE_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.POOLING_PROFILE_PARAMETER_NAME;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_POOLING_PROFILE_TYPE_QNAME;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.runtime.api.config.PoolingProfile;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.connection.ConnectionManagementType;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.declaration.type.PoolingProfileTypeBuilder;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.internal.property.QNameModelProperty;

/**
 * Enriches all the {@link ConnectionProviderDeclaration} by adding language rules parameters.
 * <p>
 * In concrete it:
 * <p>
 * <ul>
 * <li>Add a reconnection strategy parameter</li>
 * <li>A {@link PoolingProfile} parameter when the {@link ConnectionManagementType} is {@link ConnectionManagementType#POOLING}</li>
 * <li>A parameter which allows disabling connection validation when the {@link ConnectionManagementType} is
 * {@link ConnectionManagementType#POOLING} or {@link ConnectionManagementType#CACHED}</li>
 * </ul>
 *
 * @since 1.0
 */
public class ConnectionProviderDeclarationEnricher extends InfrastructureDeclarationEnricher {

  private ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    new IdempotentDeclarationWalker() {

      @Override
      protected void onConnectionProvider(ConnectionProviderDeclaration declaration) {
        addReconnectionStrategyParameter(declaration);
        ConnectionManagementType managementType = declaration.getConnectionManagementType();

        if (managementType == POOLING || managementType == CACHED) {
          addDisableValidationParameter(declaration);
        }

        if (managementType == POOLING) {
          addPoolingProfileParameter(declaration);
        }
      }
    }.walk(extensionLoadingContext.getExtensionDeclarer().getDeclaration());
  }

  private void addPoolingProfileParameter(ConnectionProviderDeclaration declaration) {
    ParameterDeclaration parameter = new ParameterDeclaration(POOLING_PROFILE_PARAMETER_NAME);
    parameter.setDescription(POOLING_PROFILE_PARAMETER_DESCRIPTION);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(new PoolingProfileTypeBuilder().buildPoolingProfileType(), false);
    parameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());
    parameter.setDslModel(ParameterDslConfiguration.builder()
        .allowsInlineDefinition(true)
        .allowsReferences(false)
        .allowTopLevelDefinition(false)
        .build());
    parameter.addModelProperty(new QNameModelProperty(MULE_POOLING_PROFILE_TYPE_QNAME));
    markAsInfrastructure(parameter);

    declaration.getParameterGroup(CONNECTION).addParameter(parameter);
  }

  private void addDisableValidationParameter(ConnectionProviderDeclaration declaration) {
    ParameterDeclaration parameter = new ParameterDeclaration(DISABLE_CONNECTION_VALIDATION_PARAMETER_NAME);
    parameter.setDescription(DISABLE_CONNECTION_VALIDATION_PARAMETER_DESCRIPTION);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setRequired(false);
    parameter.setParameterRole(BEHAVIOUR);
    parameter.setType(typeLoader.load(boolean.class), false);
    parameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());
    markAsInfrastructure(parameter);

    declaration.getParameterGroup(CONNECTION).addParameter(parameter);
  }
}
