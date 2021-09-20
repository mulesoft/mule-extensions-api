/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.display.ClassValueModel;
import org.mule.runtime.api.meta.model.display.PathModel;
import org.mule.runtime.api.util.Pair;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Ignore;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Utility class to handle Java types and their relationship with the {@link MetadataType} model
 *
 * @since 1.0
 */
public final class TypeUtils {

  private TypeUtils() {}

  /**
   * Returns all the {@link Field}s in the given {@code declaringType} which are annotated with {@link Parameter} but also do not
   * have the {@link Ignore} one.
   * <p>
   * The introspection also includes parent classes.
   *
   * @param declaringType the class to introspect.
   * @return a {@link Collection} of {@link Field fields}. May be empty but will never be {@code null}
   */
  public static Collection<Field> getParameterFields(Class<?> declaringType) {
    return getAllFields(declaringType).stream()
        .filter(field -> isParameter(field) || isParameterGroup(field))
        .collect(toList());
  }

  /**
   * Returns all the {@link Field}s in the given {@code declaringType}.
   * <p>
   * The introspection also includes parent classes.
   *
   * @param declaringType the class to introspect.
   * @return a {@link Collection} of {@link Field fields}. May be empty but will never be {@code null}
   */
  public static Collection<Field> getAllFields(Class<?> declaringType) {
    return getAllSuperClasses(declaringType).stream()
        .flatMap(type -> Stream.of(type.getDeclaredFields()))
        .collect(toList());
  }

  /**
   * Returns all the superclasses of the given {@code type}, without including {@link Object}
   * 
   * @param type a type
   * @return all the type's super classes
   */
  public static Collection<Class<?>> getAllSuperClasses(final Class<?> type) {
    List<Class<?>> result = new LinkedList<>();
    if (type != null && !type.equals(Object.class)) {
      result.add(type);
      result.addAll(getAllSuperClasses(type.getSuperclass()));
    }

    return result;
  }

  /**
   * Checks the given {@code field} for the {@link Alias} annotation. If present, {@link Alias#value()} is returned. Otherwise,
   * {@link Field#getName()} is returned.
   *
   * @param field a {@link Field}
   * @return the field's alias
   */
  public static String getAlias(Field field) {
    Alias alias = field.getAnnotation(Alias.class);
    String name = alias != null ? alias.value() : null;
    return name == null || name.length() == 0 ? field.getName() : name;
  }

  /**
   * Checks if a field is a parameter
   *
   * @param field a {@link Field}
   * @return true if the {@link Field} contains either the annotation {@link Parameter} or
   *         {@link org.mule.sdk.api.annotation.param.Parameter}
   *
   * @since 4.5
   */
  public static boolean isParameter(Field field) {
    return field.isAnnotationPresent(Parameter.class)
        || field.isAnnotationPresent(org.mule.sdk.api.annotation.param.Parameter.class);
  }


  /**
   * Checks if a field is a parameter group
   *
   * @param field a {@link Field}
   * @return true if the {@link Field} contains either the annotation {@link ParameterGroup} or
   *         {@link org.mule.sdk.api.annotation.param.ParameterGroup}
   *
   * @since 4.5
   */
  public static boolean isParameterGroup(Field field) {
    return field.isAnnotationPresent(ParameterGroup.class)
        || field.isAnnotationPresent(org.mule.sdk.api.annotation.param.ParameterGroup.class);
  }

  /**
   * Checks if a field is optional or not
   *
   * @param field the field to check
   * @return whether the field is optional or not
   *
   * @since 4.5
   */
  public static boolean isOptional(Field field) {
    return field.isAnnotationPresent(Optional.class)
        || field.isAnnotationPresent(org.mule.sdk.api.annotation.param.Optional.class);
  }

  /**
   * Returns the display name of the given {@link Field} if either annotation {@link DisplayName} or
   * {@link org.mule.sdk.api.annotation.param.display.DisplayName} are present
   *
   * @param field the {@link Field} to be introspected
   * @return {@link Optional} containing the display name string if either {@link DisplayName} or
   *         {@link org.mule.sdk.api.annotation.param.display.DisplayName} are present.
   *
   * @since 4.5
   */
  public static java.util.Optional<String> getDisplayName(Field field) {
    String displayNameValue = getAnnotationValueFromField(
                                                          field,
                                                          DisplayName.class,
                                                          org.mule.sdk.api.annotation.param.display.DisplayName.class,
                                                          DisplayName::value,
                                                          org.mule.sdk.api.annotation.param.display.DisplayName::value);

    return ofNullable(displayNameValue);
  }

