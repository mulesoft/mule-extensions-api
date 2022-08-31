/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client;

import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.util.DataSize;

public interface OperationParameterizer {

  OperationParameterizer withConfigRef(String configurationName);

  /**
   * Sets a parameter with a given value, automatically determining the group the parameter belongs to.
   *
   * @param parameterName the name of the parameter within the {@code paramGroupName} group to set.
   * @param value         the value of the parameter to set
   * @return {@code this} instance
   */
  OperationParameterizer withParameter(String parameterName, Object value);

  /**
   * Sets a parameter with a given value.
   *
   * @param parameterGroup the name of the group containing the parameter to set.
   * @param parameterName  the name of the parameter within the {@code paramGroupName} group to set.
   * @param value          the value of the parameter to set
   * @return {@code this} instance
   */
  OperationParameterizer withParameter(String parameterGroup, String parameterName, Object value);

  OperationParameterizer withSimpleReconnection(int frequency, int count);

  OperationParameterizer reconnectingForever(int frequency);

  OperationParameterizer withDefaultRepeatableStreaming();

  OperationParameterizer withInMemoryRepeatableStreaming(DataSize initialBufferSize,
                                                         DataSize bufferSizeIncrement,
                                                         DataSize maxBufferSize);

  OperationParameterizer withFileStoreRepeatableStreaming(DataSize maxInMemorySize);

  OperationParameterizer withDefaultRepeatableIterables();

  OperationParameterizer withInMemoryRepeatableIterables(int initialBufferSize, int bufferSizeIncrement, int maxBufferSize);

  OperationParameterizer withFileStoreRepeatableIterables(int maxInMemoryInstances);

  OperationParameterizer withNonRepeatableStreaming();

  OperationParameterizer inTheContextOf(Event event);

}
