/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.api.runtime.parameter;

/**
 * A Propagator that is used in SDK operations so that an extension can indicate where the distributed trace context can be set
 * and inject the distributed trace context.
 *
 * @since 4.5.0
 */
public interface DistributedTraceContextPropagator {

  /**
   * Injects the distributed trace context according to the setter.
   *
   * @param setter a {@link DistributedTraceContextSetter} used to inject the headers
   */
  void injectDistributedTraceFields(DistributedTraceContextSetter setter);
}
