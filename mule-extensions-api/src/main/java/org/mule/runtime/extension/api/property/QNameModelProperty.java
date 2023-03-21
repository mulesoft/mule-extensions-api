/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.extension.internal.util.QNameDTO;

import javax.xml.namespace.QName;

/**
 * A {@link ModelProperty} which indicates that the enriched model relates to a {@link QName} which is not the one the extension
 * belongs to.
 *
 * @since 1.0
 */
public final class QNameModelProperty implements ModelProperty {

  private static final long serialVersionUID = -5990616758297636399L;

  public static final String NAME = "QName";

  private final QNameDTO value;

  /**
   * Creates a new instance
   * 
   * @param value the referenced {@link QName}
   */
  public QNameModelProperty(QName value) {
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
}
