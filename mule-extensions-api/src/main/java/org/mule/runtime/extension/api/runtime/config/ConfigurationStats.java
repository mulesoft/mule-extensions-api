/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.config;

import org.mule.api.annotation.NoImplement;

/**
 * Usage statistics about a {@link ConfigurationInstance}.
 * <p/>
 * Implementations of this interface are to be thread-safe
 *
 * @since 1.0
 */
@NoImplement
public interface ConfigurationStats {

  /**
   * @return The last time in milliseconds that the referenced configuration was used or referenced
   */
  long getLastUsedMillis();

  /**
   * @return How many currently executing operations are making use of the referenced configuration
   */
  int getInflightOperations();

  /**
   * @return How many currently running sources are making use of the referenced configuration
   */
  default int getRunningSources() {
    return 0;
  }
}
