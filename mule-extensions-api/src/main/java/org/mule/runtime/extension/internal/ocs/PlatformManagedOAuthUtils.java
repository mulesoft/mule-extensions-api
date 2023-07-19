/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.ocs;

import static java.lang.System.getProperty;
import static org.mule.runtime.extension.internal.ocs.OCSConstants.OCS_ENABLED;

import org.mule.runtime.api.component.ConfigurationProperties;

/**
 * Utilities for Platform Managed OAuth Connections
 * <p>
 * Platform Managed OAuth is an experimental feature. It will only be enabled on selected environments and scenarios. Backwards
 * compatibility is not guaranteed.
 *
 * @since 1.3.0
 */
public final class PlatformManagedOAuthUtils {

  /**
   * Determines if the Platform Managed OAuth connections feature is enabled
   *
   * @param configurationProperties the application's {@link ConfigurationProperties}
   * @return Whether the feature is enabled or not
   */
  public static boolean isPlatformManagedOAuthEnabled() {
    return getProperty(OCS_ENABLED) != null;
  }

  private PlatformManagedOAuthUtils() {}
}
