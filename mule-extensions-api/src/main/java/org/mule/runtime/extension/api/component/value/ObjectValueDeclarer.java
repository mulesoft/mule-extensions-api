/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.component.value;

import org.mule.api.annotation.NoImplement;

import java.util.function.Consumer;

/**
 * Describes an Object value to be created.
 *
 * It describes the Object structure but not its format. This should be used the same way regardless of the final format it will
 * have, be it a JSON document or a CSV line.
 *
 * @since 1.5.0
 */
@NoImplement
public interface ObjectValueDeclarer {

  /**
   * Describe a field of the object
   *
   * @param name  the name of the field being declared
   * @param value the value to be associated with the field
   * @return this declarer
   */
  ObjectValueDeclarer withField(String name, Object value);

  /**
   * Describe an entry of the map
   *
   * @param name                  the name of the entry being declared
   * @param valueDeclarerConsumer a consumer to configure the value of the field
   * @return this declarer
   */
  ObjectValueDeclarer withField(String name, Consumer<ValueDeclarer> valueDeclarerConsumer);

}
