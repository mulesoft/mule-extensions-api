/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.component.value;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.connection.ConnectionProvider;

/**
 * Describes a value to be created.
 *
 * @since 1.5.0
 */
@NoImplement
public interface ValueDeclarer {

  /**
   * Declares that the described value represents an Object value and returns a declarer to describe it.
   *
   * @return the declarer to be used to declare the object value
   */
  ObjectValueDeclarer asObjectValue();

  /**
   * Declares that the described value represents an Array value and returns a declarer to describe it.
   *
   * @return the declarer to be used to declare the array value
   */
  ArrayValueDeclarer asArrayValue();

  /**
   * Declares the value that is described by this declarer.
   *
   * @param value the actual value
   */
  void withValue(Object value);

}
