/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mule.metadata.api.model.MetadataFormat.CSV;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.metadata.api.model.MetadataFormat.JSON;
import static org.mule.metadata.api.model.MetadataFormat.XML;
import static org.mule.metadata.api.utils.MetadataTypeUtils.getLocalPart;
import static org.mule.metadata.api.utils.MetadataTypeUtils.getTypeId;
import static org.mule.metadata.api.utils.MetadataTypeUtils.isCollection;
import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.annotation.TypeIdAnnotation;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataFormat;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.visitor.BasicTypeMetadataVisitor;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.metadata.java.api.utils.JavaTypeUtils;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.extension.api.declaration.type.annotation.DslBaseType;
import org.mule.runtime.extension.api.declaration.type.annotation.ExpressionSupportAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.FlattenedTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.InfrastructureTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.LayoutTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterDslAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.SubstitutionGroup;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeDslAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.TypedValueTypeAnnotation;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Set of utility operations to handle {@link MetadataType}
 *
 * @since 1.0
 */
public final class ExtensionMetadataTypeUtils {

  private static final List<MetadataFormat> KNOWN_METADATA_FORMATS = asList(JAVA, XML, JSON, CSV);

  private ExtensionMetadataTypeUtils() {}

  public static Optional<String> getId(MetadataType metadataType) {
    return JavaTypeUtils.getId(metadataType);
  }

  /**
   * @param metadataType the {@link ObjectType} who's {@link Class type} is required
   * @return the {@link Class type} of the given {@link ObjectType} if one exists in the current classloader,
   *         {@link Optional#empty()} otherwise.
   */
  public static <T> Optional<Class<T>> getType(MetadataType metadataType) {
    return getType(metadataType, Thread.currentThread().getContextClassLoader());
  }

  /**
   * @param metadataType the {@link ObjectType} who's {@link Class type} is required
   * @param classloader the {@link ClassLoader} to use when looking for the {@link Class}
   * @return the {@link Class type} of the given {@link ObjectType} if one exists in the current classloader,
   *         {@link Optional#empty()} otherwise.
   */
  public static <T> Optional<Class<T>> getType(MetadataType metadataType, ClassLoader classloader) {
    try {
      return Optional.of(JavaTypeUtils.getType(metadataType, classloader));
    } catch (Throwable e) {
      return empty();
    }
  }

