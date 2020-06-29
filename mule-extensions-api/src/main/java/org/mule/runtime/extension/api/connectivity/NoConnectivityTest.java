/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity;

/**
 * Marker, interface to indicate that the implementing component does not support connectivity testing.
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.connectivity.NoConnectivityTest} instead.
 */
@Deprecated
public interface NoConnectivityTest extends org.mule.sdk.api.connectivity.NoConnectivityTest {

}
