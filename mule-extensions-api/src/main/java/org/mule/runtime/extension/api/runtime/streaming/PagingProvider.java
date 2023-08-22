/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.streaming;

import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * This interface provides functionality for consuming a data feed in pages.
 * <p>
 * Instances should not be reused. Each execution of a paging operation should return different instances.
 * <p>
 * Implementing this interface does not guarantee thread safeness.
 *
 * @param <C> connection type expected to handle the operations.
 * @param <T> the type of the returned pages.
 * @since 1.0
 */
@MinMuleVersion("4.1")
public interface PagingProvider<C, T> extends org.mule.sdk.api.runtime.streaming.PagingProvider<C, T> {

}
