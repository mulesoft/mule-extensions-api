/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

/**
 * Created by luciano.raineri on 8/7/17.
 */
public class BaseType extends PrefixDefinedAnnotation
{
  public BaseType() {
    super();
  }

  public BaseType(String prefix, String type) {
    super(prefix,type);
  }

  public BaseType(String formattedBaseType) throws IllegalArgumentException
  {
    super(formattedBaseType);
  }

  public String getType() {
    return getContent();
  }
}
