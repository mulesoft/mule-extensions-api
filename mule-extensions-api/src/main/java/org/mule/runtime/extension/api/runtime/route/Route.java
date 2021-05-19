/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.route;

import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.stereotype.AllowedStereotypes;
import org.mule.runtime.extension.api.runtime.process.RouterCompletionCallback;

/**
 * A {@link Route} allows a given {@link ComponentModel Operation} to receive a chain of message processors along with a set of
 * parameters, to be executed as part of the operation's execution.
 * <p>
 * Routes should be used for receiving multiple possible paths of execution, where the {@link ParameterModel parameters} of the
 * {@link Route} can be used to decide whether or not the contained processors should be executed. {@link StereotypeModel
 * Stereotypes} can be used in the {@link Route} declaration to define which are the <b>allowed stereotypes</b> of the received
 * processors.
 * <p>
 * This {@link Route} will not allow complex parameters to be defined inline, since the DSL content of the element will be
 * exclusive of the parameterized processors.
 *
 * @since 1.0
 * @see Chain
 * @see AllowedStereotypes
 * @see RouterCompletionCallback
 */
public abstract class Route {

  @Parameter
  private Chain processors;

  public Chain getChain() {
    return processors;
  }

}
