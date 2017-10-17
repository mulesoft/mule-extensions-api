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

/**
 * {@link Exception} to indicate than an error occurred resolving {@link Value values}
 *
 * @since 1.0
 */
public class ValueResolvingException extends MuleException {

  public final static String UNKNOWN = "UNKNOWN";
  public final static String INVALID_VALUE_RESOLVER_NAME = "INVALID_VALUE_RESOLVER_NAME";
  public final static String CONNECTION_FAILURE = "CONNECTION_FAILURE";
  public final static String INVALID_LOCATION = "INVALID_LOCATION";
  public final static String NOT_VALUE_PROVIDER_ENABLED = "NOT_VALUE_PROVIDER_ENABLED";
  public final static String MISSING_REQUIRED_PARAMETERS = "MISSING_REQUIRED_PARAMETERS";

  private String failureCode = UNKNOWN;

  public ValueResolvingException(String message, String failureCode) {
    super(createStaticMessage(message));
    this.failureCode = failureCode;
  }

  public ValueResolvingException(String message, String failureCode, Throwable cause) {
    super(createStaticMessage(message), cause);
    this.failureCode = failureCode;
  }

  public ValueResolvingException(I18nMessage message, String failureCode) {
    super(message);
    this.failureCode = failureCode;
  }

  public ValueResolvingException(I18nMessage message, String failureCode, Throwable cause) {
    super(message, cause);
    this.failureCode = failureCode;
  }

  /**
   * @return The failure code of the error that produced the error
   */
  public String getFailureCode() {
    return failureCode;
  }
}
