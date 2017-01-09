/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.model;

import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.dsl.config.ComponentConfiguration;
import org.mule.runtime.api.dsl.config.ComponentIdentifier;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.internal.dsl.model.DefaultDslElementModelResolver;

import java.util.Optional;
import java.util.Set;

/**
 * Provides the {@link DslElementModel} of any {@link ComponentConfiguration} within
 * the context of the available application plugins, provided for each instance
 * of this {@link DslElementModelResolver}
 *
 * @since 1.0
 */
public interface DslElementModelResolver {

  /**
   * Provides a default implementation of the {@link DslElementModelResolver}
   *
   * @param extensions the {@link Set} of {@link ExtensionModel} to be used as context when
   *                   performing a {@link ComponentIdentifier#getNamespace namespace}
   *                   based lookup for a given {@link ExtensionModel}.
   * @return a default implementation of the {@link DslElementModelResolver}
   */
  static DslElementModelResolver getDefault(Set<ExtensionModel> extensions) {
    return new DefaultDslElementModelResolver(extensions);
  }

  /**
   * Resolves the {@link DslElementModel} for the given {@link ComponentConfiguration applicationElement},
   * providing an element with all the required information for representing this {@code applicationElement}
   * element in the DSL and binding it to its {@link ExtensionModel model} component or {@link MetadataType}.
   * <p>
   * This resolution can only be performed from DSL top-level-elements, which have global representations
   * in the {@link ExtensionModel}, so this method will return an {@link Optional#empty} result if the
   * given {@code applicationElement} does not identify either a {@link ConfigurationModel},
   * {@link OperationModel}, {@link SourceModel} or an {@link ObjectType} than can be expressed as
   * an explicit top level element.
   *
   * @param componentConfiguration the {@link ComponentConfiguration} for which its {@link DslElementModel}
   *                           representation is required.
   * @param <T>                the expected model type of the {@link DslElementModel element}
   * @return a {@link DslElementModel} representation of the {@link ComponentConfiguration} if one
   * is possible to be built based on the {@link ExtensionModel extensions} provided as
   * resolution context, or {@link Optional#empty} if no {@link DslElementModel} could be created
   * for the given {@code applicationElement} with the current extensions context.
   */
  <T> Optional<DslElementModel<T>> resolve(ComponentConfiguration componentConfiguration);

}
