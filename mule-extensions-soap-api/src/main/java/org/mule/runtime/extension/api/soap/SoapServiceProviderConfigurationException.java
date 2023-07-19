/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
