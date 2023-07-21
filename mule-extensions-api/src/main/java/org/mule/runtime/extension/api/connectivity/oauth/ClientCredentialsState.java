/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.connectivity.oauth;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.extension.api.annotation.connectivity.oauth.ClientCredentials;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * {@link ConnectionProvider} implementations which are also annotated with {@link ClientCredentials} <b>MUST</b> have a field of
 * this type. When the authorization dance is completed, the runtime will inject in such field an instance of this class.
 *
 * This class holds all the relevant information about the completed authorization dance so that the {@link ConnectionProvider}
 * can make use of it.
 *
 * @since 1.2.1
 */
@MinMuleVersion("4.2.1")
@NoImplement
public interface ClientCredentialsState extends OAuthState {


}
