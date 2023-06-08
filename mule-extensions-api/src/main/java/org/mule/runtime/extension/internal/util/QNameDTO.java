/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
