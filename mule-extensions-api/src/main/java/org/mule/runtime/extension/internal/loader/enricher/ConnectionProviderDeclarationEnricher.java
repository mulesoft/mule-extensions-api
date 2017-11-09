/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static org.mule.runtime.api.meta.model.connection.ConnectionManagementType.POOLING;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.POST_STRUCTURE_DECLARATION;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE_DECLARATION;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.addReconnectionConfigParameter;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.addPoolingProfileParameter;
import org.mule.runtime.api.config.PoolingProfile;
import org.mule.runtime.api.meta.model.connection.ConnectionManagementType;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.internal.property.NoReconnectionStrategyModelProperty;

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
public class ConnectionProviderDeclarationEnricher implements DeclarationEnricher {


  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE_DECLARATION;
  }

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    final ExtensionDeclaration declaration = extensionLoadingContext.getExtensionDeclarer().getDeclaration();
    if (!declaration.getModelProperty(NoReconnectionStrategyModelProperty.class).isPresent()) {
      new IdempotentDeclarationWalker() {

        @Override
        protected void onConnectionProvider(ConnectionProviderDeclaration declaration) {
          addReconnectionConfigParameter(declaration);
          ConnectionManagementType managementType = declaration.getConnectionManagementType();

          if (managementType == POOLING) {
            addPoolingProfileParameter(declaration);
          }
        }
      }.walk(declaration);
    }
  }

}
