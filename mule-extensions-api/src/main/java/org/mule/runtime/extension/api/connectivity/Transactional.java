/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity;

/**
 * Base contract for a component which can participate in a transaction
 *
 * @since 1.0
 */
//TODO: MULE-8946 these methods should throw TransactionException
public interface Transactional
{

    /**
     * Begins the transaction
     */
    void begin() throws Exception;

    /**
     * Commits the transaction
     */
    void commit() throws Exception;

    /**
     * Rolls the transaction back
     */
    void rollback() throws Exception;
}
