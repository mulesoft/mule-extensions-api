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
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.meta.model.stereotype.StereotypeModelBuilder.newStereotype;
import static org.mule.runtime.api.util.MuleSystemProperties.MULE_FLOW_REFERERENCE_FIELDS_MATCH_ANY;
import static org.mule.runtime.extension.api.annotation.param.Optional.PAYLOAD;
import static org.mule.runtime.extension.api.declaration.type.TypeUtils.getAlias;
import static org.mule.runtime.extension.api.declaration.type.TypeUtils.getAllFields;
import static org.mule.runtime.extension.api.declaration.type.TypeUtils.getParameterFields;
import static org.mule.runtime.extension.api.declaration.type.TypeUtils.isOptional;
import static org.mule.runtime.extension.api.declaration.type.TypeUtils.isParameterGroup;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.CONFIG;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.FLOW;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.OBJECT_STORE;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.SUB_FLOW;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.getDefaultValue;
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
import org.mule.runtime.api.meta.model.display.ClassValueModel;
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
import org.mule.sdk.api.annotation.semantics.file.FilePath;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

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
    if (isParameterGroup(field)) {
      fieldBuilder.with(new FlattenedTypeAnnotation());
      processExclusiveOptionals(field, fieldBuilder);
    }
  }

  private void processExclusiveOptionals(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    ExclusiveOptionals exclusiveOptionals = field.getType().getAnnotation(ExclusiveOptionals.class);
    if (exclusiveOptionals != null) {
      Set<String> exclusiveParameters = getParameterFields(field.getType()).stream()
          .filter(TypeUtils::isOptional)
          .map(f -> f.getAnnotation(Alias.class) != null ? f.getAnnotation(Alias.class).value() : f.getName())
          .collect(Collectors.toCollection(LinkedHashSet::new));
      fieldBuilder.with(new ExclusiveOptionalsTypeAnnotation(exclusiveParameters, exclusiveOptionals.isOneRequired()));
    }
  }

  private void processDisplayAnnotation(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    DisplayModel.DisplayModelBuilder builder = DisplayModel.builder();
    boolean shouldAddTypeAnnotation = false;

    String displayNameValue = getAnnotationValueFromField(
                                                          field,
                                                          DisplayName.class,
                                                          org.mule.sdk.api.annotation.param.display.DisplayName.class,
                                                          DisplayName::value,
                                                          org.mule.sdk.api.annotation.param.display.DisplayName::value);

    if (displayNameValue != null) {
      builder.displayName(displayNameValue);
      shouldAddTypeAnnotation = true;
    }

    String summaryValue = getAnnotationValueFromField(
                                                      field,
                                                      Summary.class,
                                                      org.mule.sdk.api.annotation.param.display.Summary.class,
                                                      Summary::value, org.mule.sdk.api.annotation.param.display.Summary::value);

    if (summaryValue != null) {
      builder.summary(summaryValue);
      shouldAddTypeAnnotation = true;
    }

    String exampleValue = getAnnotationValueFromField(
                                                      field,
                                                      Example.class,
                                                      org.mule.sdk.api.annotation.param.display.Example.class,
                                                      Example::value, org.mule.sdk.api.annotation.param.display.Example::value);

    if (exampleValue != null) {
      builder.example(exampleValue);
      shouldAddTypeAnnotation = true;
    }

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

    if (pathModel != null) {
      builder.path(pathModel);
      shouldAddTypeAnnotation = true;
    }

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

    if (classValueModel != null) {
      builder.classValue(classValueModel);
      shouldAddTypeAnnotation = true;
    }

    if (shouldAddTypeAnnotation) {
      fieldBuilder.with(new DisplayTypeAnnotation(builder.build()));
    }
  }

  private void processLayoutAnnotation(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    LayoutModel.LayoutModelBuilder builder = LayoutModel.builder();
    boolean shouldAddTypeAnnotation = false;

    Pair<Integer, String> value = getAnnotationValueFromField(field,
                                                              Placement.class,
                                                              org.mule.sdk.api.annotation.param.display.Placement.class,
                                                              (Placement placement) -> Pair.of(placement.order(),
                                                                                               placement.tab()),
                                                              (org.mule.sdk.api.annotation.param.display.Placement placement) -> Pair
                                                                  .of(placement.order(), placement.tab()));
    if (value != null) {
      builder.tabName(value.getRight()).order(value.getLeft());
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

    if (!isOptional(field) && !isParameterGroup(field)) {
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
    final ParameterDsl legacyAnnotation = field.getAnnotation(ParameterDsl.class);
    final org.mule.sdk.api.annotation.dsl.xml.ParameterDsl sdkAnnotation =
        field.getAnnotation(org.mule.sdk.api.annotation.dsl.xml.ParameterDsl.class);
    if (legacyAnnotation != null && sdkAnnotation != null) {
      throw new IllegalModelDefinitionException(format("Parameter '%s' is annotated with '@%s' and '@%s' at the same time",
                                                       field.getName(), ParameterDsl.class.getName(),
                                                       org.mule.sdk.api.annotation.dsl.xml.ParameterDsl.class.getName()));
    } else if (legacyAnnotation != null) {
      fieldBuilder.with(new ParameterDslAnnotation(legacyAnnotation.allowInlineDefinition(), legacyAnnotation.allowReferences()));
    } else if (sdkAnnotation != null) {
      fieldBuilder.with(new ParameterDslAnnotation(sdkAnnotation.allowInlineDefinition(), sdkAnnotation.allowReferences()));
    }
  }

  private void setOptionalAndDefault(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    Optional<Content> contentAnnotation = ofNullable(field.getAnnotation(Content.class));
    fieldBuilder.required(true);

    contentAnnotation.ifPresent(content -> {
      if (content.primary()) {
        fieldBuilder.required(false);
        if (getDefaultValue(field) == null) {
          fieldBuilder.with(new DefaultValueAnnotation(PAYLOAD));
        }
      }
    });

    if (isOptional(field)) {
      optionalField(fieldBuilder, (String) getDefaultValue(field));
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
