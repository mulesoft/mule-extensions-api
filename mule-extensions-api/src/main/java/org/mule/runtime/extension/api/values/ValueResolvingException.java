/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.values;

import static org.mule.runtime.api.i18n.I18nMessageFactory.createStaticMessage;

import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.i18n.I18nMessage;
import org.mule.runtime.api.value.Value;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * {@link Exception} to indicate than an error occurred resolving {@link Value values}
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
public class ValueResolvingException extends org.mule.sdk.api.values.ValueResolvingException {

  public final static String UNKNOWN = org.mule.sdk.api.values.ValueResolvingException.UNKNOWN;
  public final static String INVALID_VALUE_RESOLVER_NAME =
      org.mule.sdk.api.values.ValueResolvingException.INVALID_VALUE_RESOLVER_NAME;
  public final static String CONNECTION_FAILURE = org.mule.sdk.api.values.ValueResolvingException.CONNECTION_FAILURE;
  public final static String INVALID_LOCATION = org.mule.sdk.api.values.ValueResolvingException.INVALID_LOCATION;
  public final static String NOT_VALUE_PROVIDER_ENABLED =
      org.mule.sdk.api.values.ValueResolvingException.NOT_VALUE_PROVIDER_ENABLED;
  public final static String MISSING_REQUIRED_PARAMETERS =
      org.mule.sdk.api.values.ValueResolvingException.MISSING_REQUIRED_PARAMETERS;

  public ValueResolvingException(String message, String failureCode) {
    super(message, failureCode);
  }

  public ValueResolvingException(String message, String failureCode, Throwable cause) {
    super(message, failureCode, cause);
  }

  public ValueResolvingException(I18nMessage message, String failureCode) {
    super(message, failureCode);
  }

  public ValueResolvingException(I18nMessage message, String failureCode, Throwable cause) {
    super(message, failureCode, cause);
  }

}
