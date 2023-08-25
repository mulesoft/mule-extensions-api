/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

/**
 * Pojo used to store a baseType when specified from {@code TypeDsl}. The format expected is prefix:element and they should both
 * always be specified.
 * 
 * @since 1.0
 */
public class DslBaseType extends PrefixDefinedAnnotation {

  public DslBaseType() {
    super();
  }

  public DslBaseType(String prefix, String type) {
    super(prefix, type);
  }

  public DslBaseType(String formattedBaseType) throws IllegalArgumentException {
    super(formattedBaseType);
  }

  public String getType() {
    return getContent();
  }
}
