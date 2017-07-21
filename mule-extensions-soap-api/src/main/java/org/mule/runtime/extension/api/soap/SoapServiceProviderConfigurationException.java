/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.api.soap;

/**
 * {@link RuntimeException} that aims to be thrown in the {@link SoapServiceProvider#validateConfiguration()} when some configured
 * value is invalid.
 *
 * @since 1.0
 */
public class SoapServiceProviderConfigurationException extends RuntimeException {

  public SoapServiceProviderConfigurationException(String message) {
    super(message);
  }

  public SoapServiceProviderConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}
