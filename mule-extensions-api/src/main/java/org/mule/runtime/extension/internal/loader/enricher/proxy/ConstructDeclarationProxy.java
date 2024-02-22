/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher.proxy;

import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ComponentVisibility;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.declaration.fluent.*;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ConstructDeclarationProxy extends ConstructDeclaration {

  private final OperationDeclaration delegate;

  public ConstructDeclarationProxy(OperationDeclaration delegate) {
    super(delegate.getName());
    this.delegate = delegate;
  }

  @Override
  public List<NestableElementDeclaration> getNestedComponents() {
    return delegate.getNestedComponents();
  }

  @Override
  public ConstructDeclaration addNestedComponent(NestableElementDeclaration nestedComponentDeclaration) {
    this.delegate.addNestedComponent(nestedComponentDeclaration);
    return this;
  }

  @Override
  public void addErrorModel(ErrorModel errorModel) {
    this.delegate.addErrorModel(errorModel);
  }

  @Override
  public Set<ErrorModel> getErrorModels() {
    return this.delegate.getErrorModels();
  }

  @Override
  public ComponentVisibility getVisibility() {
    return delegate.getVisibility();
  }

  @Override
  public void setVisibility(ComponentVisibility visibility) {
    this.delegate.setVisibility(visibility);
  }

  @Override
  public StereotypeModel getStereotype() {
    return this.delegate.getStereotype();
  }

  @Override
  public void withStereotype(StereotypeModel stereotype) {
    this.delegate.withStereotype(stereotype);
  }

  @Override
  public Optional<DeprecationModel> getDeprecation() {
    return this.delegate.getDeprecation();
  }

  @Override
  public void withDeprecation(DeprecationModel deprecation) {
    this.delegate.withDeprecation(deprecation);
  }

  @Override
  public Optional<MuleVersion> getMinMuleVersion() {
    return this.delegate.getMinMuleVersion();
  }

  @Override
  public void withMinMuleVersion(MuleVersion minMuleVersion) {
    this.delegate.withMinMuleVersion(minMuleVersion);
  }

  @Override
  public List<ParameterGroupDeclaration> getParameterGroups() {
    return this.delegate.getParameterGroups();
  }

  @Override
  public ParameterGroupDeclaration getParameterGroup(String groupName) {
    return this.delegate.getParameterGroup(groupName);
  }

  @Override
  public ParameterGroupDeclaration getDefaultParameterGroup() {
    return this.delegate.getDefaultParameterGroup();
  }

  @Override
  public List<ParameterDeclaration> getAllParameters() {
    return this.delegate.getAllParameters();
  }

  @Override
  public void addSemanticTerm(String semanticTerm) {
    this.delegate.addSemanticTerm(semanticTerm);
  }

  @Override
  public Set<String> getSemanticTerms() {
    return this.delegate.getSemanticTerms();
  }

  @Override
  public String getName() {
    return this.delegate.getName();
  }

  @Override
  public Set<ModelProperty> getModelProperties() {
    return this.delegate.getModelProperties();
  }

  @Override
  public <P extends ModelProperty> Optional<P> getModelProperty(Class<P> propertyType) {
    return this.delegate.getModelProperty(propertyType);
  }

  @Override
  public ConstructDeclaration addModelProperty(ModelProperty modelProperty) {
    this.delegate.addModelProperty(modelProperty);
    return this;
  }

  @Override
  public String getDescription() {
    return this.delegate.getDescription();
  }

  @Override
  public void setDescription(String description) {
    this.delegate.setDescription(description);
  }

  @Override
  public DisplayModel getDisplayModel() {
    return this.delegate.getDisplayModel();
  }

  @Override
  public void setDisplayModel(DisplayModel displayModel) {
    this.delegate.setDisplayModel(displayModel);
  }
}
