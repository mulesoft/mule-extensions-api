/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.component.value;

import org.mule.api.annotation.NoImplement;

/**
 * ADD JDOC
 *
 * @since 1.5.0
 */
@NoImplement
public interface ValueDeclarer {

  /**
   * ADD JDOC
   *
   * @return
   */
  MapValueDeclarer asMapValue();

  /**
   * ADD JDOC
   *
   * @return
   */
  ObjectValueDeclarer asObjectValue();

  /**
   * ADD JDOC
   *
   * @return
   */
  ArrayValueDeclarer asArrayValue();

  /**
   * ADD JDOC
   *
   * @param value
   */
  void withValue(Object value);

}
