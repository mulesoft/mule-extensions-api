/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.util;

import javax.xml.namespace.QName;

/**
 * DTO to avoid serialization frameworks form introspecting the JVM class {@link javax.xml.namespace.QName}, which is forbidden by
 * default from Java 17.
 * 
 * @since 1.5
 */
public final class QNameDTO {

  private final String namespaceURI;
  private final String localPart;
  private final String prefix;

  private transient QName value;

  public QNameDTO(QName value) {
    this.namespaceURI = value.getNamespaceURI();
    this.localPart = value.getLocalPart();
    this.prefix = value.getPrefix();

    this.value = value;
  }

  public QName getValue() {
    if (value == null) {
      value = new QName(namespaceURI, localPart, prefix);
    }

    return value;
  }

}
