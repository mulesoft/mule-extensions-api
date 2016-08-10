/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.manifest;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Allows serializing {@link Map.Entry} instances as a {@code &lt;property&gt;} element.
 *
 * This class is for internal use only. Users should not reference it.
 *
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public final class XmlProperty {

  @XmlAttribute
  private String key;

  @XmlAttribute
  private String value;

  /**
   * Creates a new instance
   */
  XmlProperty() {}

  /**
   * Creaes a new instance
   *
   * @param key   the initial {@link #key} value
   * @param value the initial {@link #value}
   */
  XmlProperty(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
