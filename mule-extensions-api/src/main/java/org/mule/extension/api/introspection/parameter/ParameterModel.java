/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.parameter;

import static java.util.Arrays.asList;

import org.mule.extension.api.introspection.Described;
import org.mule.extension.api.introspection.EnrichableModel;
import org.mule.extension.api.introspection.config.ConfigurationModel;
import org.mule.extension.api.introspection.operation.OperationModel;
import org.mule.metadata.api.model.MetadataType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A parameter of an operation or configuration
 * <p>
 * A parameter provides a name and
 * </p>
 * It can apply either to a {@link ConfigurationModel} or a
 * {@link OperationModel}
 *
 * @since 1.0
 */
public interface ParameterModel extends Described, EnrichableModel
{

    Set<String> RESERVED_NAMES = Collections.unmodifiableSet(new HashSet<>(asList("name")));

    /**
     * Returns the type of the parameter
     *
     * @return a not {@code null} {@link MetadataType}
     */
    MetadataType getType();

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
