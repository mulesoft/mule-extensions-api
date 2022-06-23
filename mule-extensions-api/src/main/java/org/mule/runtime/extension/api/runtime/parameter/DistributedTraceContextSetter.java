/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.api.runtime.parameter;

/**
 * A component that indicates where and how to inject the text key/values for distributed context propagation
 *
 * @since 4.5.0
 */
public interface DistributedTraceContextSetter {

  /**
   * Injects a text-based key/value entry corresponding for the context propagation.
   *
   * @param key   the key of the propagation entry.
   * @param value the value for the propagation entry.
   */
  void set(String key, String value);

}
