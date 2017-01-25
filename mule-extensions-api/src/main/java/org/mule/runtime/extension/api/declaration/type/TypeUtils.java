/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.extension.api.declaration.type.ReconnectionStrategyTypeBuilder.RECONNECTION_STRATEGY;
import static org.mule.runtime.extension.api.declaration.type.ReconnectionStrategyTypeBuilder.RECONNECT_ALIAS;
import static org.mule.runtime.extension.api.declaration.type.ReconnectionStrategyTypeBuilder.RECONNECT_FOREVER_ALIAS;
import static org.mule.runtime.extension.api.declaration.type.RedeliveryPolicyTypeBuilder.REDELIVERY_POLICY;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Ignore;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.declaration.type.annotation.ExpressionSupportAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.LayoutTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterRoleAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeAliasAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.api.util.ExtensionModelUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utility class to handle Java types and their relationship with the {@link MetadataType} model
 *
 * @since 1.0
 */
public final class TypeUtils {

  private TypeUtils() {}

  private static List<String> INFRASTRUCTURE_ALIASES =
      asList(REDELIVERY_POLICY, RECONNECTION_STRATEGY, RECONNECT_FOREVER_ALIAS, RECONNECT_ALIAS);

  /**
   * Returns all the {@link Field}s in the given {@code declaringType} which are annotated
   * with {@link Parameter} but also do not have the {@link Ignore} one.
   * <p>
   * The introspection also includes parent classes.
   *
   * @param declaringType the class to introspect.
   * @return a {@link Collection} of {@link Field fields}. May be empty but will never be {@code null}
   */
  public static Collection<Field> getParameterFields(Class<?> declaringType) {
    return getAllFields(declaringType).stream()
        .filter(field -> (field.getAnnotation(Parameter.class) != null || field.getAnnotation(ParameterGroup.class) != null)
            && field.getAnnotation(Ignore.class) == null)
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
   * Returns all the superclasses of the given {@code type}, without including
   * {@link Object}
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
   * Checks the given {@code field} for the {@link Alias} annotation. If present, {@link Alias#value()}
   * is returned. Otherwise, {@link Field#getName()} is returned.
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
   * Checks the given {@code metadataType} for the {@link ExpressionSupportAnnotation}.
   * <p>
   * If present, the {@link ExpressionSupportAnnotation#getExpressionSupport()}
   * value is returned. Otherwise, it defaults to {@link ExpressionSupport#SUPPORTED}
   *
   * @param metadataType a {@link MetadataType}
   * @return a {@link ExpressionSupport}
   */
  public static ExpressionSupport getExpressionSupport(MetadataType metadataType) {
    return metadataType.getAnnotation(ExpressionSupportAnnotation.class)
        .map(ExpressionSupportAnnotation::getExpressionSupport)
        .orElse(ExpressionSupport.SUPPORTED);
  }

  public static ParameterRole getParameterRole(MetadataType metadataType) {
    return metadataType.getAnnotation(ParameterRoleAnnotation.class)
        .map(ParameterRoleAnnotation::getRole)
        .orElse(BEHAVIOUR);
  }

  public static boolean isContent(MetadataType type) {
    return ExtensionModelUtils.isContent(getParameterRole(type));
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

  public static boolean isInfrastructure(MetadataType type) {
    return type.getAnnotation(TypeAliasAnnotation.class)
        .map(typeAliasAnnotation -> INFRASTRUCTURE_ALIASES.contains(typeAliasAnnotation.getValue()))
        .orElse(false);
  }

  /**
   * @param metadataType a type model
   * @return whether instances of the given {@code metadataType} accept being referenced to
   */
  public static boolean acceptsReferences(MetadataType metadataType) {
    return metadataType.getAnnotation(XmlHintsAnnotation.class)
        .map(XmlHintsAnnotation::allowsReferences)
        .orElse(true);
  }
}