  /**
   * Returns the summary of the given {@link Field} if either annotation {@link Summary} or
   * {@link org.mule.sdk.api.annotation.param.display.Summary} are present
   *
   * @param field the {@link Field} to be introspected
   * @return {@link Optional} containing the summary string if either {@link Summary} or
   *         {@link org.mule.sdk.api.annotation.param.display.Summary} are present.
   *
   * @since 4.5
   */
  public static java.util.Optional<String> getSummaryValue(Field field) {
    String summaryValue = getAnnotationValueFromField(
                                                      field,
                                                      Summary.class,
                                                      org.mule.sdk.api.annotation.param.display.Summary.class,
                                                      Summary::value, org.mule.sdk.api.annotation.param.display.Summary::value);

    return ofNullable(summaryValue);
  }

  /**
   * Returns the example of the given {@link Field} if either annotation {@link Example} or
   * {@link org.mule.sdk.api.annotation.param.display.Example} are present
   *
   * @param field the {@link Field} to be introspected
   * @return {@link Optional} containing the example string if either {@link Example} or
   *         {@link org.mule.sdk.api.annotation.param.display.Example} are present.
   * @since 4.5
   */
  public static java.util.Optional<String> getExampleValue(Field field) {
    String exampleValue = getAnnotationValueFromField(
                                                      field,
                                                      Example.class,
                                                      org.mule.sdk.api.annotation.param.display.Example.class,
                                                      Example::value, org.mule.sdk.api.annotation.param.display.Example::value);

    return ofNullable(exampleValue);
  }

  /**
   * Returns the {@link PathModel} of the given {@link Field} if either annotation {@link Path} or {@link FilePath} are present
   *
   * @param field the {@link Field} to be introspected
   * @return {@link Optional} containing the {@link PathModel} if either {@link Path} or {@link FilePath} are present.
   *
   * @since 4.5
   */
  public static java.util.Optional<PathModel> getPathModel(Field field) {
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

  /**
   * Returns the {@link ClassValueModel} of the given {@link Field} if either annotation {@link ClassValue} or
   * {@link org.mule.sdk.api.annotation.param.display.ClassValue} are present
   *
   * @param field the {@link Field} to be introspected
   * @return {@link Optional} containing the {@link ClassValueModel} if either {@link ClassValue} or
   *         {@link org.mule.sdk.api.annotation.param.display.ClassValue} are present.
   *
   * @since 4.5
   */
  public static java.util.Optional<ClassValueModel> getClassValueModel(Field field) {
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

  /**
   * Returns the order and tab of the given {@link Field} if either annotation {@link Placement} or
   * {@link org.mule.sdk.api.annotation.param.display.Placement} are present
   *
   * @param field the {@link Field} to be introspected
   * @return {@link Optional} containing a {@code java.util.Optional<Pair<Integer, String>>} with the order and tab placement if
   *         either {@link Placement} or {@link org.mule.sdk.api.annotation.param.display.Placement} are present.
   *
   * @since 4.5
   */
  public static java.util.Optional<Pair<Integer, String>> getPlacementValue(Field field) {
    Pair<Integer, String> value = getAnnotationValueFromField(field,
                                                              Placement.class,
                                                              org.mule.sdk.api.annotation.param.display.Placement.class,
                                                              (Placement placement) -> new Pair(placement.order(),
                                                                                                placement.tab()),
                                                              (org.mule.sdk.api.annotation.param.display.Placement placement) -> new Pair(placement
                                                                  .order(), placement.tab()));

    return ofNullable(value);
  }

  /**
   * Checks if the given {@link Field} is annotated with either {@link Text} or
   * {@link org.mule.sdk.api.annotation.param.display.Text}
   *
   * @param field the {@link Field} to be introspected
   * @return true if the given {@link Field} is annotated with either {@link Text} or
   *         {@link org.mule.sdk.api.annotation.param.display.Text}
   *
   * @since 4.5
   */
  public static boolean isTextField(Field field) {
    return isAnnotationPresentOnField(field, Text.class, org.mule.sdk.api.annotation.param.display.Text.class);
  }

  /**
   * Checks if the given {@link Field} is annotated with {@link Password}
   *
   * @param field the {@link Field} to be introspected
   * @return true if the given {@link Field} is annotated {@link Password}
   *
   * @since 4.5
   */
  public static boolean isPasswordField(Field field) {
    return isAnnotationPresentOnField(field, Password.class);
  }

  /**
   * Checks if the given {@link Field} is annotated with {@link Query}
   *
   * @param field the {@link Field} to be introspected
   * @return true if the given {@link Field} is annotated with {@link Query}
   *
   * @since 4.5
   */
  public static boolean isQueryField(Field field) {
    return isAnnotationPresentOnField(field, Query.class);
  }

  public static <R extends Annotation> boolean isAnnotationPresentOnField(Field field, Class<R> annotationClass) {
    return field.getAnnotation(annotationClass) != null;
  }

  public static <R extends Annotation, S extends Annotation> boolean isAnnotationPresentOnField(Field field,
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
