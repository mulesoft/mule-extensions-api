/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.tx;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.extension.api.runtime.source.SourceCallbackContext;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Allows to manipulate the transaction that was started and bound to an specific {@link SourceCallbackContext}
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@NoImplement
public interface TransactionHandle extends org.mule.sdk.api.tx.TransactionHandle {
}
