/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.nested;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModelVisitor;
import org.mule.runtime.api.meta.model.nested.NestedComponentModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.model.AbstractNamedImmutableModel;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Immutable concrete implementation of {@link NestedComponentModel}
 *
 * @since 1.0
 */
public class ImmutableNestedComponentModel extends AbstractNamedImmutableModel implements NestedComponentModel {

  private final boolean isRequired;
  private final Set<StereotypeModel> allowedStereotypes;

  /**
   * Creates a new instance
   *
   * @param name            the model's name
   * @param description     the model's description
   * @param isRequired      whether or not this component is required
   * @param allowedStereotypes     A {@link Set} of custom properties which extend this model
   * @param displayModel    a model containing directives about how this component is to be displayed in the UI
   * @param modelProperties A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank
   */
  public ImmutableNestedComponentModel(String name, String description,
                                       DisplayModel displayModel,
                                       boolean isRequired,
                                       Set<StereotypeModel> allowedStereotypes,
                                       Set<ModelProperty> modelProperties) {
    super(name, description, displayModel, modelProperties);
    this.isRequired = isRequired;
    this.allowedStereotypes = ImmutableSet.copyOf(allowedStereotypes);
  }

  @Override
  public Set<StereotypeModel> getAllowedStereotypes() {
    return allowedStereotypes;
  }

  @Override
  public boolean isRequired() {
    return isRequired;
  }

  @Override
  public void accept(NestableElementModelVisitor visitor) {
    visitor.visit(this);
  }

}
