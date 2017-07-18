/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime;

import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.extension.api.runtime.source.Source;

/**
 * Provides information about the Mule Flow on which a {@link Source} is contained.
 * <p>
 * An implementation of {@link Source} can optionally have one (and only one) field of this type.
 * The runtime will detect that field and inject an instance with the information.
 * <p>
 * This information is typically useful to name threads, send notifications, initialise consumers, etc.
 *
 * @since 1.0
 */
public interface FlowInfo extends NamedObject {

  /**
   * @return the maximum concurrency supported by the flow
   */
  int getMaxConcurrency();

}
