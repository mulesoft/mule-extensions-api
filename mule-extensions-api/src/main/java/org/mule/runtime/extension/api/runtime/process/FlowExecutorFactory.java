/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.process;

import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.meta.model.operation.OperationModel;

/**
 * Provides a way for {@link OperationModel Operations} to obtain a {@link FlowExecutor} for any Flow in the current application,
 * propagating the original {@link Event} as received by the {@link OperationModel Operation}
 *
 * @since 1.1
 */
public interface FlowExecutorFactory {

  /**
   * Creates a new {@link FlowExecutor} for the Flow with the given {@code flowName}
   * @param flowName the reference name of the target Flow
   * @return a new instance of {@link FlowExecutor} that can execute the target Flow using the current Event
   * @throws FlowNotFoundException if the {@code flowName} doesn't match any global component
   * @throws InvalidFlowReferenceException if the {@code flowName} refers to a Component that is not a Flow
   */
  FlowExecutor newExecutor(String flowName) throws FlowNotFoundException, InvalidFlowReferenceException;

}
