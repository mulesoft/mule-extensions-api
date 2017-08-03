/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

/**
 * Pojo used to store a substitutionGroup when specified from {@code XmlHints}
 */
public class SubstitutionGroup {

  private static final String DELIMITER = ":";
  private static final String EMPTY = "";
  private String prefix = EMPTY;
  private String element = EMPTY;

  public SubstitutionGroup() {}

  public SubstitutionGroup(String prefix, String element) {
    this.prefix = prefix;
    this.element = element;
  }

  public SubstitutionGroup(String formattedSubstitutionGroup) throws IllegalArgumentException {
    String[] splittedSubstitutionGroup = formattedSubstitutionGroup.split(DELIMITER);
    if (splittedSubstitutionGroup.length == 0) {
      //Empty string
      return;
    }
    if (splittedSubstitutionGroup.length == 2) {
      this.prefix = splittedSubstitutionGroup[0];
      this.element = splittedSubstitutionGroup[1];
    } else {
      throw new IllegalArgumentException(formattedSubstitutionGroup
          + " is not a valid format for a substitutionGroup. prefix:element is expected");
    }
  }

  public boolean isDefined() {
    return (!prefix.equals(EMPTY) || !element.equals(EMPTY));
  }

  public String getPrefix() {
    return this.prefix;
  }

  public String getElement() {
    return this.element;
  }
}
