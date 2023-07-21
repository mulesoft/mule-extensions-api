/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import org.mule.api.annotation.NoImplement;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.Optional;

/**
 * An object which holds information about an OAuth authorization
 *
 * @since 1.2.1
 */
@MinMuleVersion("4.2.1")
@NoImplement
public interface OAuthState extends org.mule.sdk.api.connectivity.oauth.OAuthState {

}
