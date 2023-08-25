/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.lang.String.format;
import static java.util.Optional.of;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.BACK_PRESSURE_STRATEGY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.BACK_PRESSURE_STRATEGY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.STRUCTURE;

import org.mule.metadata.api.annotation.EnumAnnotation;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.IdempotentDeclarationEnricherWalkDelegate;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher;
import org.mule.runtime.extension.api.property.BackPressureStrategyModelProperty;
import org.mule.runtime.extension.api.runtime.source.BackPressureMode;

import java.util.Optional;

/**
 * Adds a parameter for back pressure on message sources that apply
 *
 * @since 1.1
 */
public class BackPressureDeclarationEnricher implements WalkingDeclarationEnricher {

  @Override
  public Optional<DeclarationEnricherWalkDelegate> getWalkDelegate(ExtensionLoadingContext extensionLoadingContext) {
    return of(new IdempotentDeclarationEnricherWalkDelegate() {

      final ExtensionDeclaration extensionDeclaration = extensionLoadingContext.getExtensionDeclarer().getDeclaration();

      @Override
      protected void onSource(SourceDeclaration sourceDeclaration) {
        Optional<BackPressureStrategyModelProperty> backPressureStrategyModelProperty =
            sourceDeclaration.getModelProperty(BackPressureStrategyModelProperty.class);

        if (backPressureStrategyModelProperty.isPresent()
            && backPressureStrategyModelProperty.get().getSupportedModes().size() > 1) {
          addBackPressureParameter(extensionDeclaration, sourceDeclaration, backPressureStrategyModelProperty.get());
        }
      }
    });
  }

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return STRUCTURE;
  }

  private void addBackPressureParameter(ExtensionDeclaration extensionDeclaration,
                                        SourceDeclaration sourceDeclaration,
                                        BackPressureStrategyModelProperty property) {

    ParameterDeclaration parameter = new ParameterDeclaration(BACK_PRESSURE_STRATEGY_PARAMETER_NAME);
    parameter.setDescription(BACK_PRESSURE_STRATEGY_PARAMETER_DESCRIPTION);
    parameter.setRequired(false);
    parameter.setDefaultValue(property.getDefaultMode());
    parameter.setExpressionSupport(NOT_SUPPORTED);
    parameter.setLayoutModel(LayoutModel.builder().tabName(ADVANCED_TAB).build());

    MetadataType type = BaseTypeBuilder.create(JAVA).stringType()
        .id(format("%s-%s-backPressureStrategy", extensionDeclaration.getName(), sourceDeclaration.getName()))
        .with(new EnumAnnotation<>(property.getSupportedModes().stream().map(BackPressureMode::name).toArray(String[]::new)))
        .with(new ClassInformationAnnotation(BackPressureMode.class))
        .build();

    parameter.setType(type, false);

    sourceDeclaration.getParameterGroup(DEFAULT_GROUP_NAME).addParameter(parameter);
  }

}
