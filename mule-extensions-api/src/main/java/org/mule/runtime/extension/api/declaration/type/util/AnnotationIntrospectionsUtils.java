/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.api.declaration.type.util;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;

import org.mule.runtime.api.meta.model.display.ClassValueModel;
import org.mule.runtime.api.meta.model.display.PathModel;
import org.mule.runtime.extension.api.annotation.param.Query;
import org.mule.runtime.extension.api.annotation.param.display.ClassValue;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Path;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.param.display.Text;
import org.mule.runtime.extension.api.exception.IllegalParameterModelDefinitionException;
import org.mule.sdk.api.annotation.semantics.file.FilePath;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Utility class to introspect annotations on object fields and type classes.
 *
 * @since 1.5
 */
public class AnnotationIntrospectionsUtils {

  public static Optional<String> getDisplayName(Field field) {
    String displayNameValue = getAnnotationValueFromField(
                                                          field,
                                                          DisplayName.class,
                                                          org.mule.sdk.api.annotation.param.display.DisplayName.class,
                                                          DisplayName::value,
                                                          org.mule.sdk.api.annotation.param.display.DisplayName::value);

    return ofNullable(displayNameValue);
  }

  public static Optional<String> getSummaryValue(Field field) {
    String summaryValue = getAnnotationValueFromField(
                                                      field,
                                                      Summary.class,
                                                      org.mule.sdk.api.annotation.param.display.Summary.class,
                                                      Summary::value, org.mule.sdk.api.annotation.param.display.Summary::value);

    return ofNullable(summaryValue);
  }

  public static Optional<String> getExampleValue(Field field) {
    String exampleValue = getAnnotationValueFromField(
                                                      field,
                                                      Example.class,
                                                      org.mule.sdk.api.annotation.param.display.Example.class,
                                                      Example::value, org.mule.sdk.api.annotation.param.display.Example::value);

    return ofNullable(exampleValue);
  }

  public static Optional<PathModel> getPathModel(Field field) {
    Function<Path, PathModel> getPathModelFromLegacyAnnotation =
        (Path path) -> new PathModel(path.type(), path.acceptsUrls(), path.location(), path.acceptedFileExtensions());
    Function<FilePath, PathModel> getPathModelFromSdkAnnotation =
        (FilePath filePath) -> new PathModel(filePath.type(), filePath.acceptsUrls(), filePath.location(),
                                             filePath.acceptedFileExtensions());

    PathModel pathModel = getAnnotationValueFromField(
                                                      field,
                                                      Path.class,
                                                      FilePath.class,
                                                      getPathModelFromLegacyAnnotation, getPathModelFromSdkAnnotation);

    return ofNullable(pathModel);
  }

  public static Optional<ClassValueModel> getClassValueModel(Field field) {
    Function<ClassValue, ClassValueModel> getClassValueFromLegacyAnnotation =
        (ClassValue classValue) -> new ClassValueModel(Stream.of(classValue.extendsOrImplements()).filter(p -> !isBlank(p))
            .collect(toList()));
    Function<org.mule.sdk.api.annotation.param.display.ClassValue, ClassValueModel> getClassValueFromSdkAnnotation =
        (org.mule.sdk.api.annotation.param.display.ClassValue classValue) -> new ClassValueModel(Stream
            .of(classValue.extendsOrImplements()).filter(p -> !isBlank(p)).collect(toList()));
    ClassValueModel classValueModel = getAnnotationValueFromField(
                                                                  field,
                                                                  ClassValue.class,
                                                                  org.mule.sdk.api.annotation.param.display.ClassValue.class,
                                                                  getClassValueFromLegacyAnnotation,
                                                                  getClassValueFromSdkAnnotation);

    return ofNullable(classValueModel);
  }

  public static Optional<Pair<Integer, String>> getPlacementValue(Field field) {
    Pair<Integer, String> value = getAnnotationValueFromField(field,
                                                              Placement.class,
                                                              org.mule.sdk.api.annotation.param.display.Placement.class,
                                                              (Placement placement) -> Pair.of(placement.order(),
                                                                                               placement.tab()),
                                                              (org.mule.sdk.api.annotation.param.display.Placement placement) -> Pair
                                                                  .of(placement.order(), placement.tab()));

    return ofNullable(value);
  }

  public static boolean isTextField(Field field) {
    return isAnnotationPresent(field, Text.class, org.mule.sdk.api.annotation.param.display.Text.class);
  }

  public static boolean isPasswordField(Field field) {
    return isAnnotationPresent(field, Password.class);
  }

  public static boolean isQueryField(Field field) {
    return isAnnotationPresent(field, Query.class);
  }

  public static <R extends Annotation> boolean isAnnotationPresent(Field field, Class<R> annotationClass) {
    return field.getAnnotation(annotationClass) != null;
  }

  public static <R extends Annotation, S extends Annotation> boolean isAnnotationPresent(Field field,
                                                                                         Class<R> legacyAnnotationClass,
                                                                                         Class<S> sdkAnnotationClass) {
    R legacyAnnotation = field.getAnnotation(legacyAnnotationClass);
    S sdkAnnotation = field.getAnnotation(sdkAnnotationClass);

    if (legacyAnnotation != null && sdkAnnotation != null) {
      throw new IllegalParameterModelDefinitionException(format("Annotations %s and %s are both present at the same time on field %s",
                                                                legacyAnnotationClass.getName(), sdkAnnotationClass.getName(),
                                                                field.getName()));
    } else {
      return legacyAnnotation != null || sdkAnnotation != null;
    }
  }

  private static <R extends Annotation, S extends Annotation, T> T getAnnotationValueFromField(
                                                                                               Field field,
                                                                                               Class<R> legacyAnnotationClass,
                                                                                               Class<S> sdkAnnotationClass,
                                                                                               Function<R, T> legacyAnnotationMapping,
                                                                                               Function<S, T> sdkAnnotationMapping) {
    R legacyAnnotation = field.getAnnotation(legacyAnnotationClass);
    S sdkAnnotation = field.getAnnotation(sdkAnnotationClass);

    T value;
    if (legacyAnnotation != null && sdkAnnotation != null) {
      throw new IllegalParameterModelDefinitionException(format("Annotations %s and %s are both present at the same time on field %s",
                                                                legacyAnnotationClass.getName(), sdkAnnotationClass.getName(),
                                                                field.getName()));
    } else if (legacyAnnotation != null) {
      value = legacyAnnotationMapping.apply(legacyAnnotation);
    } else if (sdkAnnotation != null) {
      value = sdkAnnotationMapping.apply(sdkAnnotation);
    } else {
      value = null;
    }

    return value;
  }
}
