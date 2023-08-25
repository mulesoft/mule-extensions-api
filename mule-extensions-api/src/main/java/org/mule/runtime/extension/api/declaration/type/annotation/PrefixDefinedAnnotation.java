/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import org.mule.runtime.api.util.Preconditions;

import org.apache.commons.lang3.StringUtils;

/**
 * Pojo used to store annotations which value has the format prefix:element.
 * 
 * @since 1.0
 */
public abstract class PrefixDefinedAnnotation {

  private static final String DELIMITER = ":";
  private String prefix = StringUtils.EMPTY;
  private String element = StringUtils.EMPTY;

  public PrefixDefinedAnnotation() {}

  public PrefixDefinedAnnotation(String prefix, String content) {
    setValues(prefix, content);
  }

  public PrefixDefinedAnnotation(String formattedAnnotation) throws IllegalArgumentException {
    if (StringUtils.isBlank(formattedAnnotation)) {
      return;
    }
    String[] splittedAnnotation = formattedAnnotation.split(DELIMITER);
    Preconditions.checkArgument(splittedAnnotation.length == 2,
                                String.format("%s is not a valid format. prefix:content is expected", formattedAnnotation));
    setValues(StringUtils.trim(splittedAnnotation[0]), StringUtils.trim(splittedAnnotation[1]));
  }

  void setValues(String prefix, String content) {
    Preconditions.checkArgument(!StringUtils.isBlank(prefix) && !StringUtils.isBlank(content), String
        .format("prefix and content should both be specified, got prefix: %s and content: %s", prefix, content));
    this.prefix = prefix;
    this.element = content;
  }

  public boolean isDefined() {
    return (StringUtils.isNotEmpty(prefix) && StringUtils.isNotEmpty(element));
  }

  public String getPrefix() {
    return this.prefix;
  }

  protected String getContent() {
    return this.element;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PrefixDefinedAnnotation) {
      return prefix.equals(((PrefixDefinedAnnotation) obj).getPrefix())
          && element.equals(((PrefixDefinedAnnotation) obj).getContent());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return prefix.hashCode() + element.hashCode();
  }

}
