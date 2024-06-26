/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.dsl;

import static java.lang.String.format;

/**
 * Mule DSL constants.
 *
 * This is an internal utils class (internal to the container, not the module), not to be considered part of the API. Backwards
 * compatibility not guaranteed.
 *
 * @since 1.7
 */
public interface DslConstants {

  /**
   * This is the namespace prefix for core elements in the configuration.
   */
  String CORE_PREFIX = "mule";

  /**
   * Format mask for the default location of a schema
   */
  String DEFAULT_NAMESPACE_URI_MASK = "http://www.mulesoft.org/schema/mule/%s";

  /**
   * Namespace for Mule core elements
   */
  String CORE_NAMESPACE = format(DEFAULT_NAMESPACE_URI_MASK, "core");

  /**
   * Namespace for Mule core elements
   */
  String CORE_SCHEMA_LOCATION = format("%s/%s/%s.xsd", CORE_NAMESPACE, "current", CORE_PREFIX);

  /**
   * This is the namespace prefix for EE elements in the configuration.
   */
  String EE_PREFIX = "ee";

  /**
   * Namespace for EE elements
   */
  String EE_NAMESPACE = format(DEFAULT_NAMESPACE_URI_MASK, "ee/core");

  /**
   * The name of the 'name' attribute of a DSL element
   */
  String NAME_ATTRIBUTE_NAME = "name";

  /**
   * The name of the 'config' attribute of an executable element in the DSL
   */
  String CONFIG_ATTRIBUTE_NAME = "config-ref";

  /**
   * The name of the 'key' element of a Map DSL entry
   */
  String KEY_ATTRIBUTE_NAME = "key";

  /**
   * The name of the 'value' element of a Map DSL entry
   */
  String VALUE_ATTRIBUTE_NAME = "value";

  /**
   * The identifier name of the {@code reconnection} implementation of {@code reconnectionConfig} infrastructure parameter
   */
  String RECONNECTION_ELEMENT_IDENTIFIER = "reconnection";

  /**
   * The identifier name of the {@code expirationPolicy} infrastructure parameter
   */
  String EXPIRATION_POLICY_ELEMENT_IDENTIFIER = "expiration-policy";

  /**
   * The identifier name of the {@code poolingProfile} infrastructure parameter
   */
  String POOLING_PROFILE_ELEMENT_IDENTIFIER = "pooling-profile";

  /**
   * The identifier name of the {@code tls:context} infrastructure parameter
   */
  String TLS_CONTEXT_ELEMENT_IDENTIFIER = "context";

  /**
   * The identifier name of the {@code tls:key-store} infrastructure parameter
   */
  String TLS_KEY_STORE_ELEMENT_IDENTIFIER = "key-store";

  /**
   * The identifier name of the {@code tls:trust-store} infrastructure parameter
   */
  String TLS_TRUST_STORE_ELEMENT_IDENTIFIER = "trust-store";

  /**
   * The identifier name of the {@code tls:revocation-check} infrastructure parameter
   */
  String TLS_REVOCATION_CHECK_ELEMENT_IDENTIFIER = "revocation-check";

  /**
   * The identifier name of the {@code tls:standard-revocation-check} infrastructure parameter
   */
  String TLS_STANDARD_REVOCATION_CHECK_ELEMENT_IDENTIFIER = "standard-revocation-check";

  /**
   * The identifier name of the {@code tls:custom-ocsp-responder} infrastructure parameter
   */
  String TLS_CUSTOM_OCSP_RESPONDER_ELEMENT_IDENTIFIER = "custom-ocsp-responder";

  /**
   * The identifier name of the {@code tls:crl-file} infrastructure parameter
   */
  String TLS_CRL_FILE_ELEMENT_IDENTIFIER = "crl-file";

  /**
   * The namespace URI of the {@code TLS} infrastructure parameters
   */
  String TLS_NAMESPACE_URI = "http://www.mulesoft.org/schema/mule/tls";

  /**
   * The prefix name of the {@code tls:context} infrastructure parameter
   */
  String TLS_PREFIX = "tls";

  /**
   * The identifier name of the {@code schedulingStrategy} infrastructure parameter
   */
  String SCHEDULING_STRATEGY_ELEMENT_IDENTIFIER = "scheduling-strategy";

  /**
   * The identifier name of the {@code errorMappings} infrastructure parameter
   */
  String ERROR_MAPPINGS_ELEMENT_IDENTIFIER = "error-mappings";

  /**
   * The identifier name of the {@code errorMapping} infrastructure parameter
   */
  String ERROR_MAPPING_ELEMENT_IDENTIFIER = "error-mapping";
}
