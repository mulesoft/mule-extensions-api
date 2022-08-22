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
 * Describes a Map value to be created.
 *
 * @since 1.5.0
 */
@NoImplement
public interface MapValueDeclarer {

  /**
   * Describe an entry of the map
   *
   * @param name  the name of the entry being declared
   * @param value the value to be associated with the entry
   * @return this declarer
   */
  MapValueDeclarer withEntry(String name, Object value);

  /**
   * Describe an entry of the map
   *
   * @param name                  the name of the entry being declared
   * @param valueDeclarerConsumer a consumer to configure the value of the entry
   * @return this declarer
   */
  MapValueDeclarer withEntry(String name, Consumer<ValueDeclarer> valueDeclarerConsumer);

}
