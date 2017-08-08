/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.apache.commons.lang3.StringUtils;

public abstract class PrefixDefinedAnnotation {

  private static final String DELIMITER = ":";
  private String prefix = StringUtils.EMPTY;
  private String content = StringUtils.EMPTY;

  public PrefixDefinedAnnotation() {}

  public PrefixDefinedAnnotation(String prefix, String content) {
    setValues(prefix, content);
  }

  public PrefixDefinedAnnotation(String formattedAnnotation) throws IllegalArgumentException {
    if (StringUtils.isBlank(formattedAnnotation)) {
      return;
    }
    String[] splittedAnnotation = formattedAnnotation.split(DELIMITER);
    if (splittedAnnotation.length == 2) {
      setValues(StringUtils.trim(splittedAnnotation[0]), StringUtils.trim(splittedAnnotation[1]));
    } else {
      throw new IllegalArgumentException(String
          .format("%s is not a valid format. prefix:content is expected", formattedAnnotation));
    }
  }

  void setValues(String prefix, String content) {
    if (StringUtils.isBlank(prefix) || StringUtils.isBlank(content)) {
      throw new IllegalArgumentException(String
          .format("prefix and content should both be specified, got prefix: %s and content: %s", prefix, content));
    }
    this.prefix = prefix;
    this.content = content;
  }

  public boolean isDefined() {
    return (StringUtils.isNotEmpty(prefix) && StringUtils.isNotEmpty(content));
  }

  public String getPrefix() {
    return this.prefix;
  }

  public String getContent() {
    return this.content;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PrefixDefinedAnnotation) {
      return prefix.equals(((PrefixDefinedAnnotation) obj).getPrefix())
          && content.equals(((PrefixDefinedAnnotation) obj).getContent());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return prefix.hashCode() + content.hashCode();
  }

}
