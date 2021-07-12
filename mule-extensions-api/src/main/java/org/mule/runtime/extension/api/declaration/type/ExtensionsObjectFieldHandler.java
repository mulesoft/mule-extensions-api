/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.getBoolean;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.meta.model.stereotype.StereotypeModelBuilder.newStereotype;
import static org.mule.runtime.api.util.MuleSystemProperties.MULE_FLOW_REFERERENCE_FIELDS_MATCH_ANY;
import static org.mule.runtime.extension.api.annotation.param.Optional.PAYLOAD;
import static org.mule.runtime.extension.api.declaration.type.TypeUtils.getAlias;
import static org.mule.runtime.extension.api.declaration.type.TypeUtils.getAllFields;
import static org.mule.runtime.extension.api.declaration.type.TypeUtils.getParameterFields;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.CONFIG;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.FLOW;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.OBJECT_STORE;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.SUB_FLOW;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.getDefaultValue;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.toClassValueModel;
import static org.mule.runtime.extension.internal.semantic.TypeSemanticTermsUtils.enrichWithTypeAnnotation;

import org.mule.metadata.api.annotation.DefaultValueAnnotation;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectFieldTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.java.api.handler.DefaultObjectFieldHandler;
import org.mule.metadata.java.api.handler.ObjectFieldHandler;
import org.mule.metadata.java.api.handler.TypeHandlerManager;
import org.mule.metadata.java.api.utils.ParsingContext;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.display.PathModel;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.ConfigReferences;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.dsl.xml.ParameterDsl;
import org.mule.runtime.extension.api.annotation.param.ConfigOverride;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.ExclusiveOptionals;
import org.mule.runtime.extension.api.annotation.param.NullSafe;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.Query;
import org.mule.runtime.extension.api.annotation.param.RefName;
import org.mule.runtime.extension.api.annotation.param.display.ClassValue;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Path;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.param.display.Text;
import org.mule.runtime.extension.api.annotation.param.reference.ConfigReference;
import org.mule.runtime.extension.api.annotation.param.reference.FlowReference;
import org.mule.runtime.extension.api.annotation.param.reference.ObjectStoreReference;
import org.mule.runtime.extension.api.declaration.type.annotation.ConfigOverrideTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.DefaultImplementingTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.DisplayTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ExclusiveOptionalsTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ExpressionSupportAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.FlattenedTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.LayoutTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.NullSafeTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterDslAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.StereotypeTypeAnnotation;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.exception.IllegalParameterModelDefinitionException;
import org.mule.runtime.extension.api.runtime.route.Chain;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An implementation of {@link ObjectFieldHandler} which navigates an object's {@link Field}s by looking for, and following the
 * rules, of the Extensions API annotations.
 * <p>
 * If the introspected class does not contain such annotations, then {@link Introspector#getBeanInfo(Class)} is used to obtain the
 * class properties. Those properties will be managed as optional parameters, without a default value
 *
 * @since 1.0
 */
final class ExtensionsObjectFieldHandler implements ObjectFieldHandler {

  @Override
  public void handleFields(Class<?> clazz, TypeHandlerManager typeHandlerManager, ParsingContext context,
                           ObjectTypeBuilder builder) {

    if (clazz.getName().equals(Chain.class.getName())) {
      return;
    }

    Collection<Field> fields = getParameterFields(clazz);
    if (fields.isEmpty()) {
      validateIllegalAnnotationUseOnNonParameterFields(clazz);
      fallbackToBeanProperties(clazz, typeHandlerManager, context, builder);
      return;
    }

    for (Field field : fields) {
      final ObjectFieldTypeBuilder fieldBuilder = builder.addField();
      fieldBuilder.key(getAlias(field));

      setOptionalAndDefault(field, fieldBuilder);
      processParameterGroup(field, fieldBuilder);
      processExpressionSupport(field, fieldBuilder);
      processNullSafe(clazz, field, fieldBuilder, typeHandlerManager, context);
      processElementStyle(field, fieldBuilder);
      processLayoutAnnotation(field, fieldBuilder);
      processDisplayAnnotation(field, fieldBuilder);
      processConfigOverride(field, fieldBuilder);
      processElementReference(field, fieldBuilder);
      processSemanticTerms(field, fieldBuilder);
      setFieldType(typeHandlerManager, context, field, fieldBuilder);
    }
  }

  private void validateIllegalAnnotationUseOnNonParameterFields(Class<?> clazz) {
    final String annotationsPackageName = Parameter.class.getPackage().getName();
    List<String> illegalFieldNames = getAllFields(clazz).stream()
        .filter(field -> Stream.of(field.getAnnotations())
            .anyMatch(a -> {
              final Class<? extends Annotation> annotationType = a.annotationType();
              return annotationType.getName().contains(annotationsPackageName)
                  && !annotationType.getSimpleName().equals(RefName.class.getSimpleName());
            }))
        .map(Field::getName)
        .collect(toList());

    if (!illegalFieldNames.isEmpty()) {
      throw new IllegalModelDefinitionException(
                                                format("Class '%s' has fields which are not parameters but are annotated with Extensions API annotations."
                                                    + " Illegal fields are " + illegalFieldNames, clazz.getName()));
    }
  }

  private void processParameterGroup(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    if (field.getAnnotation(ParameterGroup.class) != null) {
      fieldBuilder.with(new FlattenedTypeAnnotation());
      processExclusiveOptionals(field, fieldBuilder);
    }
  }

  private void processExclusiveOptionals(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    ExclusiveOptionals exclusiveOptionals = field.getType().getAnnotation(ExclusiveOptionals.class);
    if (exclusiveOptionals != null) {
      Set<String> exclusiveParameters = getParameterFields(field.getType()).stream()
          .filter(f -> f.isAnnotationPresent(org.mule.runtime.extension.api.annotation.param.Optional.class)
              || f.isAnnotationPresent(org.mule.sdk.api.annotation.param.Optional.class))
          .map(f -> f.getAnnotation(Alias.class) != null ? f.getAnnotation(Alias.class).value() : f.getName())
          .collect(Collectors.toCollection(LinkedHashSet::new));
      fieldBuilder.with(new ExclusiveOptionalsTypeAnnotation(exclusiveParameters, exclusiveOptionals.isOneRequired()));
    }
  }

  private void processDisplayAnnotation(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    DisplayModel.DisplayModelBuilder builder = DisplayModel.builder();
    boolean shouldAddTypeAnnotation = false;

    if (field.getAnnotation(DisplayName.class) != null) {
      builder.displayName(field.getAnnotation(DisplayName.class).value());
      shouldAddTypeAnnotation = true;
    }

    if (field.getAnnotation(Summary.class) != null) {
      builder.summary(field.getAnnotation(Summary.class).value());
      shouldAddTypeAnnotation = true;
    }

    if (field.getAnnotation(Example.class) != null) {
      builder.example(field.getAnnotation(Example.class).value());
      shouldAddTypeAnnotation = true;
    }

    Path path = field.getAnnotation(Path.class);
    if (path != null) {
      builder.path(new PathModel(path.type(), path.acceptsUrls(), path.location(), path.acceptedFileExtensions()));
      shouldAddTypeAnnotation = true;
    }

    ClassValue classValue = field.getAnnotation(ClassValue.class);
    if (classValue != null) {
      builder.classValue(toClassValueModel(classValue));
      shouldAddTypeAnnotation = true;
    }

    if (shouldAddTypeAnnotation) {
      fieldBuilder.with(new DisplayTypeAnnotation(builder.build()));
    }
  }

  private void processLayoutAnnotation(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    LayoutModel.LayoutModelBuilder builder = LayoutModel.builder();
    boolean shouldAddTypeAnnotation = false;
    Placement placement = field.getAnnotation(Placement.class);
    if (placement != null) {
      builder.tabName(placement.tab()).order(placement.order());
      shouldAddTypeAnnotation = true;
    }

    if (field.getAnnotation(Password.class) != null) {
      builder.asPassword();
      shouldAddTypeAnnotation = true;
    }

    if (field.getAnnotation(Text.class) != null) {
      builder.asText();
      shouldAddTypeAnnotation = true;
    }

    if (field.getAnnotation(Query.class) != null) {
      builder.asQuery();
      shouldAddTypeAnnotation = true;
    }

    if (shouldAddTypeAnnotation) {
      fieldBuilder.with(new LayoutTypeAnnotation(builder.build()));
    }
  }

  private void setFieldType(TypeHandlerManager typeHandlerManager, ParsingContext context, Field field,
                            ObjectFieldTypeBuilder fieldBuilder) {
    final Type fieldType = field.getGenericType();
    final Optional<TypeBuilder<?>> typeBuilder = context.getTypeBuilder(fieldType);

    if (typeBuilder.isPresent()) {
      fieldBuilder.value(typeBuilder.get());
    } else {
      typeHandlerManager.handle(fieldType, context, fieldBuilder.value());
    }
  }

  private void processExpressionSupport(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    Expression expression = field.getAnnotation(Expression.class);
    fieldBuilder.with(new ExpressionSupportAnnotation(expression != null ? expression.value() : SUPPORTED));
  }

  private void processSemanticTerms(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    enrichWithTypeAnnotation(field, fieldBuilder);
  }

  private void processNullSafe(Class<?> declaringClass, Field field, ObjectFieldTypeBuilder fieldBuilder,
                               TypeHandlerManager typeHandlerManager, ParsingContext context) {
    NullSafe nullSafe = field.getAnnotation(NullSafe.class);
    if (nullSafe == null) {
      return;
    }

    final boolean isOptional = field.isAnnotationPresent(org.mule.runtime.extension.api.annotation.param.Optional.class)
        || field.isAnnotationPresent(org.mule.sdk.api.annotation.param.Optional.class);
    final boolean isParameterGroup = field.isAnnotationPresent(ParameterGroup.class);

    if (!isOptional && !isParameterGroup) {
      throw new IllegalParameterModelDefinitionException(format(
                                                                "Field '%s' in class '%s' is required but annotated with '@%s', which is redundant",
                                                                field.getName(), declaringClass.getName(),
                                                                NullSafe.class.getSimpleName()));
    }

    Class<?> defaultType = nullSafe.defaultImplementingType();
    if (defaultType.equals(Object.class)) {
      fieldBuilder.with(new NullSafeTypeAnnotation(field.getType(), false));
    } else {
      fieldBuilder.with(new NullSafeTypeAnnotation(defaultType, true));

      final Optional<TypeBuilder<?>> typeBuilder = context.getTypeBuilder(defaultType);
      if (typeBuilder.isPresent()) {
        fieldBuilder.with(new DefaultImplementingTypeAnnotation(typeBuilder.get().build()));
      } else {
        BaseTypeBuilder defaultTypeBuilder = BaseTypeBuilder.create(JAVA);
        typeHandlerManager.handle(defaultType, context, defaultTypeBuilder);
        fieldBuilder.with(new DefaultImplementingTypeAnnotation(defaultTypeBuilder.build()));
      }
    }
  }

  private void processConfigOverride(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    ConfigOverride override = field.getAnnotation(ConfigOverride.class);
    if (override != null) {
      fieldBuilder.required(false);
      fieldBuilder.with(new ConfigOverrideTypeAnnotation());
    }
  }

  private void processElementReference(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    ConfigReferences references = field.getAnnotation(ConfigReferences.class);
    if (references != null) {
      stream(references.value())
          .map(ref -> new StereotypeTypeAnnotation(singletonList(newStereotype(ref.name(), ref.namespace())
              .withParent(CONFIG)
              .build())))
          .forEach(fieldBuilder::with);
    }

    ConfigReference ref = field.getAnnotation(ConfigReference.class);
    if (ref != null) {
      fieldBuilder.with(new StereotypeTypeAnnotation(singletonList(newStereotype(ref.name(), ref.namespace())
          .withParent(CONFIG)
          .build())));
    }

    if (field.getAnnotation(FlowReference.class) != null) {
      if (getBoolean(MULE_FLOW_REFERERENCE_FIELDS_MATCH_ANY)) {
        fieldBuilder.with(new StereotypeTypeAnnotation(asList(FLOW, SUB_FLOW, OBJECT_STORE, CONFIG)));
      } else {
        fieldBuilder.with(new StereotypeTypeAnnotation(singletonList(FLOW)));
      }
    }

    if (field.getAnnotation(ObjectStoreReference.class) != null) {
      fieldBuilder.with(new StereotypeTypeAnnotation(singletonList(OBJECT_STORE)));
    }
  }

  private void processElementStyle(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    final ParameterDsl annotation = field.getAnnotation(ParameterDsl.class);
    if (annotation != null) {
      fieldBuilder.with(new ParameterDslAnnotation(annotation.allowInlineDefinition(),
                                                   annotation.allowReferences()));
    }
  }

  private void setOptionalAndDefault(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    Optional<Content> contentAnnotation = ofNullable(field.getAnnotation(Content.class));
    Optional<org.mule.runtime.extension.api.annotation.param.Optional> optionalAnnotation =
        ofNullable(field.getAnnotation(org.mule.runtime.extension.api.annotation.param.Optional.class));
    Optional<org.mule.sdk.api.annotation.param.Optional> sdkOptionalAnnotation =
        ofNullable(field.getAnnotation(org.mule.sdk.api.annotation.param.Optional.class));
    fieldBuilder.required(true);

    contentAnnotation.ifPresent(content -> {
      if (content.primary()) {
        fieldBuilder.required(false);
        if (getDefaultValue(field) == null) {
          fieldBuilder.with(new DefaultValueAnnotation(PAYLOAD));
        }
      }
    });

    if (sdkOptionalAnnotation.isPresent()) {
      optionalField(fieldBuilder, getDefaultValue(sdkOptionalAnnotation.get()));
    } else if (optionalAnnotation.isPresent()) {
      optionalField(fieldBuilder, getDefaultValue(optionalAnnotation.get()));
    }

    if (Boolean.class.isAssignableFrom(field.getType()) || boolean.class.isAssignableFrom(field.getType())) {
      fieldBuilder.required(false);
      if (getDefaultValue(field) == null && field.getAnnotation(ConfigOverride.class) == null) {
        fieldBuilder.with(new DefaultValueAnnotation(valueOf(FALSE)));
      }
    }
  }

  private void fallbackToBeanProperties(Class<?> clazz, TypeHandlerManager typeHandlerManager, ParsingContext context,
                                        ObjectTypeBuilder builder) {
    if (!clazz.isInterface()) {
      new DefaultObjectFieldHandler().handleFields(clazz, typeHandlerManager, context, builder);
    }
  }

  private void optionalField(ObjectFieldTypeBuilder fieldBuilder, String defaultValue) {
    fieldBuilder.required(false);
    if (defaultValue != null) {
      fieldBuilder.with(new DefaultValueAnnotation(defaultValue));
    }
  }
}
