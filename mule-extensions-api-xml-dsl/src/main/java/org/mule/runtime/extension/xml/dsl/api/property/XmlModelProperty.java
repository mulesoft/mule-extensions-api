/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.api.property;

import org.mule.runtime.extension.api.introspection.ModelProperty;

/**
 * Represents an extension's capability to be mapped and usable
 * from a Mule XML config
 *
 * @since 1.0
 */
public final class XmlModelProperty implements ModelProperty {

  private final String xsdFileName;
  private final String schemaVersion;
  private final String namespace;
  private final String namespaceUri;
  private final String schemaLocation;

  public XmlModelProperty(String schemaVersion, String namespace, String namespaceUri, String xsdFileName,
                          String schemaLocation) {
    this.schemaVersion = schemaVersion;
    this.namespace = namespace;
    this.namespaceUri = namespaceUri;
    this.xsdFileName = xsdFileName;
    this.schemaLocation = schemaLocation;
  }

  /**
   * @return The version of the module. Defaults to 1.0.
   */
  public String getSchemaVersion() {
    return schemaVersion;
  }

  /**
   * @return The extension's namespace
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @return The extension's namespace URI
   */
  public String getNamespaceUri() {
    return namespaceUri;
  }

  /**
   * @return The name of the schema file
   */
  public String getXsdFileName() {
    return xsdFileName;
  }

  /**
   * @return The Schema's Location URI
   */
  public String getSchemaLocation() {
    return schemaLocation;
  }

  /**
   * {@inheritDoc}
   *
   * @return {@code XML}
   */
  @Override
  public String getName() {
    return "XML";
  }

  /**
   * {@inheritDoc}
   *
   * @return {@code true}
   */
  @Override
  public boolean isExternalizable() {
    return true;
  }
}
