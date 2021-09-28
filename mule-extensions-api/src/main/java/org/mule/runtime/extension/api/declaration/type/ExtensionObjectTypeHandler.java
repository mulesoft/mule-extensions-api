/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mule.runtime.api.util.Preconditions.checkArgument;
import static org.mule.runtime.extension.internal.loader.util.JavaParserUtils.getAlias;
import static org.mule.runtime.extension.api.declaration.type.annotation.StereotypeTypeAnnotation.fromDefinitions;
import static org.mule.runtime.extension.internal.semantic.TypeSemanticTermsUtils.enrichWithTypeAnnotation;

import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.api.builder.WithAnnotation;
import org.mule.metadata.java.api.handler.ObjectFieldHandler;
import org.mule.metadata.java.api.handler.ObjectHandler;
import org.mule.metadata.java.api.handler.TypeHandlerManager;
import org.mule.metadata.java.api.utils.ParsingContext;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.extension.api.annotation.Extensible;
import org.mule.runtime.extension.api.annotation.dsl.xml.TypeDsl;
import org.mule.runtime.extension.api.annotation.param.stereotype.Stereotype;
import org.mule.runtime.extension.api.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.LiteralTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterResolverTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeDslAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.TypedValueTypeAnnotation;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.stereotype.ImplicitStereotypeDefinition;
import org.mule.runtime.extension.api.stereotype.StereotypeDefinition;
import org.mule.sdk.api.runtime.parameter.Literal;
import org.mule.sdk.api.runtime.parameter.ParameterResolver;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.collect.ImmutableMap;

/**
 * An implementation of {@link ObjectHandler} which allows the type to me enriched with custom type annotations of the Extensions
 * API.
 *
 * @since 1.0
 */
public class ExtensionObjectTypeHandler extends ObjectHandler {

  private final ParameterResolverTypeAnnotation parameterResolverTypeAnnotation = new ParameterResolverTypeAnnotation();
  private final LiteralTypeAnnotation literalTypeAnnotation = new LiteralTypeAnnotation();
  private final TypedValueTypeAnnotation typedValueTypeAnnotation = new TypedValueTypeAnnotation();

  private final Map<Class<?>, ParsingContext> wrappedTypesContexts;

  public ExtensionObjectTypeHandler(ObjectFieldHandler fieldHandler) {
    super(fieldHandler);
    wrappedTypesContexts = ImmutableMap.<Class<?>, ParsingContext>builder()
        .put(ParameterResolver.class, new ParsingContext())
        .put(TypedValue.class, new ParsingContext())
        .put(Literal.class, new ParsingContext())
        .build();
  }

  @Override
  public TypeBuilder<?> handleClass(Class<?> clazz, List<Type> genericTypes, TypeHandlerManager typeHandlerManager,
                                    ParsingContext context, BaseTypeBuilder baseTypeBuilder) {
    TypeBuilder typeBuilder = baseTypeBuilder;
    Class<?> currentClass = clazz;

    if (ParameterResolver.class.isAssignableFrom(clazz)) {
      handleGenericType(clazz, genericTypes, typeHandlerManager, wrappedTypesContexts.get(ParameterResolver.class),
                        baseTypeBuilder, parameterResolverTypeAnnotation);
      currentClass = getGenericClass(genericTypes, 0);
    } else if (TypedValue.class.isAssignableFrom(clazz)) {
      handleGenericType(clazz, genericTypes, typeHandlerManager, wrappedTypesContexts.get(TypedValue.class),
                        baseTypeBuilder, typedValueTypeAnnotation);
      currentClass = getGenericClass(genericTypes, 0);
    } else if (Literal.class.isAssignableFrom(clazz)) {
      handleGenericType(clazz, genericTypes, typeHandlerManager, wrappedTypesContexts.get(Literal.class),
                        baseTypeBuilder, literalTypeAnnotation);
      currentClass = getGenericClass(genericTypes, 0);
    } else {
      typeBuilder = super.handleClass(currentClass, genericTypes, typeHandlerManager, context, baseTypeBuilder);
    }

    if (typeBuilder != null && typeBuilder instanceof WithAnnotation) {
      final WithAnnotation annotatedBuilder = (WithAnnotation) typeBuilder;
      if (currentClass.isAnnotationPresent(Extensible.class) ||
          currentClass.isAnnotationPresent(org.mule.sdk.api.annotation.Extensible.class)) {
        annotatedBuilder.with(new ExtensibleTypeAnnotation());
      }

      Optional<TypeDslAnnotation> typeDslAnnotation = getTypeDslAnnotation(currentClass);
      typeDslAnnotation.ifPresent((annotatedBuilder::with));

      annotatedBuilder.with(new TypeAliasAnnotation(getAlias(currentClass)));

      Stereotype stereotype = currentClass.getAnnotation(Stereotype.class);
      boolean allowTopLevelDefinition = typeDslAnnotation.map(TypeDslAnnotation::allowsTopLevelDefinition).orElse(false);
      handleStereotype(currentClass, annotatedBuilder, allowTopLevelDefinition, stereotype);

      enrichWithTypeAnnotation(currentClass, annotatedBuilder);
    }

    return typeBuilder;
  }

