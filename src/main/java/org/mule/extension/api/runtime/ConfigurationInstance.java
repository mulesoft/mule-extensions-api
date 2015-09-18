/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.runtime;

import org.mule.extension.api.introspection.ConfigurationModel;

/**
 * Just like the {@link ConfigurationModel} class provides introspection on a configuration's
 * structure and parameters, this concept provides an abstraction on an actual configuration instance
 * which was declared and instantiated.
 * <p/>
 * This abstraction is inspired in the traditional model that programming languages use to declare
 * a variable. In such model, a variable has a name, a type, a value and in some higher level languages
 * such as Java or .net they can also have metadata by the use of annotations.
 * <p/>
 * In the same way, this declaration provides a {@link #getName()}, a {@link #getModel()} (which for the
 * purpose of this abstraction acts as a type), a {@link #getValue()} (which is the actual configuration
 * instance) and some metadata ({@link #getStatistics()})
 *
 * @param <T> the generic type for the actual value
 * @since 1.0
 */
public interface ConfigurationInstance<T>
{

    /**
     * @return the name for this instance
     */
    String getName();

    /**
     * @return the {@link ConfigurationModel} this instance is based on
     */
    ConfigurationModel getModel();

    /**
     * @return the actual configuration instance to be used
     */
    T getValue();

    /**
     * @return a {@link ConfigurationStats} object with statistics about the usage of the configuration
     */
    ConfigurationStats getStatistics();

}
