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

import java.util.function.Consumer;

public interface OperationParameterizer {

  OperationParameterizer withConfigRef(String configurationName);

  OperationParameterizer parameters(ComponentParameterization<OperationModel> parameterization);

  OperationParameterizer withSimpleReconnection(int frequency, int count);

  OperationParameterizer reconnectingForever(int frequency);

  OperationParameterizer withInMemoryRepeatableStream(Consumer<InMemoryRepeatableStreamConfigurer> configurer);

  OperationParameterizer withInMemoryRepeatableIterable(Consumer<InMemoryRepeatableIterableConfigurer> configurer);

  OperationParameterizer withFileStoreRepeatableStream(Consumer<FileStoreRepeatableStreamConfigurer> configurer);

  OperationParameterizer withNonRepeatableStreams();


  interface InMemoryRepeatableStreamConfigurer {

    InMemoryRepeatableStreamConfigurer initialBufferSize(DataSize bufferSize);

    InMemoryRepeatableStreamConfigurer bufferSizeIncrement(DataSize delta);

    InMemoryRepeatableStreamConfigurer maxBufferSize(DataSize bufferSize);
  }

  interface InMemoryRepeatableIterableConfigurer {

    InMemoryRepeatableIterableConfigurer initialBufferSize(int bufferSize);

    InMemoryRepeatableIterableConfigurer bufferSizeIncrement(int delta);

    InMemoryRepeatableIterableConfigurer maxBufferSize(int bufferSize);
  }
  interface FileStoreRepeatableStreamConfigurer {

    FileStoreRepeatableStreamConfigurer inMemoryBufferSize(DataSize bufferSize);

  }

  interface FileStoreRepeatableIterableConfigurer {

    FileStoreRepeatableIterableConfigurer inMemoryObjects(int bufferSize);
  }

}
