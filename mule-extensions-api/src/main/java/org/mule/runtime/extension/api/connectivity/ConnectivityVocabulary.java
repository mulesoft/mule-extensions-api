/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.connectivity;

public final class ConnectivityVocabulary {

  private static final String API_CONTRACT_PREFIX = "apiContract.";
  private static final String CONNECTIVITY_PREFIX = "connectivity.";
  private static final String CORE_PREFIX = "core.";

  /**
   * An URL (potentially a template)
   */
  public static final String URL_TEMPLATE = CORE_PREFIX + "urlTemplate";

  /**
   * Describes an encoding property
   */
  public static final String ENCODING = API_CONTRACT_PREFIX + "Encoding";

  /**
   * EndPoint in the API holding a number of executable operations
   */
  public static final String ENDPOINT = API_CONTRACT_PREFIX + "endPoint";

  /**
   * Path template for an endpoint
   */
  public static final String PATH = API_CONTRACT_PREFIX + "path";


  /**
   * Describes a connection and it's authentication details.
   */
  public static final String CONNECTION = CONNECTIVITY_PREFIX + "Connection";

  /**
   * Describes a connection which doesn't support any type of authentication.
   */
  public static final String UNSECURED_CONNECTION = CONNECTIVITY_PREFIX + "UnsecureConnection";

  /**
   * Describes a connection that uses Basic as it's authentication method.
   */
  public static final String BASIC_AUTH_CONNECTION = CONNECTIVITY_PREFIX + "BasicAuthenticationConnection";


  /**
   * Describes a connection that uses Digest as it's authentication method.
   */
  public static final String DIGEST_AUTH_CONNECTION = CONNECTIVITY_PREFIX + "DigestAuthenticationConnection";

  /**
   * Describes a connection that uses Kerberos as it's authentication method.
   */
  public static final String KERBEROS_AUTH_CONNECTION = CONNECTIVITY_PREFIX + "KerberosAuthenticationConnection";

  /**
   * Describes a connection that uses an ApiKey as it's authentication method.
   */
  public static final String API_KEY_AUTH_CONNECTION = CONNECTIVITY_PREFIX + "ApiKeyAuthenticationConnection";

  /**
   * A connection that uses the OAuth Authorization Code grant type authorization method.
   */
  public static final String OAUTH_AUTHORIZATION_CODE_CONNECTION = CONNECTIVITY_PREFIX + "OAuthAuthorizationCodeConnection";

  /**
   * A connection that uses the OAuth Client Credentials grant type authorization method.
   */
  public static final String OAUTH_CLIENT_CREDENTIALS_CONNECTION = CONNECTIVITY_PREFIX + "OAuthClientCredentialsConnection";

  /**
   * Describes a connection that uses OCS for authentication.
   */
  public static final String OAUTH_PLATFORM_MANAGED_CONNECTION = CONNECTIVITY_PREFIX + "PlatformManagedOAuthConnection";

  /**
   * A custom type which models a Proxy Configuration
   */
  public static final String PROXY_CONFIGURATION_TYPE = CONNECTIVITY_PREFIX + "ProxyConfiguration";

  /**
   * A parameter which value holds a proxy configuration
   */
  public static final String PROXY_CONFIGURATION_PARAMETER = CONNECTIVITY_PREFIX + "proxyConfiguration";

  /**
   * Proxy Configuration for a connection.
   */
  public static final String NTLM_PROXY_CONFIGURATION = CONNECTIVITY_PREFIX + "NtlmProxyConfiguration";

  /**
   * A property which value should be encrypted.
   */
  public static final String SECRET = CONNECTIVITY_PREFIX + "secret";

  /**
   * A property that should be encrypted and it's content represents a plain value.
   */
  public static final String SCALAR_SECRET = CONNECTIVITY_PREFIX + "scalarSecret";

  /**
   * A property that describes a client id.
   */
  public static final String CLIENT_ID = CONNECTIVITY_PREFIX + "clientId";

  /**
   * A property that describes a password.
   */
  public static final String CLIENT_SECRET = CONNECTIVITY_PREFIX + "clientSecret";

  /**
   * A property that describes a Token ID.
   */
  public static final String TOKEN_ID = CONNECTIVITY_PREFIX + "tokenId";

  /**
   * A property that describes a password.
   */
  public static final String PASSWORD = CONNECTIVITY_PREFIX + "password";

  /**
   * A property that describes an API Key.
   */
  public static final String API_KEY = CONNECTIVITY_PREFIX + "apiKey";

  /**
   * A property that describes a secret token.
   */
  public static final String SECRET_TOKEN = CONNECTIVITY_PREFIX + "secretToken";

  /**
   * A property that describes a security token.
   */
  public static final String SECURITY_TOKEN = CONNECTIVITY_PREFIX + "securityToken";

  /**
   * A property that describes a session ID.
   */
  public static final String SESSION_ID = CONNECTIVITY_PREFIX + "sessionId";

  /**
   * A property that describes a connection ID.
   */
  public static final String CONNECTION_ID = CONNECTIVITY_PREFIX + "connectionId";

  /**
   * A property that describes an Account ID.
   */
  public static final String ACCOUNT_ID = CONNECTIVITY_PREFIX + "accountId";

  /**
   * A property that describes a unique tenant as part of a system.
   */
  public static final String TENANT = CONNECTIVITY_PREFIX + "tenant";

  /**
   * A property that describes an username to connect to a system.
   */
  public static final String USERNAME = CONNECTIVITY_PREFIX + "username";

  /**
   * A property that describes a host to connect to a system.
   */
  public static final String HOST = CONNECTIVITY_PREFIX + "host";

  /**
   * A property that describes a port to connect to a system.
   */
  public static final String PORT = CONNECTIVITY_PREFIX + "port";

  /**
   * A property that describes a port to connect to a system.
   */
  public static final String NTLM_DOMAIN = CONNECTIVITY_PREFIX + "ntlmDomain";

  private ConnectivityVocabulary() {
  }
}
