/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.Optional;

/**
 * Provides information about the request that is sent to the authorization url during an OAuth2 dance using the
 * Authorization-Code grant type.
 * <p>
 * If the acting {@link ConnectionProvider} has a {@code before} flow configured, then an instance of this class will be initial
 * payload of such flow.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
public interface AuthCodeRequest extends org.mule.sdk.api.connectivity.oauth.AuthCodeRequest {

}
