/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.runtime.extension.internal.util.QNameDTO;

import javax.xml.namespace.QName;

/**
 * A {@link TypeAnnotation} which indicates that the enriched type relates to a {@link QName} which is not the one the extension
 * belongs to.
 *
 * @since 1.0
 */
public final class QNameTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "QName";
  private final QNameDTO value;

  /**
   * Creates a new instance
   *
   * @param value the referenced {@link QName}
   */
  public QNameTypeAnnotation(QName value) {
    this.value = new QNameDTO(value);
  }

  /**
   * @return The referenced {@link QName}
   */
  public QName getValue() {
    return value.getValue();
  }

  /**
   * @return {@code QName}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * @return {@code true}
   */
  @Override
  public boolean isPublic() {
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (getClass().isInstance(obj)) {
      return getName().equals(((TypeAnnotation) obj).getName());
    }

    return false;
  }

  @Override
  public int hashCode() {
    return getName().hashCode();
  }
}
