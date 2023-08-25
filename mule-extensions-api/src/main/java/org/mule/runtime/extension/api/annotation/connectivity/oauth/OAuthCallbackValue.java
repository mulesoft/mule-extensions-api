/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.connectivity.oauth;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation is used to annotate fields on a {@link ConnectionProvider} which supports OAuth.
 * <p>
 * The annotation is used to provide and expression which is evaluated against the response of the access token url. The result of
 * evaluating such expression will be injected into the annotated field.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface OAuthCallbackValue {

  /**
   * Expression to extract the parameter from the oauth response
   */
  String expression();
}
