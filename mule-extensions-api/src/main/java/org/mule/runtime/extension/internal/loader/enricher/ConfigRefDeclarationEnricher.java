/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.util.stream.Collectors.toList;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.api.meta.model.stereotype.StereotypeModelBuilder.newStereotype;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;
import static org.mule.runtime.extension.internal.declaration.type.MetadataTypeConstants.CONFIG_TYPE;
import static org.mule.runtime.extension.internal.util.ExtensionNamespaceUtils.getExtensionsNamespace;
import static org.mule.sdk.api.stereotype.MuleStereotypes.CONFIG;
import static org.slf4j.LoggerFactory.getLogger;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.declaration.fluent.ComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.util.DeclarationWalker;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.property.NoImplicitModelProperty;
import org.mule.runtime.extension.api.property.SyntheticModelModelProperty;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;

/**
 * Enriches component models that depends on a configuration adding the `config-ref` parameter to the model.
 * <p>
 * The "config-ref" parameter points to a connector configuration that will be used to execute the component, this parameter is
 * always required unless an implicit one can be created.
 *
 * @since 1.2
 */
public class ConfigRefDeclarationEnricher implements DeclarationEnricher {

  private static final Logger LOGGER = getLogger(ConfigRefDeclarationEnricher.class);

  private static final String CONFIG_REF_NAME = "config-ref";

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    final ExtensionDeclaration declaration = extensionLoadingContext.getExtensionDeclarer().getDeclaration();
    Multimap<ComponentDeclaration, ConfigurationDeclaration> componentsConfigs = getComponentConfigsMap(declaration);

    componentsConfigs.asMap()
        .forEach((component, configs) -> {
          // This is ugly, but is needed so that in the rare case than an extension other than apikit happens to define a
          // config-ref parameter that is valid, the behavior is not affected.
          if ("APIKit".equals(declaration.getName())
              && component.getDefaultParameterGroup().getParameters().stream()
                  .anyMatch(param -> param.getName().equals(CONFIG_REF_NAME))) {
            LOGGER.warn("Component '" + component.getName() + "' in extension '" + declaration.getName() + "' already has a '"
                + CONFIG_REF_NAME + "' parameter defined. Skipping ConfigRefDeclarationEnricher for it.");
            return;
          }

          component.getDefaultParameterGroup().addParameter(buildConfigRefParameter(component, configs));
        });
  }

  private Multimap<ComponentDeclaration, ConfigurationDeclaration> getComponentConfigsMap(ExtensionDeclaration declaration) {
    final String namespace = getExtensionsNamespace(declaration);
    Multimap<ComponentDeclaration, ConfigurationDeclaration> componentConfigs = LinkedListMultimap.create();
    new DeclarationWalker() {

      @Override
      protected void onConfiguration(ConfigurationDeclaration config) {
        config.getConstructs().forEach(construct -> componentConfigs.put(construct, config));
        config.getMessageSources().forEach(source -> componentConfigs.put(source, config));
        config.getOperations().forEach(operation -> componentConfigs.put(operation, config));

        if (config.getStereotype() == null) {
          config.withStereotype(newStereotype(config.getName(), namespace).withParent(CONFIG).build());
        }
      }
    }.walk(declaration);
    return componentConfigs;
  }

  private ParameterDeclaration buildConfigRefParameter(ComponentDeclaration componentDeclaration,
                                                       Collection<ConfigurationDeclaration> configs) {
    ParameterDeclaration parameter = new ParameterDeclaration(CONFIG_REF_NAME);
    parameter.setDescription("The name of the configuration to be used to execute this component");
    parameter.setRequired(!hasAnImplicitConfig(configs));
    parameter.setParameterRole(BEHAVIOUR);
    parameter.addModelProperty(new SyntheticModelModelProperty());
    parameter.setDslConfiguration(ParameterDslConfiguration.builder().allowsReferences(true).build());
    parameter.setType(CONFIG_TYPE, false);
    parameter.setExpressionSupport(expressionSupport(componentDeclaration));
    parameter.setAllowedStereotypeModels(collectStereotypes(configs));
    return parameter;
  }

  private List<StereotypeModel> collectStereotypes(Collection<ConfigurationDeclaration> configs) {
    return configs.stream().map(c -> c.getStereotype()).collect(toList());
  }

  private boolean hasAnImplicitConfig(Collection<ConfigurationDeclaration> configs) {
    List<ConfigurationDeclaration> implicitConfigs = configs.stream()
        .filter(this::canBeUsedImplicitly)
        .collect(toList());

    return implicitConfigs.stream().anyMatch(config -> {
      List<ConnectionProviderDeclaration> providers = config.getConnectionProviders();
      return providers.isEmpty() || providers.stream().anyMatch(this::canBeUsedImplicitly);
    });

  }

  private boolean canBeUsedImplicitly(ParameterizedDeclaration<?> parameterized) {
    if (parameterized.getModelProperty(NoImplicitModelProperty.class).isPresent()) {
      return false;
    }

    return parameterized.getAllParameters().stream().noneMatch(ParameterDeclaration::isRequired);
  }

  private static ExpressionSupport expressionSupport(ComponentDeclaration<?> componentDeclaration) {
    return componentDeclaration instanceof OperationDeclaration ? SUPPORTED : NOT_SUPPORTED;
  }
}
