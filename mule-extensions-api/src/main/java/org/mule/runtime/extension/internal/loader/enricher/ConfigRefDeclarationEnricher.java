/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.util.stream.Collectors.toList;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.declaration.fluent.ComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.property.SyntheticModelModelProperty;
import org.mule.runtime.extension.api.runtime.config.ConfigurationProvider;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

/**
 * Enriches component models that depends on a configuration adding the `config-ref` parameter to the model.
 * <p>
 * The "config-ref" parameter points to a connector configuration that will be used to execute the component, this parameter
 * is always required unless an implicit one can be created.
 *
 * @since 1.2
 */
public class ConfigRefDeclarationEnricher implements DeclarationEnricher {

  private static final String CONFIG_REF_NAME = "config-ref";
  private static final MetadataType CONFIG_TYPE = buildConfigRefType();

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    final ExtensionDeclaration declaration = extensionLoadingContext.getExtensionDeclarer().getDeclaration();
    Multimap<ComponentDeclaration, ConfigurationDeclaration> componentsConfigs = getComponentConfigsMap(declaration);
    componentsConfigs.asMap()
        .forEach((component, configs) -> component.getDefaultParameterGroup().addParameter(buildConfigRefParameter(configs)));
  }

  private Multimap<ComponentDeclaration, ConfigurationDeclaration> getComponentConfigsMap(ExtensionDeclaration declaration) {
    Multimap<ComponentDeclaration, ConfigurationDeclaration> componentConfigs = LinkedListMultimap.create();
    new IdempotentDeclarationWalker() {

      @Override
      protected void onConfiguration(ConfigurationDeclaration config) {
        config.getConstructs().forEach(construct -> componentConfigs.put(construct, config));
        config.getMessageSources().forEach(source -> componentConfigs.put(source, config));
        config.getOperations().forEach(operation -> componentConfigs.put(operation, config));
      }
    }.walk(declaration);
    return componentConfigs;
  }

  private ParameterDeclaration buildConfigRefParameter(Collection<ConfigurationDeclaration> configs) {
    ParameterDeclaration parameter = new ParameterDeclaration(CONFIG_REF_NAME);
    parameter.setDescription("The name of the configuration to be used to execute this component");
    parameter.setRequired(!hasAnImplicitConfig(configs));
    parameter.setParameterRole(BEHAVIOUR);
    parameter.addModelProperty(new SyntheticModelModelProperty());
    parameter.setDslConfiguration(ParameterDslConfiguration.builder().allowsReferences(true).build());
    parameter.setType(CONFIG_TYPE, false);
    parameter.setExpressionSupport(NOT_SUPPORTED);
    return parameter;
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
    return parameterized.getAllParameters().stream().noneMatch(ParameterDeclaration::isRequired);
  }

  private static MetadataType buildConfigRefType() {
    return BaseTypeBuilder.create(JAVA).objectType().id(ConfigurationProvider.class.getName()).build();
  }
}
