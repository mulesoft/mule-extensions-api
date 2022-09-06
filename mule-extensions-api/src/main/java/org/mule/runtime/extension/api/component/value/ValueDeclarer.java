/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.component.value;

import org.mule.api.annotation.Experimental;
import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.connection.ConnectionProvider;

import java.util.function.Consumer;

/**
 * Describes a value to be created.
 *
 * @since 1.5.0
 */
@Experimental
@NoImplement
public interface ValueDeclarer {

  /**
   * Declares that the described value represents an Object value.
   *
   * @param objectValueDeclarerConsumer a consumer to configure the value of the object
   * @return the declarer to be used to declare the object value
   */
  void objectValue(Consumer<ObjectValueDeclarer> objectValueDeclarerConsumer);

  /**
   * Declares that the described value represents an Array value.
   *
   * @param arrayValueDeclarerConsumer a consumer to configure the value of the array
   * @return the declarer to be used to declare the array value
   */
  void arrayValue(Consumer<ArrayValueDeclarer> arrayValueDeclarerConsumer);

  /**
   * Declares that the described value represents a {@link org.mule.runtime.api.config.PoolingProfile}
   *
   * @param poolingProfileValueDeclarerConsumer a consumer to configure the value of the pooling profile
   * @return the declarer to be used to declare the pooling profile value
   */
  void poolingProfile(Consumer<PoolingProfileValueDeclarer> poolingProfileValueDeclarerConsumer);

  /**
   * Declares the value that is described by this declarer.
   *
   * @param value the actual value
   */
  void withValue(Object value);

}
