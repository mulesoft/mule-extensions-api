/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.parameter;

import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.message.GroupCorrelation;
import org.mule.runtime.extension.api.annotation.execution.OnError;
import org.mule.runtime.extension.api.annotation.execution.OnSuccess;

/**
 * Provides message correlation information. Operations and source callbacks (such as methods annotated with
 * {@link OnSuccess} or @{@link OnError}) can obtain an instance in order to obtain information regarding how the
 * message being processed correlates to its context.
 *
 * @since 1.1
 */
public interface CorrelationInfo {

  /**
   * @return The id of the {@link Event} being processed
   */
  String getEventId();

  /**
   * Indicates if outbound correlation ids are enabled at the application level or not. If {@code false}, outbound operations
   * should not send correlation id, unless that operation has an explicit override.
   *
   * @return whether outbound correlation ids is enabled by default or not
   */
  boolean isOutboundCorrelationEnabled();

  /**
   * @return The correlation id of the {@link Event} currently being processed
   */
  String getCorrelationId();

  /**
   * @return the {@link GroupCorrelation} of the event regarding it's relationship with other events in the same group.
   */
  GroupCorrelation getGroupCorrelation();
}
