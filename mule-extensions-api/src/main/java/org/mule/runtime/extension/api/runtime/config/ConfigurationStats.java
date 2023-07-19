/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
   *
   * @deprecated Use {@link #getActiveComponents()} instead.
   */
  @Deprecated
  int getInflightOperations();

  /**
   * @return How many currently running sources are making use of the referenced configuration
   * @since 1.1.6 1.2.2 1.3.0
   *
   * @deprecated Use {@link #getActiveComponents()} instead.
   */
  @Deprecated
  default int getRunningSources() {
    return 0;
  }

  /**
   * @return How many currently active components are making use of the referenced configuration (could be running sources,
   *         in-flight operations, open streams or paging providers, etc.)
   * @since 1.2.3 1.3.1 1.4.0
   */
  default int getActiveComponents() {
    return 0;
  }
}
