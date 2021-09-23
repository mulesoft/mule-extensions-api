/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.util;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.lang.model.element.Element;

public final class JavaParserUtils {

  private JavaParserUtils() {
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

  public static Optional<ExpressionSupport> getExpressionSupport(AnnotatedElement element) {
    return getExpressionSupport(element::getAnnotation);
  }

  public static Optional<ExpressionSupport> getExpressionSupport(Function<Class<? extends Annotation>, ? extends Annotation> mapper) {
    return getInfoFromAnnotation(mapper,
        Expression.class,
        org.mule.sdk.api.annotation.Expression.class,
        ann -> ann.value(),
        ann -> toMuleApi(ann.value()));
  }

  public static <R extends Annotation, S extends Annotation, T> Optional<T> getInfoFromAnnotation(
      Element element,
      Class<R> legacyAnnotationClass,
      Class<S> sdkAnnotationClass,
      Function<R, T> legacyAnnotationMapping,
      Function<S, T> sdkAnnotationMapping) {

    return getInfoFromAnnotation(element::getAnnotation,
        legacyAnnotationClass,
        sdkAnnotationClass,
        legacyAnnotationMapping,
        sdkAnnotationMapping);
  }

  private static ExpressionSupport toMuleApi(org.mule.sdk.api.meta.ExpressionSupport support) {
    if (support == org.mule.sdk.api.meta.ExpressionSupport.SUPPORTED) {
      return ExpressionSupport.SUPPORTED;
    } else if (support == org.mule.sdk.api.meta.ExpressionSupport.NOT_SUPPORTED) {
      return ExpressionSupport.NOT_SUPPORTED;
    } else if (support == org.mule.sdk.api.meta.ExpressionSupport.REQUIRED) {
      return ExpressionSupport.REQUIRED;
    } else {
      throw new IllegalModelDefinitionException("Unsupported expression support type " + support);
    }
  }

  private static <R extends Annotation, S extends Annotation, T> Optional<T> getInfoFromAnnotation(
      Function<Class<? extends Annotation>, ? extends Annotation> mapper,
      Class<R> legacyAnnotationClass,
      Class<S> sdkAnnotationClass,
      Function<R, T> legacyAnnotationMapping,
      Function<S, T> sdkAnnotationMapping) {

    R legacyAnnotation = (R) mapper.apply(legacyAnnotationClass);
    S sdkAnnotation = (S) mapper.apply(sdkAnnotationClass);

    Optional<T> result;

    if (legacyAnnotation != null) {
      result = ofNullable(legacyAnnotationMapping.apply(legacyAnnotation));
    } else if (sdkAnnotation != null) {
      result = ofNullable(sdkAnnotationMapping.apply(sdkAnnotation));
    } else {
      result = empty();
    }

    return result;
  }
}
