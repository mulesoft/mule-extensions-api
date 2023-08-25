/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.connectivity;

import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Specialization of {@link TransactionalConnection} for connections which can participate of XA transactions
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
public interface XATransactionalConnection
    extends TransactionalConnection, org.mule.sdk.api.connectivity.XATransactionalConnection {

}
