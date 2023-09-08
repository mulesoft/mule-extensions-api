/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity;

import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Marker, interface to indicate that the implementing component does not support connectivity testing.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
public interface NoConnectivityTest extends org.mule.sdk.api.connectivity.NoConnectivityTest {

}
