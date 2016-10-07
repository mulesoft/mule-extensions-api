/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime;

import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;

import java.util.Optional;

/**
 * Just like the {@link ConfigurationModel} class provides introspection on a configuration's
 * structure and parameters, this concept provides an abstraction on an actual configuration instance
 * which was declared and instantiated.
 * <p>
 * This abstraction is inspired in the traditional model that programming languages use to declare
 * a variable. In such model, a variable has a name, a type, a value and in some higher level languages
 * such as Java or .net they can also have metadata by the use of annotations.
 * <p>
 * In the same way, this declaration provides a {@link #getName()}, a {@link #getModel()} (which for the
 * purpose of this abstraction acts as a type), a {@link #getValue()} (which is the actual configuration
 * instance) and some metadata ({@link #getStatistics()})
 * <p>
 * It can optionally also contain a {@link ConnectionProvider} which will be used to handle connections
 * in case the owning extension requires connectivity. There's a strong composition and lifecycle bound
 * between a {@link ConnectionProvider} and a {@link ConfigurationInstance}. Whatever lifecycle phase
 * is applied to a {@link ConfigurationInstance}, it should be propagated to its owned {@link ConnectionProvider}.
 * More specifically, if a {@link ConfigurationInstance} is stopped, then the {@link ConnectionProvider} should also
 * be stopped and all its active connections be released.
 *
 * @since 1.0
 */
public interface ConfigurationInstance {

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
  Object getValue();

  /**
   * @return a {@link ConfigurationStats} object with statistics about the usage of the configuration
   */
  ConfigurationStats getStatistics();

  /**
   * @return An {@link Optional} which (maybe) holds the {@link ConnectionProvider} to be used with {@code this}
   * {@link ConfigurationInstance}
   */
  Optional<ConnectionProvider> getConnectionProvider();

}
