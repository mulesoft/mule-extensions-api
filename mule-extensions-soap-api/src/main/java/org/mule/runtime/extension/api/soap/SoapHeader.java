/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap;

import org.mule.runtime.api.util.Preconditions;

import org.w3c.dom.Element;

/**
 * Represents and enables the construction of a Soap Header to be sent over SOAP.
 *
 * @since 4.0
 */
public class SoapHeader {

  /**
   * The name of the header.
   */
  private String name;

  /**
   * the XML value of the header. e.g.: <con:aHeader xmlns:con="http://namespace.com>ValueContent</con:aHeader>
   */
  private Element value;

  public SoapHeader(String name, Element value) {
    Preconditions.checkNotNull(name, "Name cannot be null");
    Preconditions.checkNotNull(value, "Value cannot be null");
    this.name = name;
    this.value = value;
  }

  /**
   * @return the name of the header.
   */
  public String getName() {
    return name;
  }

  /**
   * @return the header XML value.
   */
  public Element getValue() {
    return value;
  }
}
