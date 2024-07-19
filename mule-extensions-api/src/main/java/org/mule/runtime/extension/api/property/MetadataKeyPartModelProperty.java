/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import static java.lang.String.format;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.util.Preconditions.checkArgument;

import org.mule.api.annotation.Experimental;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.metadata.MetadataKey;


/**
 * A {@link ModelProperty} for {@link ParameterModel} of {@link SourceModel} and {@link OperationModel} parameters that indicates
 * that the is a part of a {@link MetadataKey}.
 *
 * @since 1.0
 */
public final class MetadataKeyPartModelProperty implements ModelProperty {

  private final int order;
  private final boolean providedByKeyResolver;
  private final ExpressionSupport expressionSupport;

  /**
   * Creates a new instance.
   *
   * @param order the order of the parameter in the {@link MetadataKey};
   */
  public MetadataKeyPartModelProperty(int order) {
    this(order, true);
  }

  /**
   * Creates a new instance.
   *
   * @param order                 the order of the parameter in the {@link MetadataKey};
   * @param providedByKeyResolver whether or not this part will be provided by the keys resolver associated to the container of
   *                              this part
   */
  public MetadataKeyPartModelProperty(int order, boolean providedByKeyResolver) {
    this(order, providedByKeyResolver, NOT_SUPPORTED);
  }

  /**
   * Creates a new instance.
   * <p>
   * <b>NOTE:</b> Experimental feature. Backwards compatibility not guaranteed.
   *
   * @param order                 the order of the parameter in the {@link MetadataKey};
   * @param providedByKeyResolver whether or not this part will be provided by the keys resolver associated to the container of
   *                              this part
   * @param expressionSupport     the level of support this key part has for expressions
   * @since 1.8
   */
  @Experimental
  public MetadataKeyPartModelProperty(int order, boolean providedByKeyResolver, ExpressionSupport expressionSupport) {
    checkArgument(order > 0,
                  format("Invalid [order] for [MetadataKeyPart]. Expected a number greater than zero but found [%s]", order));
    this.order = order;
    this.providedByKeyResolver = providedByKeyResolver;
    this.expressionSupport = expressionSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "MetadataKeyId";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPublic() {
    return true;
  }

  /**
   * The order of this parameter in the {@link MetadataKey}.
   *
   * @return the order of the parameter for a composed {@link MetadataKey}, 0 if is a simple {@link MetadataKey}
   */
  public int getOrder() {
    return order;
  }

  /**
   * @return whether or not this part will be provided by the keys resolver associated to the container of this part, or if this
   *         part has no predefined values and has to be provided by the user entirely.
   */
  public boolean isProvidedByKeyResolver() {
    return providedByKeyResolver;
  }

  /**
   * By default, metadata keys need to be static values. An error is thrown whenever an expression is provided.
   * <p>
   * This indicates whether an eventual expression is supported/required and that it will be provided unresolved as a string for
   * the key part value.
   * <p>
   * <b>NOTE:</b> Experimental feature. Backwards compatibility not guaranteed.
   *
   * @return the level of support this key part has for expressions
   *
   * @since 1.8
   */
  @Experimental
  public ExpressionSupport getExpressionSupport() {
    return expressionSupport;
  }
}
