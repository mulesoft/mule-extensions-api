/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
