/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.semantic;

import static java.util.stream.Collectors.toCollection;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.ACCOUNT_ID;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.API_KEY;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.API_KEY_AUTH_CONNECTION;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.BASIC_AUTH_CONNECTION;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.CLIENT_ID;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.CLIENT_SECRET;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.CONNECTION_ID;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.CONNECTIVITY_PREFIX;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.DIGEST_AUTH_CONNECTION;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.DOMAIN;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.ENDPOINT;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.HOST;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.KERBEROS_AUTH_CONNECTION;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.NTLM_DOMAIN;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.NTLM_PROXY_CONFIGURATION;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.PASSWORD;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.PORT;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.PROXY_CONFIGURATION_TYPE;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.SECRET;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.SECRET_TOKEN;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.SECURITY_TOKEN;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.SESSION_ID;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.TENANT;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.TOKEN_ID;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.UNSECURED_CONNECTION;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.URL_PATH;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.URL_TEMPLATE;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.USERNAME;

import org.mule.sdk.api.annotation.semantics.connectivity.ApiKeyAuth;
import org.mule.sdk.api.annotation.semantics.connectivity.BasicAuth;
import org.mule.sdk.api.annotation.semantics.connectivity.ConfiguresNtlmProxy;
import org.mule.sdk.api.annotation.semantics.connectivity.ConfiguresProxy;
import org.mule.sdk.api.annotation.semantics.connectivity.DigestAuth;
import org.mule.sdk.api.annotation.semantics.connectivity.Domain;
import org.mule.sdk.api.annotation.semantics.connectivity.Endpoint;
import org.mule.sdk.api.annotation.semantics.connectivity.Host;
import org.mule.sdk.api.annotation.semantics.connectivity.KerberosAuth;
import org.mule.sdk.api.annotation.semantics.connectivity.NtlmDomain;
import org.mule.sdk.api.annotation.semantics.connectivity.Port;
import org.mule.sdk.api.annotation.semantics.connectivity.Unsecured;
import org.mule.sdk.api.annotation.semantics.connectivity.Url;
import org.mule.sdk.api.annotation.semantics.connectivity.UrlPath;
import org.mule.sdk.api.annotation.semantics.security.AccountId;
import org.mule.sdk.api.annotation.semantics.security.ApiKey;
import org.mule.sdk.api.annotation.semantics.security.ClientId;
import org.mule.sdk.api.annotation.semantics.security.ClientSecret;
import org.mule.sdk.api.annotation.semantics.security.ConnectionId;
import org.mule.sdk.api.annotation.semantics.security.Password;
import org.mule.sdk.api.annotation.semantics.security.Secret;
import org.mule.sdk.api.annotation.semantics.security.SecretToken;
import org.mule.sdk.api.annotation.semantics.security.SecurityToken;
import org.mule.sdk.api.annotation.semantics.security.SessionId;
import org.mule.sdk.api.annotation.semantics.security.TenantIdentifier;
import org.mule.sdk.api.annotation.semantics.security.TokenId;
import org.mule.sdk.api.annotation.semantics.security.Username;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Helper class to assist in dealing with semantic terms.
 *
 * <b>THIS CLASS IS NOT API. Only to be used by Mule Runtime internals</b>
 *
 * @since 1.5.0
 */
public final class SemanticTermsHelper {

  /**
   * Maps SDK and Extension API annotations to a {@link Set} of semantic terms.
   */
  private static final Map<Class<? extends Annotation>, String> PARAMS_TERMS = new HashMap<>();
  private static final Map<Class<? extends Annotation>, String> CONNECTION_TERMS = new HashMap<>();


