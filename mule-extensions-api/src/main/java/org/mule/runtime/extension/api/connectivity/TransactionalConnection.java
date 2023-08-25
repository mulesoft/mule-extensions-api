/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.connectivity;

import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Connections which support transaction must implement this interface in order to start or join a current transaction.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
public interface TransactionalConnection extends org.mule.sdk.api.connectivity.TransactionalConnection {

}
