/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client;

import org.mule.runtime.api.event.Event;
import org.mule.runtime.extension.api.client.params.ComponentParameterizer;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Parameterizes an operation execution performed through the {@link ExtensionsClient}
 *
 * @since 1.5.0
 */
@MinMuleVersion("4.5.0")
public interface OperationParameterizer extends ComponentParameterizer<OperationParameterizer> {

  /**
   * Specifies an event to which this operation is relative to. This means that parameters assigned with expression values, or
   * managed streams returned will be associated to this event. If not specified, a temporary event will be generated for the
   * execution and discarded immediately after.
   *
   * @param event an {@link Event}
   * @return {@code this} instance
   */
  OperationParameterizer inTheContextOf(Event event);

}