  private void handleStereotype(Class<?> currentClass, final WithAnnotation annotatedBuilder, boolean allowTopLevelDefinition,
                                Stereotype stereotype) {
    if (stereotype != null) {
      annotatedBuilder.with(fromDefinitions(singletonList(stereotype.value())));
    } else {
      // We need to generate implicit stereotypes for top level elements and their interfaces. Thing is, we don't know if an
      // interface is implemented only by top level elements when processing that interface, so to be safe, an implicit stereotype
      // is defined for all interfaces.
      if (isInterfaceFromThisExtension(currentClass) || allowTopLevelDefinition) {
        annotatedBuilder.with(fromDefinitions(singletonList(ImplicitStereotypeDefinition.class)));
      } else {
        calculateInheritedStereotype(currentClass).ifPresent(inh -> annotatedBuilder.with(fromDefinitions(singletonList(inh))));
      }
    }
  }

  /**
   * Now, the interfaces found for an extension may be not from the extension, it may be something from the JDK (i.e.:
   * Serializable) or something from the Mule runtime (i.e.: org.mule.runtime.extension.api.runtime.route.Chain). To avoid
   * generating stereotypes that have no chance of being needed, those cases are filtered out.
   */
  private boolean isInterfaceFromThisExtension(Class<?> currentClass) {
    return !currentClass.getName().startsWith("java.")
        && !currentClass.getName().startsWith("javax.")
        && !currentClass.getName().startsWith("org.mule.runtime.")
        && !currentClass.getName().startsWith("com.mulesoft.mule.runtime.")
        && currentClass.isInterface();
  }

  private Optional<Class<? extends StereotypeDefinition>> calculateInheritedStereotype(Class<?> currentClass) {
    if (currentClass == null) {
      return empty();
    }

    AtomicReference<Class<? extends StereotypeDefinition>> inheritedStereotype = new AtomicReference<>();
    Class<?> cls = currentClass;

    for (Class<?> iface : cls.getInterfaces()) {
      calculateInheritedStereotype(iface).ifPresent(inheritedStereotype::set);
    }

    while (cls != null && cls.getSuperclass() != Object.class) {
      calculateInheritedStereotype(cls.getSuperclass()).ifPresent(inheritedStereotype::set);
      cls = cls.getSuperclass();
    }

    if (inheritedStereotype.get() != null) {
      return of(inheritedStereotype.get());
    } else {
      Stereotype stereotype = currentClass.getAnnotation(Stereotype.class);
      if (stereotype != null) {
        return of(ImplicitStereotypeDefinition.class);
      } else {
        return Optional.empty();
      }
    }
  }

  private Class<?> getGenericClass(List<Type> genericTypes, int position) {
    Type type = genericTypes.get(position);
    return type instanceof Class ? (Class<?>) type : Object.class;
  }

  private void handleGenericType(Class<?> clazz, List<Type> genericTypes, TypeHandlerManager typeHandlerManager,
                                 ParsingContext context,
                                 BaseTypeBuilder typeBuilder, TypeAnnotation annotation) {
    checkArgument(!genericTypes.isEmpty(), format("Type %s doesn't have the required generic type", clazz));
    TypeBuilder handle = typeHandlerManager.handle(genericTypes.get(0), context, typeBuilder);
    if (handle instanceof WithAnnotation) {
      ((WithAnnotation) handle).with(annotation);
    }
  }

  private Optional<TypeDslAnnotation> getTypeDslAnnotation(Class<?> currentClass) {
    TypeDsl legacyTypeDslAnnotation = currentClass.getAnnotation(TypeDsl.class);
    org.mule.sdk.api.annotation.dsl.xml.TypeDsl sdkTypeDslAnnotation =
        currentClass.getAnnotation(org.mule.sdk.api.annotation.dsl.xml.TypeDsl.class);

    Optional<TypeDslAnnotation> typeDslAnnotation = empty();

    if (legacyTypeDslAnnotation != null && sdkTypeDslAnnotation != null) {
      throw new IllegalModelDefinitionException(format("Class '%s' is annotated with '@%s' and '@%s' at the same time",
                                                       currentClass.getName(), TypeDsl.class.getName(),
                                                       org.mule.sdk.api.annotation.dsl.xml.TypeDsl.class.getName()));
    } else if (legacyTypeDslAnnotation != null) {
      typeDslAnnotation = of(new TypeDslAnnotation(legacyTypeDslAnnotation.allowInlineDefinition(),
                                                   legacyTypeDslAnnotation.allowTopLevelDefinition(),
                                                   legacyTypeDslAnnotation.substitutionGroup(),
                                                   legacyTypeDslAnnotation.baseType()));
    } else if (sdkTypeDslAnnotation != null) {
      typeDslAnnotation = of(new TypeDslAnnotation(sdkTypeDslAnnotation.allowInlineDefinition(),
                                                   sdkTypeDslAnnotation.allowTopLevelDefinition(),
                                                   sdkTypeDslAnnotation.substitutionGroup(),
                                                   sdkTypeDslAnnotation.baseType()));
    }

    return typeDslAnnotation;
  }
}
