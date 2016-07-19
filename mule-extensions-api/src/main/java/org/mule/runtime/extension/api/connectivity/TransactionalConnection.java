/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity;

/**
 * Connections which support transaction must implement this interface
 * in order to start or join a current transaction.
 *
 * @since 1.0
 */
public interface TransactionalConnection extends Transactional
{

}
