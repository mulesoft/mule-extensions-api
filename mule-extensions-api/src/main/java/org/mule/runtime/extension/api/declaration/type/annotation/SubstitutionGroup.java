/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;


import org.apache.commons.lang3.StringUtils;

/**
 * Pojo used to store a substitutionGroup when specified from {@code XmlHints}.
 * The format expected is prefix:element and they should both always be specified.
 * @since 1.0
 */
public class SubstitutionGroup {

  private static final String DELIMITER = ":";
  private String prefix = StringUtils.EMPTY;
  private String element = StringUtils.EMPTY;

  public SubstitutionGroup() {}

  public SubstitutionGroup(String prefix, String element) {
    setValues(prefix, element);
  }

  public SubstitutionGroup(String formattedSubstitutionGroup) throws IllegalArgumentException {
    if (StringUtils.isBlank(formattedSubstitutionGroup)) {
      return;
    }
    String[] splittedSubstitutionGroup = formattedSubstitutionGroup.split(DELIMITER);
    if (splittedSubstitutionGroup.length == 2) {
      setValues(StringUtils.trim(splittedSubstitutionGroup[0]), StringUtils.trim(splittedSubstitutionGroup[1]));
    } else {
      throw new IllegalArgumentException(String
          .format("%s is not a valid format for a substitutionGroup. prefix:element is expected", formattedSubstitutionGroup));
    }
  }

  private void setValues(String prefix, String element) {
    if (StringUtils.isBlank(prefix) || StringUtils.isBlank(element)) {
      throw new IllegalArgumentException(String
          .format("prefix and element should both be specified, got prefix: %s and element: %s", prefix, element));
    }
    this.prefix = prefix;
    this.element = element;
  }

  public boolean isDefined() {
    return (StringUtils.isNotEmpty(prefix) && StringUtils.isNotEmpty(element));
  }

  public String getPrefix() {
    return this.prefix;
  }

  public String getElement() {
    return this.element;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SubstitutionGroup) {
      return prefix.equals(((SubstitutionGroup) obj).getPrefix()) && element.equals(((SubstitutionGroup) obj).getElement());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return prefix.hashCode() + element.hashCode();
  }
}
