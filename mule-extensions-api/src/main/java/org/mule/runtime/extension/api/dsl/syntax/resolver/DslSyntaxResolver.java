/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.syntax.resolver;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;
import org.mule.runtime.extension.api.dsl.syntax.XmlDslSyntaxResolver;

import java.util.Optional;

/**
 * Provides the {@link DslElementSyntax} of any {@link NamedObject Component}, {@link ParameterModel Parameter} or
 * {@link MetadataType Type} within the context of the {@link ExtensionModel Extension model} where the Component was declared.
 *
 * @since 1.0
 */
public interface DslSyntaxResolver {

  /**
   * Resolves the {@link DslElementSyntax} for the given {@link NamedObject component}.
   *
   * @param component the {@link NamedObject} element to be described in the {@link DslElementSyntax}
   * @return the {@link DslElementSyntax} for the {@link NamedObject model}
   */
  DslElementSyntax resolve(final NamedObject component);

  /**
   * Resolves the {@link DslElementSyntax} for the given {@link ParameterModel parameter}, providing all the required information
   * for representing this {@code parameter} element in the DSL.
   *
   * @param parameter the {@link ParameterModel} to be described in the {@link DslElementSyntax}
   * @return the {@link DslElementSyntax} for the {@link ParameterModel parameter}
   */
  DslElementSyntax resolve(final ParameterModel parameter);

  /**
   * Resolves the {@link DslElementSyntax} for the standalone xml element for the given {@link MetadataType}
   *
   * @param type the {@link MetadataType} to be described in the {@link DslElementSyntax}
   * @return the {@link DslElementSyntax} for the top level element associated to the {@link MetadataType} or
   *         {@link Optional#empty} if the {@code type} is not supported as an standalone element
   */
  Optional<DslElementSyntax> resolve(MetadataType type);

  /**
   * Resolves the {@link DslElementSyntax} for a {@link ParameterGroupModel} that has
   * to be shown as an inline element of the DSL
   *
   * @param group the {@link ParameterGroupModel} to be described in the {@link DslElementSyntax}
   * @return the {@link DslElementSyntax} for the {@link ParameterGroupModel group}
   */
  DslElementSyntax resolveInline(ParameterGroupModel group);

  /**
   * Creates an instance using the default implementation
   *
   * @param model the {@link ExtensionModel} that provides context for resolving the component's {@link DslElementSyntax}
   * @param context the {@link DslResolvingContext} in which the Dsl resolution takes place
   * @throws IllegalArgumentException if the {@link ExtensionModel} declares an imported type from an {@link ExtensionModel} not
   *         present in the provided {@link DslResolvingContext} or if the imported {@link ExtensionModel} doesn't have any
   *         {@link ImportedTypeModel}
   * @return the default implementation of a {@link DslSyntaxResolver}
   */
  static DslSyntaxResolver getDefault(ExtensionModel model, DslResolvingContext context) {
    return new XmlDslSyntaxResolver(model, context);
  }

  /**
   * Creates an instance using the default implementation
   *
   * @param model the {@link ExtensionModel} that provides context for resolving the component's {@link DslElementSyntax}
   * @param importTypesStrategy the {@link ImportTypesStrategy} used for external types resolution
   * @throws IllegalArgumentException if the {@link ExtensionModel} declares an imported type from an {@link ExtensionModel} not
   *         present in the provided {@link DslResolvingContext} or if the imported {@link ExtensionModel} doesn't have any
   *         {@link ImportedTypeModel}
   * @return the default implementation of a {@link DslSyntaxResolver}
   */
  static DslSyntaxResolver getDefault(ExtensionModel model, ImportTypesStrategy importTypesStrategy) {
    return new XmlDslSyntaxResolver(model, importTypesStrategy);
  }

}
