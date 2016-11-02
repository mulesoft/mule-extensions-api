/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.config;

import org.mule.runtime.api.meta.model.config.ConfigurationModel;

/**
 * Creates instances of objects which are compliant
 * with the model described by the owning {@link ConfigurationModel}.
 *
 * @since 1.0
 */
public interface ConfigurationFactory {

  /**
   * Returns a new instance of an object which is compliant
   * with the model described by the owning {@link ConfigurationModel}.
   *
   * @return a new object
   */
  Object newInstance();

  /**
   * Returns the type of the object to be returned by this instance
   *
   * @return a {@link Class}
   */
  Class<?> getObjectType();

}
