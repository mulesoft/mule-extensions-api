/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.extension.api.runtime.source.Source;
import org.mule.extension.api.runtime.source.SourceFactory;

/**
 * A definition of a message source in an {@link ExtensionModel}
 *
 * @since 1.0
 */
public interface SourceModel extends Described, EnrichableModel, InterceptableModel, ParametrizedModel, ExceptionEnrichableModel
{

    /**
     * @return a {@link SourceFactory} used to create instances of {@link Source} which are compliant with this model
     */
    SourceFactory getSourceFactory();

    /**
     * @return a {@link DataType} representing the payload types for the generated messages
     */
    DataType getReturnType();

    /**
     * @return a {@link DataType} representing the attribute types for the generated messages
     */
    DataType getAttributesType();
}
