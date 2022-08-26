/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client;

import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.util.DataSize;
import org.mule.runtime.extension.api.component.ComponentParameterization;

public interface OperationParameterizer {

  OperationParameterizer withConfigRef(String configurationName);

  OperationParameterizer parameters(ComponentParameterization<OperationModel> parameterization);

  OperationParameterizer withSimpleReconnection(int frequency, int count);

  OperationParameterizer reconnectingForever(int frequency);

  OperationParameterizer withDefaultInMemoryRepeatableStreaming();

  OperationParameterizer withInMemoryRepeatableStreaming(DataSize initialBufferSize,
                                                         DataSize bufferSizeIncrement,
                                                         DataSize maxBufferSize);

  OperationParameterizer withDefaultInMemoryRepeatableIterables();

  OperationParameterizer withInMemoryRepeatableIterables(int initialBufferSize, int bufferSizeIncrement, int maxBufferSize);

  OperationParameterizer withDefaultFileStoreRepeatableStreaming();

  OperationParameterizer withFileStoreRepeatableStreaming(DataSize maxInMemorySize);

  OperationParameterizer withDefaultFileStoreRepeatableIterables();

  OperationParameterizer withFileStoreRepeatableIterables(int maxInMemoryInstances);

  OperationParameterizer withNonRepeatableStreaming();

}
