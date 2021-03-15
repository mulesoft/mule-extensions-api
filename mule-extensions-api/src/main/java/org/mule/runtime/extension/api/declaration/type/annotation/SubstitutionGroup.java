/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;


/**
 * Pojo used to store a substitutionGroup when specified from {@code TypeDsl}. The format expected is prefix:element and they
 * should both always be specified.
 * 
 * @since 1.0
 */
public class SubstitutionGroup extends PrefixDefinedAnnotation {

  public SubstitutionGroup() {
    super();
  }

  public SubstitutionGroup(String prefix, String element) {
    super(prefix, element);
  }

  public SubstitutionGroup(String formattedSubstitutionGroup) throws IllegalArgumentException {
    super(formattedSubstitutionGroup);
  }

  public String getElement() {
    return getContent();
  }
}
