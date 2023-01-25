/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client.source;

import org.mule.api.annotation.Experimental;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.lifecycle.Disposable;
import org.mule.runtime.api.lifecycle.Startable;
import org.mule.runtime.api.lifecycle.Stoppable;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Offers management and lifecycle control over a message source created through the {@link ExtensionsClient}.
 * <p>
 * <b>NOTE:</b> Experimental feature. Backwards compatibility not guaranteed.
 *
 * @since 1.6.0
 */
@Experimental
@MinMuleVersion("4.6.0")
public interface SourceHandler extends Startable, Stoppable, Disposable {

  /**
   * Starts the source, which means it will start producing messages.
   * <p>
   * Calling this method on an already started instance has no effect.
   *
   * @throws MuleException         if it fails to start
   * @throws IllegalStateException if {@link #dispose()} has already been invoked
   */
  @Override
  void start() throws MuleException;

  /**
   * Stops the source, which means it will stop producing messages. It can be restarted by invoking {@link #start()} again.
   * <p>
   * Calling this method on an already stopped instance has no effect.
   *
   * @throws MuleException         if it fails to stop
   * @throws IllegalStateException if {@link #dispose()} has already been invoked
   */
  @Override
  void stop() throws MuleException;

  /**
   * Completely disposes the source and all its allocated resources. It can't be restarted, a new instance needs to be created.
   *
   * Invoking this method on an already disposed instance has no effect.
   */
  @Override
  void dispose();
}
