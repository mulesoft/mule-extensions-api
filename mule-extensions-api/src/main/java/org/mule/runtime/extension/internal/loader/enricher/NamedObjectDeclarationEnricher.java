/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static java.util.Optional.of;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.extension.api.ExtensionConstants.NAME_PARAM_DESCRIPTION;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.util.collection.SmallMap;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher;
import org.mule.runtime.extension.api.property.SyntheticModelModelProperty;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Enriches constructs models with the synthetic "name" parameter.
 * <p>
 * This name parameter is the Component ID of the construct meaning that the value associated to it can be used to reference the
 * construct in a mule application uniquely across all the instances of the same.
 *
 * @since 1.2
 */
public class NamedObjectDeclarationEnricher implements WalkingDeclarationEnricher {

  private static final MetadataType STRING_TYPE = BaseTypeBuilder.create(JAVA).stringType().build();

  private static final Map<String, Set<String>> BLOCK_LIST;

  static {
    Map<String, Set<String>> blockList = new SmallMap<>();
    blockList.put("cxf", unmodifiableSet(new HashSet<>(asList("wsSecurity", "configuration"))));
    BLOCK_LIST = unmodifiableMap(blockList);
  }

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  @Override
  public Optional<DeclarationEnricherWalkDelegate> getWalkDelegate(ExtensionLoadingContext extensionLoadingContext) {
    Set<String> blockListed = BLOCK_LIST.get(extensionLoadingContext.getExtensionDeclarer().getDeclaration().getName());
    return of(new DeclarationEnricherWalkDelegate() {

      @Override
      public void onConfiguration(ConfigurationDeclaration declaration) {
        if (blockListed != null && blockListed.contains(declaration.getName())) {
          return;
        }

        declaration.getDefaultParameterGroup().addParameter(buildNameParameter());
      }
    });
  }

  private ParameterDeclaration buildNameParameter() {
    ParameterDeclaration nameParameter = new ParameterDeclaration("name");
    nameParameter.setExpressionSupport(NOT_SUPPORTED);
    nameParameter.setParameterRole(BEHAVIOUR);
    nameParameter.addModelProperty(new SyntheticModelModelProperty());
    nameParameter.setDescription(NAME_PARAM_DESCRIPTION);
    nameParameter.setType(STRING_TYPE, false);
    nameParameter.setRequired(true);
    nameParameter.setComponentId(true);
    return nameParameter;
  }

}
