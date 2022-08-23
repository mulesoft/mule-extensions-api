/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client;

import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.extension.api.component.ComponentParameterization;

public interface OperationParameters2 {

  OperationParameters2 withConfigRef(String configurationName);

  OperationParameters2 parameters(ComponentParameterization<OperationModel> parameterization);

  OperationParameters2 withSimpleReconnection(int frequency, int count);

  OperationParameters2 reconnectingForever(int frequency);


}
