/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.tx;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.extension.api.runtime.source.SourceCallbackContext;

/**
 * Allows to manipulate the transaction that was started and bound to an specific {@link SourceCallbackContext}
 *
 * @since 1.0
 */
@NoImplement
public interface TransactionHandle extends org.mule.sdk.api.tx.TransactionHandle {
}
