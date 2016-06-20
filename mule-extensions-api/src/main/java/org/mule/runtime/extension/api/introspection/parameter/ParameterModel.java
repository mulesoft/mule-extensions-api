/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.parameter;

import static java.util.Arrays.asList;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.MetadataManager;
import org.mule.runtime.extension.api.introspection.ComponentModel;
import org.mule.runtime.extension.api.introspection.Described;
import org.mule.runtime.extension.api.introspection.EnrichableModel;
import org.mule.runtime.extension.api.introspection.Named;
import org.mule.runtime.extension.api.introspection.config.ConfigurationModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A parameter of a {@link ComponentModel Component} or Configuration
 * <p>
 * A parameter provides a name, a type and a default value.
 * </p>
 * It can apply either to a {@link ConfigurationModel} or a
 * {@link ComponentModel}
 *
 * @since 1.0
 */
public interface ParameterModel extends Named, Described, EnrichableModel
{

    Set<String> RESERVED_NAMES = Collections.unmodifiableSet(new HashSet<>(asList("name")));

    /**
     * Returns the type of the {@link ParameterModel Parameter}
     *
     * @return a not {@code null} {@link MetadataType}
     */
    MetadataType getType();

    /**
     * Returns {@code true} if the type of the {@link ParameterModel parameter}
     * is of dynamic kind, and has to be discovered during design time
     * using the {@link MetadataManager} service.
     *
     * @return {@code true} if {@code this} element type is of dynamic kind
     */
    boolean hasDynamicType();

    /**
     * Whether or not this parameter is required. This method is exclusive with
     * {@link #getDefaultValue()} in the sense that a required parameter cannot have a default
     * value. At the same time, if the parameter has a default value, then it makes no sense
     * to consider it as required
     *
     * @return a boolean value saying if this parameter is required or not
     */
    boolean isRequired();

    /**
     * The level of support {@code this} parameter has for expressions
     *
     * @return a {@link ExpressionSupport}
     */
    ExpressionSupport getExpressionSupport();

    /**
     * The default value for this parameter. It might be an expression if
     * {@link #getExpressionSupport()} returns {@link ExpressionSupport#REQUIRED}
     * or {@link ExpressionSupport#SUPPORTED}.
     * <p>
     * This method is exclusive with {@link #isRequired()}. Check that method's comments for
     * more information on the semantics of this two methods.
     *
     * @return the default value
     */
    Object getDefaultValue();
}
