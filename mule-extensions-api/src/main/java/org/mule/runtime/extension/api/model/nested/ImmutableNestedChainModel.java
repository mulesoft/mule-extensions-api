/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.nested;

import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ComponentModelVisitor;
import org.mule.runtime.api.meta.model.ComponentVisibility;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModelVisitor;
import org.mule.runtime.api.meta.model.nested.NestedChainModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;

import java.util.List;
import java.util.Set;

/**
 * Immutable concrete implementation of {@link NestedChainModel}
 *
 * @since 1.0
 */
public class ImmutableNestedChainModel extends ImmutableNestedComponentModel implements NestedChainModel {

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
  public ImmutableNestedChainModel(String name, String description,
                                   DisplayModel displayModel,
                                   boolean isRequired,
                                   Set<StereotypeModel> allowedStereotypes,
                                   ComponentVisibility visibility,
                                   Set<ModelProperty> modelProperties) {
    super(name, description, displayModel, isRequired, allowedStereotypes, visibility, modelProperties);
  }

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param isRequired           whether or not {@code this} element is required for its owner element
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
  public ImmutableNestedChainModel(String name,
                                   String description,
                                   List<ParameterGroupModel> parameterGroupModels,
                                   boolean isRequired,
                                   Set<StereotypeModel> allowedStereotypes,
                                   List<? extends NestableElementModel> nestedComponents,
                                   DisplayModel displayModel,
                                   Set<ErrorModel> errors,
                                   StereotypeModel stereotype,
                                   ComponentVisibility visibility,
                                   Set<ModelProperty> modelProperties,
                                   DeprecationModel deprecationModel,
                                   Set<String> semanticTerms) {
    this(name,
         description,
         parameterGroupModels,
         isRequired,
         allowedStereotypes,
         nestedComponents,
         displayModel,
         errors,
         stereotype,
         visibility,
         modelProperties,
         deprecationModel,
         semanticTerms, null);
  }

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param isRequired           whether or not {@code this} element is required for its owner element
   * @param allowedStereotypes   a {@link Set} with the {@link StereotypeModel}s that can be assigned to this nested element.
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param stereotype           the {@link StereotypeModel stereotype} of this component
   * @param visibility           the model's {@link ComponentVisibility}
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param deprecationModel     a {@link DeprecationModel} describing if the component is deprecated. A null value means it is
   *                             not deprecated.
   * @param semanticTerms        a {@link Set} of semantic terms which describe the component's meaning and effect
   * @param minMuleVersion       the min mule version of the nested chain model
   * @throws IllegalArgumentException if {@code name} is blank
   * @since 1.6.0
   */
  public ImmutableNestedChainModel(String name,
                                   String description,
                                   List<ParameterGroupModel> parameterGroupModels,
                                   boolean isRequired,
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
    super(name,
          description,
          parameterGroupModels,
          isRequired ? 1 : 0,
          1,
          allowedStereotypes,
          nestedComponents,
          displayModel,
          errors,
          stereotype,
          visibility,
          modelProperties,
          deprecationModel,
          semanticTerms,
          minMuleVersion);
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
  public void accept(NestableElementModelVisitor visitor) {
    visitor.visit(this);
  }

}
