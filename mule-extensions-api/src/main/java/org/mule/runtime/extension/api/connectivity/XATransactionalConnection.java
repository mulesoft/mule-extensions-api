/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
