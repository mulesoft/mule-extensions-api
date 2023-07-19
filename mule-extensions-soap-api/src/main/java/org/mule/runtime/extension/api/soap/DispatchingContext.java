/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
