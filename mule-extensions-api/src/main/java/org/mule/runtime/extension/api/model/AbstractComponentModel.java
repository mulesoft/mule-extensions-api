/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.model.parameter.AbstractStereotypedModel;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;


/**
 * Base class for immutable implementations of a {@link ComponentModel}
 *
 * @since 1.0
 */
public abstract class AbstractComponentModel extends AbstractStereotypedModel
    implements ComponentModel {

  private final List<? extends NestableElementModel> nestedComponents;
  private final Set<ErrorModel> errors;
  private final Set<String> semanticTerms;

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param stereotype           the {@link StereotypeModel stereotype} of this component
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @throws IllegalArgumentException if {@code name} is blank
   */
  protected AbstractComponentModel(String name,
                                   String description,
                                   List<ParameterGroupModel> parameterGroupModels,
                                   List<? extends NestableElementModel> nestedComponents,
                                   DisplayModel displayModel,
                                   Set<ErrorModel> errors,
                                   StereotypeModel stereotype,
                                   Set<ModelProperty> modelProperties) {
    this(name, description, parameterGroupModels, nestedComponents, displayModel, errors, stereotype, modelProperties, null);
  }

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param stereotype           the {@link StereotypeModel stereotype} of this component
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param deprecationModel     a {@link DeprecationModel} describing if the component is deprecated. A null value means it is
   *                             not deprecated.
   * @throws IllegalArgumentException if {@code name} is blank
   */
  protected AbstractComponentModel(String name,
                                   String description,
                                   List<ParameterGroupModel> parameterGroupModels,
                                   List<? extends NestableElementModel> nestedComponents,
                                   DisplayModel displayModel,
                                   Set<ErrorModel> errors,
                                   StereotypeModel stereotype,
                                   Set<ModelProperty> modelProperties,
                                   DeprecationModel deprecationModel) {
    this(name, description, parameterGroupModels, nestedComponents, displayModel, errors,
         stereotype, modelProperties, deprecationModel, null);
  }

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param stereotype           the {@link StereotypeModel stereotype} of this component
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param deprecationModel     a {@link DeprecationModel} describing if the component is deprecated. A null value means it is
   *                             not deprecated.
   * @param semanticTerms        a {@link Set} of semantic terms which describe the component's meaning and effect
   * @throws IllegalArgumentException if {@code name} is blank
   * @since 1.4.0
   */
  protected AbstractComponentModel(String name,
                                   String description,
                                   List<ParameterGroupModel> parameterGroupModels,
                                   List<? extends NestableElementModel> nestedComponents,
                                   DisplayModel displayModel,
                                   Set<ErrorModel> errors,
                                   StereotypeModel stereotype,
                                   Set<ModelProperty> modelProperties,
                                   DeprecationModel deprecationModel,
                                   Set<String> semanticTerms) {
    super(name, description, parameterGroupModels, displayModel, stereotype, modelProperties, deprecationModel);

    this.nestedComponents = copy(nestedComponents);
    this.errors = ImmutableSet.copyOf(errors);
    this.semanticTerms = semanticTerms != null ? unmodifiableSet(semanticTerms) : emptySet();
  }

  /**
   * {@inheritDoc}
   */
  public Set<ErrorModel> getErrorModels() {
    return errors;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<? extends NestableElementModel> getNestedComponents() {
    return nestedComponents;
  }

  /**
   * {@inheritDoc}
   *
   * @since 1.4.0
   */
  @Override
  public Set<String> getSemanticTerms() {
    return semanticTerms;
  }
}
