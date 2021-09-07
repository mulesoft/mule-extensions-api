/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static java.util.stream.Collectors.toList;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Ignore;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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
   * @param field
   * @return
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
   * @param field
   * @return
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

}
