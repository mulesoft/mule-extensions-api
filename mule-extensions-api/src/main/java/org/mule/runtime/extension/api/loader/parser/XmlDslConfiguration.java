/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader.parser;

/**
 * Contains the configuration of the schema attributes
 *
 * @since 1.10.0
 */
public final class XmlDslConfiguration {

  private final String prefix;
  private final String namespace;

  public XmlDslConfiguration(String prefix, String namespace) {
    this.prefix = prefix;
    this.namespace = namespace;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getNamespace() {
    return namespace;
  }
}
