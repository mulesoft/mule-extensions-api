/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.connectivity.oauth;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Similar to {@link Parameter}, but intended to be used exclusively on fields of a {@link ConnectionProvider}
 * which supports OAuth.
 * <p>
 * The semantics are exactly the same as with {@link Parameter}, with the added behaviour of the parameter's value
 * being sent as a custom OAuth parameter during the authentication dance.
 * <p>
 * Unlike traditional parameters (which support expressions by default), these parameters cannot support them. Actually,
 * the {@link Expression} annotation cannot be used alongside this one.
 *
 * @since 1.0
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface OAuthParameter {

  /**
   * The name under which the parameter's value is to be sent to the OAuth provider during the authentication request.
   * Not to be confused with the {@link Alias} annotation, this does not influence the parameter's name on the
   * {@link ExtensionModel}. This parameter can be used alongside {@link Alias} withou any problem.
   * <p>
   * Not specifying this annotation means using the parameter's default name.
   *
   * @return the name of the parameter during the OAuth dance
   */
  String requestAlias() default "";
}
