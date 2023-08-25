/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.connectivity;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Callback used to notify the runtime about the outcome of a reconnection attempt.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@NoImplement
public interface ReconnectionCallback extends org.mule.sdk.api.runtime.connectivity.ReconnectionCallback {

}
