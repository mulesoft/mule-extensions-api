/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.model.nested;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;

import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ComponentModelVisitor;
import org.mule.runtime.api.meta.model.ComponentVisibility;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModelVisitor;
import org.mule.runtime.api.meta.model.nested.NestedRouteModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.model.AbstractComponentModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable concrete implementation of {@link NestedRouteModel}
 *
 * @since 1.0
 */
public class ImmutableNestedRouteModel extends AbstractComponentModel implements NestedRouteModel {

  private final int minOccurs;
  private final Integer maxOccurs;

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the component's {@link ParameterGroupModel parameter group models}
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param minOccurs            the minimum amount of times that this component can be used inside the owning one.
   * @param maxOccurs            the maximum number of instances allowed for this route. {@code null} means unbounded.
   * @param childComponents      the route's child components
   * @param visibility           the model's {@link ComponentVisibility}
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank
   */
  public ImmutableNestedRouteModel(String name, String description,
                                   List<ParameterGroupModel> parameterGroupModels,
                                   DisplayModel displayModel,
                                   int minOccurs,
                                   Integer maxOccurs,
                                   List<? extends NestableElementModel> childComponents,
                                   ComponentVisibility visibility,
                                   Set<ModelProperty> modelProperties) {
    this(name, description, parameterGroupModels, displayModel, minOccurs, maxOccurs, childComponents, null, visibility,
         modelProperties, null, emptySet());
  }

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the component's {@link ParameterGroupModel parameter group models}
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param minOccurs            the minimum number of instances required for this kind of route
   * @param maxOccurs            the maximum number of instances allowed for this kind of route
   * @param childComponents      the route's child components
   * @param stereotypeModel      this component's stereotype or {@code null} if it doesn't have one
   * @param visibility           the model's {@link ComponentVisibility}
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param deprecationModel     a {@link DeprecationModel} describing if the component is deprecated. A null value means it is
   *                             not deprecated.
   * @param semanticTerms        a {@link Set} of semantic terms which describe the component's meaning and effect
   * @throws IllegalArgumentException if {@code name} is blank
   * @since 1.4.0
   */
  public ImmutableNestedRouteModel(String name, String description,
                                   List<ParameterGroupModel> parameterGroupModels,
                                   DisplayModel displayModel,
                                   int minOccurs,
                                   Integer maxOccurs,
                                   List<? extends NestableElementModel> childComponents,
                                   StereotypeModel stereotypeModel,
                                   ComponentVisibility visibility,
                                   Set<ModelProperty> modelProperties,
                                   DeprecationModel deprecationModel,
                                   Set<String> semanticTerms) {
    this(name, description, parameterGroupModels, displayModel, minOccurs, maxOccurs, childComponents, stereotypeModel,
         visibility, modelProperties, deprecationModel, semanticTerms, null);
  }

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the component's {@link ParameterGroupModel parameter group models}
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param minOccurs            the minimum number of instances required for this kind of route
   * @param maxOccurs            the maximum number of instances allowed for this kind of route
   * @param childComponents      the route's child components
   * @param stereotypeModel      this component's stereotype or {@code null} if it doesn't have one
   * @param visibility           the model's {@link ComponentVisibility}
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param deprecationModel     a {@link DeprecationModel} describing if the component is deprecated. A null value means it is
   *                             not deprecated.
   * @param semanticTerms        a {@link Set} of semantic terms which describe the component's meaning and effect
   * @param minMuleVersion       the min mule version of the route model.
   * @throws IllegalArgumentException if {@code name} is blank
   * @since 1.5.0
   */
  public ImmutableNestedRouteModel(String name, String description,
                                   List<ParameterGroupModel> parameterGroupModels,
                                   DisplayModel displayModel,
                                   int minOccurs,
                                   Integer maxOccurs,
                                   List<? extends NestableElementModel> childComponents,
                                   StereotypeModel stereotypeModel,
                                   ComponentVisibility visibility,
                                   Set<ModelProperty> modelProperties,
                                   DeprecationModel deprecationModel,
                                   Set<String> semanticTerms,
                                   MuleVersion minMuleVersion) {
    super(name, description, parameterGroupModels, childComponents, displayModel, emptySet(), stereotypeModel,
          visibility, modelProperties, deprecationModel, semanticTerms, minMuleVersion);
    this.minOccurs = minOccurs;
    this.maxOccurs = maxOccurs;
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.4.0
   */
  @Override
  public void accept(ComponentModelVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public boolean isRequired() {
    return minOccurs > 0;
  }

  @Override
  public void accept(NestableElementModelVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public int getMinOccurs() {
    return minOccurs;
  }

  @Override
  public Optional<Integer> getMaxOccurs() {
    return ofNullable(maxOccurs);
  }

  @Override
  public String toString() {
    return "ImmutableNestedRouteModel{" +
        "minOccurs=" + minOccurs +
        ", maxOccurs=" + maxOccurs +
        "} " + super.toString();
  }
}
