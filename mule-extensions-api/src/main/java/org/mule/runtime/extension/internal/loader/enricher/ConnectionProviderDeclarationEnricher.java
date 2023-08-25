/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.util.Optional.empty;
import static org.mule.runtime.api.meta.model.connection.ConnectionManagementType.POOLING;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.addPoolingProfileParameter;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.addReconnectionConfigParameter;
import static org.mule.runtime.extension.internal.util.ExtensionConnectivityUtils.isReconnectionStrategySupported;

import org.mule.runtime.api.config.PoolingProfile;
import org.mule.runtime.api.meta.model.connection.ConnectionManagementType;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.IdempotentDeclarationEnricherWalkDelegate;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher;

import java.util.Optional;

/**
 * Enriches all the {@link ConnectionProviderDeclaration} by adding language rules parameters.
 * <p>
 * In concrete it:
 * <p>
 * <ul>
 * <li>Add a reconnection strategy parameter</li>
 * <li>A {@link PoolingProfile} parameter when the {@link ConnectionManagementType} is
 * {@link ConnectionManagementType#POOLING}</li>
 * <li>A parameter which allows disabling connection validation when the {@link ConnectionManagementType} is
 * {@link ConnectionManagementType#POOLING} or {@link ConnectionManagementType#CACHED}</li>
 * </ul>
 *
 * @since 1.0
 */
public class ConnectionProviderDeclarationEnricher implements WalkingDeclarationEnricher {


  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  @Override
  public Optional<DeclarationEnricherWalkDelegate> getWalkDelegate(ExtensionLoadingContext extensionLoadingContext) {
    final ExtensionDeclaration declaration = extensionLoadingContext.getExtensionDeclarer().getDeclaration();

    if (isReconnectionStrategySupported(declaration)) {
      return Optional.of(new IdempotentDeclarationEnricherWalkDelegate() {

        @Override
        protected void onConnectionProvider(ConnectionProviderDeclaration declaration) {
          addReconnectionConfigParameter(declaration);
          ConnectionManagementType managementType = declaration.getConnectionManagementType();

          if (managementType == POOLING) {
            addPoolingProfileParameter(declaration);
          }
        }
      });
    }

    return empty();
  }

}
