/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.nested;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;

import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ComponentModelVisitor;
import org.mule.runtime.api.meta.model.ComponentVisibility;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModelVisitor;
import org.mule.runtime.api.meta.model.nested.NestedComponentModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.model.AbstractComponentModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * Immutable concrete implementation of {@link NestedComponentModel}
 *
 * @since 1.0
 */
public class ImmutableNestedComponentModel extends AbstractComponentModel implements NestedComponentModel {

  /**
   * @deprecated since 1.4.0. Test {@link #minOccurs} to be greater than zero instead.
   */
  @Deprecated
  private final boolean isRequired;
  private final Set<StereotypeModel> allowedStereotypes;
  private final int minOccurs;
  private final Integer maxOccurs;

  /**
   * Creates a new instance
   *
   * @param name               the model's name
   * @param description        the model's description
   * @param isRequired         whether or not this component is required
   * @param allowedStereotypes A {@link Set} of custom properties which extend this model
   * @param displayModel       a model containing directives about how this component is to be displayed in the UI
   * @param visibility         the model's {@link ComponentVisibility}
   * @param modelProperties    A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank
   */
  public ImmutableNestedComponentModel(String name, String description,
                                       DisplayModel displayModel,
                                       boolean isRequired,
                                       Set<StereotypeModel> allowedStereotypes,
                                       ComponentVisibility visibility,
                                       Set<ModelProperty> modelProperties) {
    this(name, description, emptyList(), isRequired ? 1 : 0, 1, allowedStereotypes, emptyList(), displayModel, emptySet(),
         null, visibility, modelProperties, null, emptySet());
  }

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param minOccurs            the minimum number of instances required for this component
   * @param maxOccurs            maximum amount of times that this component can be used inside the owning one. {@code null} means
   *                             unbounded.
   * @param allowedStereotypes   a {@link Set} with the {@link StereotypeModel}s that can be assigned to this nested element.
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param stereotype           the {@link StereotypeModel stereotype} of this component
   * @param visibility           the model's {@link ComponentVisibility}
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param deprecationModel     a {@link DeprecationModel} describing if the component is deprecated. A null value means it is
   *                             not deprecated.
   * @param semanticTerms        a {@link Set} of semantic terms which describe the component's meaning and effect
   * @throws IllegalArgumentException if {@code name} is blank
   * @since 1.4.0
   */
  public ImmutableNestedComponentModel(String name,
                                       String description,
                                       List<ParameterGroupModel> parameterGroupModels,
                                       int minOccurs,
                                       Integer maxOccurs,
                                       Set<StereotypeModel> allowedStereotypes,
                                       List<? extends NestableElementModel> nestedComponents,
                                       DisplayModel displayModel,
                                       Set<ErrorModel> errors,
                                       StereotypeModel stereotype,
                                       ComponentVisibility visibility,
                                       Set<ModelProperty> modelProperties,
                                       DeprecationModel deprecationModel,
                                       Set<String> semanticTerms) {
    this(name, description, parameterGroupModels, minOccurs, maxOccurs, allowedStereotypes, nestedComponents, displayModel,
         errors, stereotype, visibility, modelProperties, deprecationModel, semanticTerms, null);
  }

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param minOccurs            the minimum number of instances required for this component
   * @param maxOccurs            maximum amount of times that this component can be used inside the owning one. {@code null} means
   *                             unbounded.
   * @param allowedStereotypes   a {@link Set} with the {@link StereotypeModel}s that can be assigned to this nested element.
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param stereotype           the {@link StereotypeModel stereotype} of this component
   * @param visibility           the model's {@link ComponentVisibility}
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param deprecationModel     a {@link DeprecationModel} describing if the component is deprecated. A null value means it is
   *                             not deprecated.
   * @param semanticTerms        a {@link Set} of semantic terms which describe the component's meaning and effect
   * @throws IllegalArgumentException if {@code name} is blank
   * @since 1.6.0
   */
  public ImmutableNestedComponentModel(String name,
                                       String description,
                                       List<ParameterGroupModel> parameterGroupModels,
                                       int minOccurs,
                                       Integer maxOccurs,
                                       Set<StereotypeModel> allowedStereotypes,
                                       List<? extends NestableElementModel> nestedComponents,
                                       DisplayModel displayModel,
                                       Set<ErrorModel> errors,
                                       StereotypeModel stereotype,
                                       ComponentVisibility visibility,
                                       Set<ModelProperty> modelProperties,
                                       DeprecationModel deprecationModel,
                                       Set<String> semanticTerms,
                                       MuleVersion minMuleVersion) {
    super(name, description, parameterGroupModels, nestedComponents, displayModel, errors, stereotype, visibility,
          modelProperties,
          deprecationModel, semanticTerms, minMuleVersion);
    this.isRequired = minOccurs > 0;
    this.minOccurs = minOccurs;
    this.maxOccurs = maxOccurs;
    this.allowedStereotypes = ImmutableSet.copyOf(allowedStereotypes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<StereotypeModel> getAllowedStereotypes() {
    return allowedStereotypes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRequired() {
    return isRequired;
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.4.0
   */
  @Override
  public int getMinOccurs() {
    return minOccurs;
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.4.0
   */
  @Override
  public Optional<Integer> getMaxOccurs() {
    return ofNullable(maxOccurs);
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept(NestableElementModelVisitor visitor) {
    visitor.visit(this);
  }
}
