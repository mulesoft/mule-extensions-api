/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import org.mule.runtime.extension.api.connectivity.oauth.AuthorizationCodeGrantType;
import org.mule.runtime.extension.api.connectivity.oauth.ClientCredentialsGrantType;
import org.mule.runtime.extension.api.connectivity.oauth.OAuthGrantType;
import org.mule.runtime.extension.api.connectivity.oauth.OAuthGrantTypeVisitor;
import org.mule.runtime.extension.api.security.CredentialsPlacement;

import java.io.IOException;
import java.util.Optional;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * A {@link TypeAdapter} for the {@link OAuthGrantType} hierarchy
 *
 * @since 1.2.1
 */
public class OAuthGrantTypeTypeAdapter extends TypeAdapter<OAuthGrantType> {

  private static final String GRANT_TYPE = "grantType";
  private static final String AUTH_CODE = "Authorization Code";
  private static final String CLIENT_CREDENTIALS = "Client Credentials";
  private static final String TOKEN_URL = "tokenUrl";
  private static final String EXPIRATION_REGEX = "expirationRegex";
  private static final String DEFAULT_SCOPES = "defaultScopes";
  private static final String CREDENTIALS_PLACEMENT = "credentialsPlacement";
  private static final String ACCESS_TOKEN_EXPR = "accessTokenExpr";
  private static final String REFRESH_TOKEN_EXPR = "refreshTokenExpr";
  private static final String DEFAULT_SCOPE = "defaultScope";
  private static final String ACCESS_TOKEN_URL = "accessTokenUrl";
  private static final String AUTHORIZATION_URL = "authorizationUrl";

  @Override
  public void write(JsonWriter out, OAuthGrantType value) throws IOException {
    try {
      value.accept(new OAuthGrantTypeVisitor() {

        @Override
        public void visit(AuthorizationCodeGrantType grantType) {
          try {
            out.beginObject();
            out.name(GRANT_TYPE).value(AUTH_CODE);
            out.name(ACCESS_TOKEN_URL).value(grantType.getAccessTokenUrl());
            out.name(AUTHORIZATION_URL).value(grantType.getAuthorizationUrl());
            out.name(ACCESS_TOKEN_EXPR).value(grantType.getAccessTokenExpr());
            out.name(EXPIRATION_REGEX).value(grantType.getExpirationRegex());
            out.name(REFRESH_TOKEN_EXPR).value(grantType.getRefreshTokenExpr());
            writeOptional(out, DEFAULT_SCOPE, grantType.getDefaultScope());
            out.endObject();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }

        @Override
        public void visit(ClientCredentialsGrantType grantType) {
          try {
            out.beginObject();
            out.name(GRANT_TYPE).value(CLIENT_CREDENTIALS);
            out.name(TOKEN_URL).value(grantType.getTokenUrl());
            out.name(ACCESS_TOKEN_URL).value(grantType.getAccessTokenExpr());
            out.name(EXPIRATION_REGEX).value(grantType.getExpirationRegex());
            writeOptional(out, DEFAULT_SCOPES, grantType.getDefaultScopes());
            out.name(CREDENTIALS_PLACEMENT).value(grantType.getCredentialsPlacement().name());
            out.endObject();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      });
    } catch (RuntimeException e) {

      if (e.getCause() instanceof IOException) {
        throw (IOException) e.getCause();
      }

      throw e;
    }
  }

  @Override
  public OAuthGrantType read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();

    // consider grant type could not be there for backwards compatibility
    String grantType = json.has(GRANT_TYPE) ? json.get(GRANT_TYPE).getAsString() : null;

    if (grantType == null || AUTH_CODE.equals(grantType)) {
      return new AuthorizationCodeGrantType(json.get(ACCESS_TOKEN_URL).getAsString(),
                                            json.get(AUTHORIZATION_URL).getAsString(),
                                            json.get(ACCESS_TOKEN_EXPR).getAsString(),
                                            json.get(EXPIRATION_REGEX).getAsString(),
                                            json.get(REFRESH_TOKEN_EXPR).getAsString(),
                                            getOptionalValue(json, DEFAULT_SCOPE));
    } else if (CLIENT_CREDENTIALS.equals(grantType)) {
      return new ClientCredentialsGrantType(json.get(TOKEN_URL).getAsString(),
                                            json.get(ACCESS_TOKEN_URL).getAsString(),
                                            json.get(EXPIRATION_REGEX).getAsString(),
                                            getOptionalValue(json, DEFAULT_SCOPES),
                                            CredentialsPlacement.valueOf(json.get(CREDENTIALS_PLACEMENT).getAsString()));

    } else {
      throw new IllegalArgumentException("Unsupported Grant Type: " + grantType);
    }
  }

  private String getOptionalValue(JsonObject json, String property) {
    return json.has(property) ? json.get(property).getAsString() : "";
  }

  private void writeOptional(JsonWriter out, String property, Optional<String> value) throws IOException {
    if (value.isPresent()) {
      out.name(property).value(value.get());
    }
  }
}
