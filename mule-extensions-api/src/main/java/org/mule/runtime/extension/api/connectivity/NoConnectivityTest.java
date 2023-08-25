/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
