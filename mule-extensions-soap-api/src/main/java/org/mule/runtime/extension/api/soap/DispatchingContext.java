/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap;

import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.runtime.extension.api.soap.message.MessageDispatcher;

/**
 * A resolving context that provides access to an {@link ExtensionsClient} during the creation of a {@link MessageDispatcher}.
 *
 * @since 1.1
 */
public interface DispatchingContext {

  /**
   * @return a well initialized extensions client.
   */
  ExtensionsClient getExtensionsClient();
}
