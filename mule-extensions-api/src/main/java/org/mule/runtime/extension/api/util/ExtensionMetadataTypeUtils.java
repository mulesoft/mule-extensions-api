/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import static java.util.Optional.empty;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.metadata.api.utils.MetadataTypeUtils.getLocalPart;
import static org.mule.runtime.extension.api.util.NameUtils.getAliasName;
import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.java.api.utils.JavaTypeUtils;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.declaration.type.annotation.FlattenedTypeAnnotation;

import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Optional;

/**
 * Set of utility operations to handle {@link MetadataType}
 *
 * @since 1.0
 */
public final class ExtensionMetadataTypeUtils {

  private ExtensionMetadataTypeUtils() {}

  public static Optional<String> getId(MetadataType metadataType) {
    return JavaTypeUtils.getId(metadataType);
  }

  /**
   * @param metadataType the {@link ObjectType} who's {@link Class type} is required
   * @return the {@link Class type} of the given {@link ObjectType} if one exists in the current classloader,
   * {@link Optional#empty()} otherwise.
   */
  public static <T> Optional<Class<T>> getType(MetadataType metadataType) {
    return getType(metadataType, Thread.currentThread().getContextClassLoader());
  }

  /**
   * @param metadataType the {@link ObjectType} who's {@link Class type} is required
   * @param classloader the {@link ClassLoader} to use when looking for the {@link Class}
   * @return the {@link Class type} of the given {@link ObjectType} if one exists in the current classloader,
   * {@link Optional#empty()} otherwise.
   */
  public static <T> Optional<Class<T>> getType(MetadataType metadataType, ClassLoader classloader) {
    if (!metadataType.getMetadataFormat().equals(JAVA)) {
      return empty();
    }

    try {
      return Optional.of(JavaTypeUtils.getType(metadataType, classloader));
    } catch (Exception e) {
      return empty();
    }
  }

  /**
   * @param fieldType the {@link ObjectFieldType} to inspect to retrieve its type Alias
   * @return the {@code Alias} name of the {@link ObjectFieldType}
   */
  public static String getAlias(ObjectFieldType fieldType) {
    return fieldType.getAnnotation(TypeAliasAnnotation.class).map(TypeAliasAnnotation::getValue)
        .orElse(getLocalPart(fieldType));
  }

  /**
   * @param metadataType the {@link MetadataType} to inspect to retrieve its type Alias
   * @return the {@code Alias} name of the {@link MetadataType}
   */
  public static String getAlias(MetadataType metadataType) {
    return getAlias(metadataType, "");
  }

  /**
   * @param metadataType the {@link MetadataType} to inspect to retrieve its type Alias
   * @param defaultName  default name to use if {@code metadataType} alias is not defined
   * @return the {@code Alias} name of the {@link MetadataType} or the {@code defaultName} if alias was not specified
   */
  public static String getAlias(MetadataType metadataType, String defaultName) {
    return metadataType.getAnnotation(TypeAliasAnnotation.class).map(TypeAliasAnnotation::getValue)
        .orElseGet(() -> getType(metadataType)
            .map(type -> getAliasName(defaultName, type.getAnnotation(Alias.class)))
            .orElse(defaultName));
  }

  public static boolean isFinal(MetadataType metadataType) {
    return getType(metadataType).map(type -> Modifier.isFinal(type.getModifiers())).orElse(false);
  }

  /**
   * @param metadataType the {@link MetadataType} to inspect
   * @return whether the {@code metadataType} represents a {@link Map} or not
   */
  public static boolean isMap(MetadataType metadataType) {
    return isAssignableFrom(metadataType, Map.class);
  }

  /**
   * @param type a {@link MetadataType}
   * @return whether the given {@code type} represents an {@link InputStream} or not
   */
  public static boolean isInputStream(MetadataType type) {
    return isAssignableFrom(type, InputStream.class);
  }

  /**
   * @return {@code true} if the type is marked as a {@link FlattenedTypeAnnotation FlattenedType}
   */
  public static boolean isFlattenedParameterGroup(MetadataType type) {
    return type.getAnnotation(FlattenedTypeAnnotation.class).isPresent();
  }

  private static boolean isAssignableFrom(MetadataType metadataType, Class<?> type) {
    return getType(metadataType).map(clazz -> type.isAssignableFrom(clazz)).orElse(false);
  }
}
