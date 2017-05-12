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
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.extension.api.annotation.param.Optional.PAYLOAD;
import static org.mule.runtime.extension.api.declaration.type.TypeUtils.getAlias;
import static org.mule.runtime.extension.api.declaration.type.TypeUtils.getAllFields;
import static org.mule.runtime.extension.api.declaration.type.TypeUtils.getParameterFields;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.getDefaultValue;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.roleOf;
import org.mule.metadata.api.annotation.DefaultValueAnnotation;
import org.mule.metadata.api.builder.ObjectFieldTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.java.api.handler.DefaultObjectFieldHandler;
import org.mule.metadata.java.api.handler.ObjectFieldHandler;
import org.mule.metadata.java.api.handler.TypeHandlerManager;
import org.mule.metadata.java.api.utils.ParsingContext;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.dsl.xml.XmlHints;
import org.mule.runtime.extension.api.annotation.param.ConfigOverride;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.DefaultEncoding;
import org.mule.runtime.extension.api.annotation.param.NullSafe;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.Query;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.param.display.Text;
import org.mule.runtime.extension.api.declaration.type.annotation.DefaultEncodingAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.DisplayTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ExpressionSupportAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.FlattenedTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.LayoutTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.NullSafeTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterRoleAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.exception.IllegalParameterModelDefinitionException;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
    // TODO: MULE-9454
    if (clazz.getName().equals("org.mule.runtime.core.api.NestedProcessor")) {
      return;
    }

    Collection<Field> fields = getParameterFields(clazz);
    if (fields.isEmpty()) {
      validateIllegalAnnotationUseOnNonParameterFields(clazz);
      fallbackToBeanProperties(clazz, typeHandlerManager, context, builder);
      return;
    }

    validateNoConfigOverride(clazz);

    for (Field field : fields) {
      final ObjectFieldTypeBuilder fieldBuilder = builder.addField();
      fieldBuilder.key(getAlias(field));

      setOptionalAndDefault(field, fieldBuilder);
      processesParameterGroup(field, fieldBuilder);
      processExpressionSupport(field, fieldBuilder);
      processNullSafe(clazz, field, fieldBuilder);
      processDefaultEncoding(field, fieldBuilder);
      processElementStyle(field, fieldBuilder);
      processLayoutAnnotation(field, fieldBuilder);
      processDisplayAnnotation(field, fieldBuilder);
      setFieldType(typeHandlerManager, context, field, fieldBuilder);
    }
  }

  private void validateNoConfigOverride(Class<?> clazz) {
    List<String> illegalFieldNames = getAllFields(clazz).stream()
        .filter(field -> field.getAnnotation(ConfigOverride.class) != null)
        .map(Field::getName)
        .collect(toList());

    if (!illegalFieldNames.isEmpty()) {
      throw new IllegalModelDefinitionException(
                                                format("Type '%s' has fields declared as '%s', which is not allowed."
                                                    + " Illegal fields are " + illegalFieldNames, clazz.getName(),
                                                       ConfigOverride.class.getSimpleName()));
    }
  }

  private void validateIllegalAnnotationUseOnNonParameterFields(Class<?> clazz) {
    final String annotationsPackageName = Parameter.class.getPackage().getName();
    List<String> illegalFieldNames = getAllFields(clazz).stream()
        .filter(field -> Stream.of(field.getAnnotations())
            .anyMatch(a -> a.annotationType().getName().contains(annotationsPackageName)))
        .map(Field::getName)
        .collect(toList());

    if (!illegalFieldNames.isEmpty()) {
      throw new IllegalModelDefinitionException(
                                                format("Class '%s' has fields which are not parameters but are annotated with Extensions API annotations."
                                                    + " Illegal fields are " + illegalFieldNames, clazz.getName()));
    }
  }

  private void processesParameterGroup(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    if (field.getAnnotation(ParameterGroup.class) != null) {
      fieldBuilder.with(new FlattenedTypeAnnotation());
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

  private void processNullSafe(Class<?> declaringClass, Field field, ObjectFieldTypeBuilder fieldBuilder) {
    NullSafe nullSafe = field.getAnnotation(NullSafe.class);
    if (nullSafe == null) {
      return;
    }

    final boolean isOptional = field.isAnnotationPresent(org.mule.runtime.extension.api.annotation.param.Optional.class);
    final boolean isParameterGroup = field.isAnnotationPresent(ParameterGroup.class);

    if (!isOptional && !isParameterGroup) {
      throw new IllegalParameterModelDefinitionException(format(
                                                                "Field '%s' in class '%s' is required but annotated with '@%s', which is redundant",
                                                                field.getName(), declaringClass.getName(),
                                                                NullSafe.class.getSimpleName()));
    }

    if (nullSafe.defaultImplementingType().equals(Object.class)) {
      fieldBuilder.with(new NullSafeTypeAnnotation(field.getType(), false));
    } else {
      fieldBuilder.with(new NullSafeTypeAnnotation(nullSafe.defaultImplementingType(), true));
    }
  }

  private void processElementStyle(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    final XmlHints annotation = field.getAnnotation(XmlHints.class);
    if (annotation != null) {
      fieldBuilder.with(new XmlHintsAnnotation(annotation.allowInlineDefinition(),
                                               annotation.allowTopLevelDefinition(),
                                               annotation.allowReferences()));
    }
  }

  private void setOptionalAndDefault(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    Optional<Content> contentAnnotation = ofNullable(field.getAnnotation(Content.class));
    Optional<org.mule.runtime.extension.api.annotation.param.Optional> optionalAnnotation =
        ofNullable(field.getAnnotation(org.mule.runtime.extension.api.annotation.param.Optional.class));
    fieldBuilder.with(new ParameterRoleAnnotation(roleOf(contentAnnotation)));
    fieldBuilder.required(true);

    contentAnnotation.ifPresent(content -> {
      fieldBuilder.required(false);
      if (content.primary() && getDefaultValue(field) == null) {
        fieldBuilder.with(new DefaultValueAnnotation(PAYLOAD));
      }
    });

    optionalAnnotation.ifPresent(optional -> {
      fieldBuilder.required(false);
      if (getDefaultValue(optional) != null) {
        fieldBuilder.with(new DefaultValueAnnotation(optional.defaultValue()));
      }
    });
  }

  private void processDefaultEncoding(Field field, ObjectFieldTypeBuilder fieldBuilder) {
    if (field.getAnnotation(DefaultEncoding.class) != null) {
      fieldBuilder.required(false);
      fieldBuilder.with(new DefaultEncodingAnnotation());
    }
  }

  private void fallbackToBeanProperties(Class<?> clazz, TypeHandlerManager typeHandlerManager, ParsingContext context,
                                        ObjectTypeBuilder builder) {
    if (!clazz.isInterface()) {
      new DefaultObjectFieldHandler().handleFields(clazz, typeHandlerManager, context, builder);
    }
  }
}
