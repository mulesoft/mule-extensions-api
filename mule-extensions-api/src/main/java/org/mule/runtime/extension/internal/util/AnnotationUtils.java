/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.util;

import org.mule.runtime.extension.api.annotation.Alias;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.function.Supplier;

public final class AnnotationUtils {

  private AnnotationUtils() {
  }

  public static String getAlias(Field field) {
    return getAlias(field, field::getName);
  }

  public static String getAlias(Class<?> clazz) {
    return getAlias(clazz, clazz::getSimpleName);
  }

  public static String getAlias(AnnotatedElement element, Supplier<String> defaultValue) {
    return getAlias(element::getAnnotation, defaultValue);
  }

  public static String getAlias(Function<Class<? extends Annotation>, Annotation> annotationMapper, Supplier<String> defaultValue) {
    String name = null;
    Alias legacyAlias = (Alias) annotationMapper.apply(Alias.class);
    if (legacyAlias != null) {
      name = legacyAlias.value();
    } else {
      org.mule.sdk.api.annotation.Alias alias = (org.mule.sdk.api.annotation.Alias) annotationMapper.apply(org.mule.sdk.api.annotation.Alias.class);
      if (alias != null) {
        name = alias.value();
      }
    }

    return name == null || name.length() == 0 ? defaultValue.get() : name;
  }
}
