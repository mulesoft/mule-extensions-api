/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static org.mule.runtime.extension.api.annotation.Extension.DEFAULT_CONFIG_NAME;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.POST_STRUCTURE;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import org.mule.runtime.api.meta.model.declaration.fluent.ComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExecutableComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.WithOperationsDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.WithSourcesDeclaration;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher;
import org.mule.runtime.extension.api.property.ImplicitConfigNameModelProperty;
import org.mule.runtime.extension.api.property.ManyImplicitConfigsModelProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @since 1.10
 */
public class ImplicitConfigEnricher implements WalkingDeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return POST_STRUCTURE;
  }

  @Override
  public Optional<DeclarationEnricherWalkDelegate> getWalkDelegate(ExtensionLoadingContext extensionLoadingContext) {
    return of(new DeclarationEnricherWalkDelegate() {

      private ConfigurationDeclaration defaultConfig = null;

      private Map<ComponentDeclaration, Set<Object>> result = new HashMap<>();

      @Override
      public void onConfiguration(ConfigurationDeclaration declaration) {
        if (declaration.getName().equals(DEFAULT_CONFIG_NAME)) {
          defaultConfig = declaration;
        }
      }

      @Override
      public void onOperation(WithOperationsDeclaration owner, OperationDeclaration declaration) {
        resolve(owner, declaration);
      }

      @Override
      public void onSource(WithSourcesDeclaration owner, SourceDeclaration declaration) {
        resolve(owner, declaration);
      }

      private void resolve(Object owner, ExecutableComponentDeclaration declaration) {
        result.computeIfAbsent(declaration, m -> new HashSet<>())
            .add(owner);
      }

      @Override
      public void onWalkFinished() {
        result.forEach((component, owners) -> {
          List<ConfigurationDeclaration> implicitConfigurationModels = owners.stream()
              .map(owner -> {
                if (owner instanceof ConfigurationDeclaration) {
                  return (ConfigurationDeclaration) owner;
                } else {
                  return defaultConfig;
                }
              })
              .filter(owner -> owner != null)
              .filter(ConfigRefDeclarationEnricher::canBeUsedImplicitly)
              .collect(toList());
          if (implicitConfigurationModels.size() == 1) {
            component.addModelProperty(new ImplicitConfigNameModelProperty(implicitConfigurationModels.get(0).getName()));
          }
          if (implicitConfigurationModels.size() > 1) {
            component.addModelProperty(new ManyImplicitConfigsModelProperty());
          }
        });

      }
    });
  }

}