  /**
   * @param fieldType the {@link ObjectFieldType} to inspect to retrieve its type Alias
   * @return the {@code Alias} name of the {@link ObjectFieldType}
   */
  public static String getAlias(ObjectFieldType fieldType) {
    return fieldType.getAnnotation(TypeAliasAnnotation.class).map(TypeAliasAnnotation::getValue)
        .orElseGet(() -> getLocalPart(fieldType));
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
   * @param defaultName default name to use if {@code metadataType} alias is not defined
   * @return the {@code Alias} name of the {@link MetadataType} or the {@code defaultName} if alias was not specified
   */
  public static String getAlias(MetadataType metadataType, String defaultName) {
    return metadataType.getAnnotation(TypeAliasAnnotation.class).map(TypeAliasAnnotation::getValue)
        .orElse(defaultName);
  }

  public static boolean isFinal(MetadataType metadataType) {
    return metadataType.getAnnotation(ClassInformationAnnotation.class)
        .map(ClassInformationAnnotation::isFinal)
        .orElse(false);
  }

  /**
   * @param metadataType the {@link MetadataType} to inspect
   * @return whether the {@code metadataType} represents a {@link Map} or not
   */
  public static boolean isMap(MetadataType metadataType) {
    return metadataType.getAnnotation(ClassInformationAnnotation.class)
        .map(ClassInformationAnnotation::isMap)
        .orElseGet(() -> metadataType.getAnnotation(TypeIdAnnotation.class)
            .map(TypeIdAnnotation::getValue)
            .map(id -> id.equals(Map.class.getName())).orElse(false));
  }

  public static boolean isMapOfStrings(MetadataType metadataType) {
    return metadataType.getAnnotation(ClassInformationAnnotation.class)
        .map(info -> {
          if (info.isMap() && info.getGenericTypes().size() == 2) {
            String string = String.class.getName();
            return string.equals(info.getGenericTypes().get(0)) && string.equals(info.getGenericTypes().get(1));
          }
          return false;
        }).orElse(false);
  }

  /**
   * @return {@code true} if the type is marked as a {@link FlattenedTypeAnnotation FlattenedType}
   */
  public static boolean isFlattenedParameterGroup(MetadataType type) {
    return type.getAnnotation(FlattenedTypeAnnotation.class).isPresent();
  }

  /**
   * @return {@code true} if the given {@link MetadataType} should support inline definition as child element
   */
  public static boolean allowsInlineDefinition(MetadataType type) {
    Reference<Boolean> supported = new Reference<>(true);
    type.accept(new MetadataTypeVisitor() {

      @Override
      protected void defaultVisit(MetadataType metadataType) {
        metadataType.getAnnotation(ParameterDslAnnotation.class)
            .map(ParameterDslAnnotation::allowsInlineDefinition)
            .ifPresent(supported::set);
      }

      @Override
      public void visitArrayType(ArrayType arrayType) {
        arrayType.getType().accept(this);
      }

      @Override
      public void visitObject(ObjectType objectType) {

        // Check for parameter-level override
        Optional<ParameterDslAnnotation> paramDsl = objectType.getAnnotation(ParameterDslAnnotation.class);
        Optional<TypeDslAnnotation> typeDsl = objectType.getAnnotation(TypeDslAnnotation.class);

        if (typeDsl.isPresent() && paramDsl.isPresent()) {
          supported.set(typeDsl.get().allowsInlineDefinition() && paramDsl.get().allowsInlineDefinition());
        } else if (typeDsl.isPresent()) {
          supported.set(typeDsl.get().allowsInlineDefinition());
        } else {
          paramDsl.map(ParameterDslAnnotation::allowsInlineDefinition)
              .ifPresent(supported::set);
        }
      }
    });

    return supported.get();
  }

  /**
   * @return whether the given {@link MetadataType} should support being defined as a top level element
   */
  public static boolean allowsTopLevelDefinition(MetadataType type) {
    return type.getAnnotation(TypeDslAnnotation.class)
        .map(TypeDslAnnotation::allowsTopLevelDefinition).orElse(false);
  }

  /**
   * @return whether the given {@link MetadataType} should support registry references
   */
  public static boolean allowsReferences(MetadataType type) {
    return type.getAnnotation(ParameterDslAnnotation.class)
        .map(ParameterDslAnnotation::allowsReferences).orElse(true);
  }

  /**
   * Checks the given {@code metadataType} for the {@link ExpressionSupportAnnotation}.
   * <p>
   * If present, the {@link ExpressionSupportAnnotation#getExpressionSupport()} value is returned. Otherwise, it defaults to
   * {@link ExpressionSupport#SUPPORTED}
   *
   * @param metadataType a {@link MetadataType}
   * @return a {@link ExpressionSupport}
   */
  public static ExpressionSupport getExpressionSupport(MetadataType metadataType) {
    return metadataType.getAnnotation(ExpressionSupportAnnotation.class)
        .map(ExpressionSupportAnnotation::getExpressionSupport)
        .orElse(ExpressionSupport.SUPPORTED);
  }

  /**
   * @param metadataType a type model
   * @return a {@link LayoutModel} if the {@code metadataType} contains layout information
   */
  public static Optional<LayoutModel> getLayoutModel(MetadataType metadataType) {
    if (metadataType.getAnnotation(LayoutTypeAnnotation.class).isPresent()) {
      LayoutTypeAnnotation layoutTypeAnnotation = metadataType.getAnnotation(LayoutTypeAnnotation.class).get();
      return of(layoutTypeAnnotation.getLayoutModel());
    }

    return empty();
  }

  /**
   * Returns true if the type is an infrastructure type, false otherwise.
   *
   * @param type the type to check
   * @return whether is an infrastructure type or not.
   */
  public static boolean isInfrastructure(MetadataType type) {
    return type.getAnnotation(InfrastructureTypeAnnotation.class).isPresent();
  }

  /**
   * @param metadataType a type model
   * @return whether instances of the given {@code metadataType} accept being referenced to
   */
  public static boolean acceptsReferences(MetadataType metadataType) {
    return metadataType.getAnnotation(ParameterDslAnnotation.class)
        .map(ParameterDslAnnotation::allowsReferences)
        .orElse(true);
  }

  public static boolean isBasic(MetadataType metadataType) {
    Reference<Boolean> basic = new Reference<>(false);
    metadataType.accept(new BasicTypeMetadataVisitor() {

      @Override
      protected void visitBasicType(MetadataType metadataType) {
        basic.set(true);
      }
    });

    return basic.get();
  }

  /**
   * @param metadataType
   * @return the substitutionGroup defined by the user or {@code Optional.empty()} if not present.
   */
  public static Optional<SubstitutionGroup> getSubstitutionGroup(MetadataType metadataType) {
    return metadataType.getAnnotation(TypeDslAnnotation.class).flatMap(TypeDslAnnotation::getSubstitutionGroup);
  }

  /**
   * @param metadataType
   * @return the baseType defined by the user or {Optional.empty()} if not present
   */
  public static Optional<DslBaseType> getBaseType(MetadataType metadataType) {
    return metadataType.getAnnotation(TypeDslAnnotation.class).flatMap(TypeDslAnnotation::getDslBaseType);
  }

  /**
   * Returns a {@link MetadataFormat} which represents the given {@code mediaType}.
   * <p>
   * If the {@code mediaType} matches any of the well known formats, then it will return one of those. Otherwise, a new
   * {@link MetadataFormat} will be created and returned
   *
   * @param mediaType a {@link MediaType}
   * @return a {@link MetadataFormat}
   */
  public static MetadataFormat toMetadataFormat(MediaType mediaType) {
    String rfc = mediaType.toRfcString();
    return KNOWN_METADATA_FORMATS.stream()
        .filter(f -> f.getValidMimeTypes().stream().anyMatch(rfc::matches))
        .findFirst()
        .orElseGet(() -> new MetadataFormat(rfc, rfc, rfc));
  }

  /**
   * Returns a {@link Boolean} indicating whether the given {@link MetadataType} is a Java Collection or not.
   *
   * @param metadataType {@link MetadataType} to introspect
   * @return a {@link boolean}
   */
  public static boolean isJavaCollection(MetadataType metadataType) {
    if (isCollection(metadataType)) {
      Optional<Class<Object>> type = getType(metadataType);
      if (type.isPresent()) {
        return Collection.class.isAssignableFrom(type.get());
      }
    }
    return false;
  }

  /**
   * @return {@code true} if the given {@link MetadataType} is representing the generic of a {@link TypedValue}
   */
  public static boolean isTypedValue(MetadataType metadataType) {
    return metadataType.getAnnotation(TypedValueTypeAnnotation.class).isPresent();
  }

  /**
   * @return {@code true} if the given type can receive the result of a registry reference
   */
  public static boolean isReferableType(MetadataType type) {
    try {
      Class<Object> clazz = JavaTypeUtils.getType(type);
      return Object.class.equals(clazz) || Serializable.class.equals(clazz);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Check whether the given {@link MetadataType}s can be considered equal.
   *
   * @since 1.4.0
   */
  public static boolean areTypesEqual(MetadataType type, MetadataType otherType, ClassLoader classLoader) {
    String typeClassName = getClassInformationName(type);
    String otherTypeClassName = getClassInformationName(otherType);

    if (typeClassName != null && otherTypeClassName != null) {
      if (classLoader != null) {
        if (!areAssignable(typeClassName, otherTypeClassName, classLoader)) {
          return false;
        }
      } else {
        if (!Objects.equals(typeClassName, otherTypeClassName)) {
          return false;
        }
      }
    }

    String typeId = getTypeId(type).orElse(null);
    String otherTypeId = getTypeId(otherType).orElse(null);

    if (typeId != null && otherTypeId != null) {
      return Objects.equals(typeId, otherTypeId);
    } else {
      return Objects.equals(type, otherType);
    }
  }

  private static boolean areAssignable(String className, String otherClassName, ClassLoader classLoader) {
    try {
      Class clazz = classLoader.loadClass(className);
      Class otherClazz = classLoader.loadClass(otherClassName);

      return clazz.isAssignableFrom(otherClazz) || otherClazz.isAssignableFrom(clazz);
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  private static String getClassInformationName(MetadataType type) {
    return type.getAnnotation(ClassInformationAnnotation.class)
        .map(annotation -> annotation.getClassname()).orElse(null);
  }
}