  static {
    CONNECTION_TERMS.put(ApiKeyAuth.class, API_KEY_AUTH_CONNECTION);
    CONNECTION_TERMS.put(BasicAuth.class, BASIC_AUTH_CONNECTION);
    CONNECTION_TERMS.put(DigestAuth.class, DIGEST_AUTH_CONNECTION);
    CONNECTION_TERMS.put(KerberosAuth.class, KERBEROS_AUTH_CONNECTION);
    CONNECTION_TERMS.put(Unsecured.class, UNSECURED_CONNECTION);

    PARAMS_TERMS.put(ConfiguresNtlmProxy.class, NTLM_PROXY_CONFIGURATION);
    PARAMS_TERMS.put(ConfiguresProxy.class, PROXY_CONFIGURATION_TYPE);
    PARAMS_TERMS.put(Domain.class, DOMAIN);
    PARAMS_TERMS.put(Endpoint.class, ENDPOINT);
    PARAMS_TERMS.put(Host.class, HOST);
    PARAMS_TERMS.put(NtlmDomain.class, NTLM_DOMAIN);
    PARAMS_TERMS.put(Port.class, PORT);
    PARAMS_TERMS.put(Url.class, URL_TEMPLATE);
    PARAMS_TERMS.put(UrlPath.class, URL_PATH);
    PARAMS_TERMS.put(AccountId.class, ACCOUNT_ID);
    PARAMS_TERMS.put(ApiKey.class, API_KEY);
    PARAMS_TERMS.put(ClientId.class, CLIENT_ID);
    PARAMS_TERMS.put(ClientSecret.class, CLIENT_SECRET);
    PARAMS_TERMS.put(ConnectionId.class, CONNECTION_ID);
    PARAMS_TERMS.put(Password.class, PASSWORD);
    PARAMS_TERMS.put(org.mule.runtime.extension.api.annotation.param.display.Password.class, PASSWORD);
    PARAMS_TERMS.put(Secret.class, SECRET);
    PARAMS_TERMS.put(SecretToken.class, SECRET_TOKEN);
    PARAMS_TERMS.put(SecurityToken.class, SECURITY_TOKEN);
    PARAMS_TERMS.put(SessionId.class, SESSION_ID);
    PARAMS_TERMS.put(TenantIdentifier.class, TENANT);
    PARAMS_TERMS.put(TokenId.class, TOKEN_ID);
    PARAMS_TERMS.put(Username.class, USERNAME);
  }

  public static Set<String> getConnectionTermsFromAnnotations(Function<Class<? extends Annotation>, Boolean> predicate) {
    Set<String> collector = new LinkedHashSet<>();
    collectFromAnnotations(CONNECTION_TERMS, predicate, collector);

    return collector;
  }

  public static Set<String> getParameterTermsFromAnnotations(Function<Class<? extends Annotation>, Boolean> predicate) {
    Set<String> collector = new LinkedHashSet<>();
    collectFromAnnotations(PARAMS_TERMS, predicate, collector);

    return collector;
  }

  public static Set<String> getAllTermsFromAnnotations(Function<Class<? extends Annotation>, Boolean> predicate) {
    Set<String> collector = new LinkedHashSet<>();
    collectFromAnnotations(CONNECTION_TERMS, predicate, collector);
    collectFromAnnotations(PARAMS_TERMS, predicate, collector);

    return collector;
  }

  private static void collectFromAnnotations(Map<Class<? extends Annotation>, String> terms,
                                             Function<Class<? extends Annotation>, Boolean> predicate,
                                             Set<String> collector) {
    terms.forEach((annotationType, term) -> {
      if (predicate.apply(annotationType)) {
        collector.add(term);
      }
    });
  }

  public static Set<String> getConnectionTerms(Set<String> terms) {
    return extractMatching(CONNECTION_TERMS.values(), terms);
  }

  public static Set<String> getParameterTerms(Set<String> terms) {
    return extractMatching(PARAMS_TERMS.values(), terms);
  }

  private static Set<String> extractMatching(Collection<String> searchSpace, Set<String> subset) {
    if (subset != null) {
      return subset.stream()
          .filter(term -> term.startsWith(CONNECTIVITY_PREFIX) || searchSpace.contains(term))
          .collect(toCollection(LinkedHashSet::new));
    }

    return new LinkedHashSet<>();
  }

  private SemanticTermsHelper() {}
}
