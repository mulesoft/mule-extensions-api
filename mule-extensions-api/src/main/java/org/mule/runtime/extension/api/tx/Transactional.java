/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.tx;

import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Base contract for a component which can participate in a transaction
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
public interface Transactional extends org.mule.sdk.api.tx.Transactional {
}
