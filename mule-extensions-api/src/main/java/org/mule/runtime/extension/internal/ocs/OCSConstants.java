/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.ocs;

import org.mule.api.annotation.Experimental;
import org.mule.runtime.api.component.ConfigurationProperties;

/**
 * Constants for the OAuth Client Service functionality.
 * <p>
 * These are used to extract configuration arguments from the application's {@link ConfigurationProperties} object. The following
 * properties will be fetch:
 *
 * <ul>
 * <li>{@link OCSConstants#OCS_SERVICE_URL}</li>
 * <li>{@link OCSConstants#OCS_PLATFORM_AUTH_URL}</li>
 * <li>{@link OCSConstants#OCS_CLIENT_ID}</li>
 * <li>{@link OCSConstants#OCS_CLIENT_SECRET}</li>
 * <li>{@link OCSConstants#OCS_ORG_ID}</li>
 * </ul>
 * <p>
 * If any of these properties are missing, {@link IllegalStateException} will be thrown when using this client. Because this
 * feature is at the moment experimental, the presence of these properties also act as a feature flag. If any is absent, the
 * feature will not work
 * <p>
 * Platform Managed OAuth is an experimental feature. It will only be enabled on selected environments and scenarios. Backwards
 * compatibility is not guaranteed.
 *
 * @since 1.3.0
 */
@Experimental
public final class OCSConstants {

  /**
   * Key to obtain the URL of the OCS API
   */
  public static final String OCS_SERVICE_URL = "ocs.service.url";

  /**
   * Key to obtain the URL of the OAuth service provider that grants access tokens to the OCS API
   */
  public static final String OCS_PLATFORM_AUTH_URL = "ocs.platform.authentication.url";

  /**
   * Key to obtain the path of the URL of the OAuth service provider that grants access tokens to the OCS API
   */
  public static final String OCS_PLATFORM_AUTH_PATH = "ocs.platform.authentication.path";

  /**
   * Default value of the path of the URL of the OAuth service provider that grants access tokens to the OCS API
   */
  public static final String OCS_PLATFORM_AUTH_DEFAULT_PATH = "/oauth2/token";

  /**
   * Key to obtain the client id to obtain an access token for the OCS API
   */
  public static final String OCS_CLIENT_ID = "ocs.service.client.id";

  /**
   * Key to obtain the client secret to obtain an access token to the OCS API
   */
  public static final String OCS_CLIENT_SECRET = "ocs.service.client.secret";

  /**
   * Key to obtain the organization id to be used when accessing the OCS API
   */
  public static final String OCS_ORG_ID = "csorganization.id";

  /**
   * Key to obtain the OCS API version to be used, if not specified, the default api version for the mule version running will be
   * used.
   *
   * @since 1.3.1 1.4.0
   */
  public static final String OCS_API_VERSION = "ocs.api.version";

  /**
   * Property that if set signals that OCS is supported.
   */
  public static final String OCS_ENABLED = "ocs.enabled";

  private OCSConstants() {}
}
