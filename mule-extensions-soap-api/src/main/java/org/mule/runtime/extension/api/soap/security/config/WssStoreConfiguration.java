/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap.security.config;


/**
 * Provides methods to access the configuration of a store used to add WS security to the SOAP requests.
 *
 * @since 1.0
 */
public interface WssStoreConfiguration {

  /**
   * @return The location of the store.
   */
  String getStorePath();

  /**
   * @return The password to access the store.
   */
  String getPassword();

  /**
   * @return The type of store ("jks", "pkcs12", "jceks", or any other).
   */
  String getType();
}
