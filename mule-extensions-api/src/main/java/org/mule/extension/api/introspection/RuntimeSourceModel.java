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
 * A specialization of the {@link SourceModel} interface which adds
 * behavioural components that are relevant to the extension's functioning
 * when in runtime.
 *
 * @see SourceModel
 * @since 1.0
 */
public interface RuntimeSourceModel extends RuntimeComponentModel, SourceModel
{

    /**
     * @return a {@link SourceFactory} used to create instances of {@link Source} which are compliant with this model
     */
    SourceFactory getSourceFactory();
}
