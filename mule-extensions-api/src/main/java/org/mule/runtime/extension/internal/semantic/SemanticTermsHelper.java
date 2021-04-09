/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.semantic;

import static com.google.common.collect.ImmutableSet.of;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.ACCOUNT_ID;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.API_KEY;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.API_KEY_AUTH_CONNECTION;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.BASIC_AUTH_CONNECTION;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.CLIENT_ID;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.CLIENT_SECRET;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.CONNECTION_ID;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.DIGEST_AUTH_CONNECTION;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.DOMAIN;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.ENDPOINT;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.HOST;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.KERBEROS_AUTH_CONNECTION;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.NTLM_DOMAIN;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.NTLM_PROXY_CONFIGURATION;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.PASSWORD;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.PORT;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.PROXY_CONFIGURATION_TYPE;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.SECRET;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.SECRET_TOKEN;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.SECURITY_TOKEN;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.SESSION_ID;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.TENANT;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.TOKEN_ID;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.UNSECURED_CONNECTION;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.URL_PATH;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.URL_TEMPLATE;
import static org.mule.runtime.extension.api.connectivity.ConnectivityVocabulary.USERNAME;

import org.mule.metadata.api.builder.WithAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.SemanticTermsTypeAnnotation;
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
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class SemanticTermsHelper {

  private static final Map<Class<? extends Annotation>, Set<String>> TERMS_MAP = new HashMap<>();

  static {
    TERMS_MAP.put(ApiKeyAuth.class, of(API_KEY_AUTH_CONNECTION));
    TERMS_MAP.put(BasicAuth.class, of(BASIC_AUTH_CONNECTION));
    TERMS_MAP.put(ConfiguresNtlmProxy.class, of(NTLM_PROXY_CONFIGURATION));
    TERMS_MAP.put(ConfiguresProxy.class, of(PROXY_CONFIGURATION_TYPE));
    TERMS_MAP.put(DigestAuth.class, of(DIGEST_AUTH_CONNECTION));
    TERMS_MAP.put(Domain.class, of(DOMAIN));
    TERMS_MAP.put(Endpoint.class, of(ENDPOINT));
    TERMS_MAP.put(Host.class, of(HOST));
    TERMS_MAP.put(KerberosAuth.class, of(KERBEROS_AUTH_CONNECTION));
    TERMS_MAP.put(NtlmDomain.class, of(NTLM_DOMAIN));
    TERMS_MAP.put(Port.class, of(PORT));
    TERMS_MAP.put(Unsecured.class, of(UNSECURED_CONNECTION));
    TERMS_MAP.put(Url.class, of(URL_TEMPLATE));
    TERMS_MAP.put(UrlPath.class, of(URL_PATH));
    TERMS_MAP.put(Domain.class, of(DOMAIN));
    TERMS_MAP.put(AccountId.class, of(ACCOUNT_ID));
    TERMS_MAP.put(ApiKey.class, of(API_KEY));
    TERMS_MAP.put(ClientId.class, of(CLIENT_ID));
    TERMS_MAP.put(ClientSecret.class, of(CLIENT_SECRET));
    TERMS_MAP.put(ConnectionId.class, of(CONNECTION_ID));
    TERMS_MAP.put(Password.class, of(PASSWORD));
    TERMS_MAP.put(Secret.class, of(SECRET));
    TERMS_MAP.put(SecretToken.class, of(SECRET_TOKEN));
    TERMS_MAP.put(SecurityToken.class, of(SECURITY_TOKEN));
    TERMS_MAP.put(SessionId.class, of(SESSION_ID));
    TERMS_MAP.put(TenantIdentifier.class, of(TENANT));
    TERMS_MAP.put(TokenId.class, of(TOKEN_ID));
    TERMS_MAP.put(Username.class, of(USERNAME));
  }

  public static Set<String> getTermsFromAnnotations(AnnotatedElement annotated) {
    return getTermsFromAnnotations(a -> annotated.getAnnotation(a) != null);
  }

  public static Set<String> getTermsFromAnnotations(Function<Class<? extends Annotation>, Boolean> predicate) {
    Set<String> terms = new LinkedHashSet<>();
    TERMS_MAP.forEach((annotationType, term) -> {
      if (predicate.apply(annotationType)) {
        terms.addAll(term);
      }
    });

    return terms;
  }

  public static void enrichWithTypeAnnotation(AnnotatedElement element, final WithAnnotation annotatedBuilder) {
    Set<String> terms = getTermsFromAnnotations(element);
    if (!terms.isEmpty()) {
      annotatedBuilder.with(new SemanticTermsTypeAnnotation(terms));
    }
  }

  private SemanticTermsHelper() {}
}
