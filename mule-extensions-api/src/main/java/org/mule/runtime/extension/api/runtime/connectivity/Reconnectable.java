/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.connectivity;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * A component which has its own way of reconnecting.
 * <p>
 * The runtime might still try to perform reconnection on components which does not implement this interface, only that the
 * runtime will decide the logic to apply. For components which do implement this interface, the runtime will call the
 * {@link #reconnect(ConnectionException, ReconnectionCallback)} method and wait for {@code this} component to notify the result
 * through the {@link ReconnectionCallback}
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
public interface Reconnectable {

  /**
   * Performs custom reconnection logic. The runtime will consider the reconnection finished once a result has been communicated
   * through the {@code reconnectionCallback}.
   * <p>
   * This method is to implement ONE reconnection attempt. This method is not to have any retry strategies. The runtime will
   * automatically perform retries when and how it applies.
   *
   * @param exception            the connectivity error that triggered the reconnection
   * @param reconnectionCallback the callback used to notify the reconnection result
   */
  void reconnect(ConnectionException exception, ReconnectionCallback reconnectionCallback);
}
