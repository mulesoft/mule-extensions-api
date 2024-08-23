/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.declaration.type;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsIterableContaining.hasItem;
import static org.hamcrest.core.IsIterableContaining.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.api.model.MetadataFormat;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.java.api.handler.ObjectFieldHandler;
import org.mule.metadata.java.api.handler.TypeHandlerManager;
import org.mule.metadata.java.api.utils.ParsingContext;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.extension.api.declaration.type.ExtensionObjectTypeHandler;
import org.mule.runtime.extension.api.declaration.type.annotation.LiteralTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterResolverTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.TypedValueTypeAnnotation;
import org.mule.runtime.extension.api.test.internal.loader.TestingClass;
import org.mule.sdk.api.runtime.parameter.Literal;
import org.mule.sdk.api.runtime.parameter.ParameterResolver;

import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

public class ExtensionObjectTypeHandlerTestCase {

  private ExtensionObjectTypeHandler handler;
  private TypeHandlerManager typeHandlerManager;
  private ParsingContext parsingContext;

  @Before
  public void setUp() {
    handler = new ExtensionObjectTypeHandler(mock(ObjectFieldHandler.class));
    typeHandlerManager = mock(TypeHandlerManager.class);
    when(typeHandlerManager.handle(any(), any(), any()))
        .then(inv -> {
          final TypeBuilder newBuilder = BaseTypeBuilder.create(MetadataFormat.JAVA)
              .anyType();

          inv.getArgument(1, ParsingContext.class)
              .addTypeBuilder(inv.getArgument(0, Type.class), newBuilder);

          return newBuilder;
        });
    parsingContext = new ParsingContext();
  }

  @Test
  public void passThrough() {
    handler.handleClass(TestingClass.class,
                        emptyList(),
                        typeHandlerManager,
                        parsingContext,
                        BaseTypeBuilder.create(MetadataFormat.JAVA));

    final MetadataType type = parsingContext.getTypeBuilder(TestingClass.class).get().build();
    assertThat(type.getAnnotations(),
               not(hasItems(instanceOf(ParameterResolverTypeAnnotation.class),
                            instanceOf(LiteralTypeAnnotation.class),
                            instanceOf(TypedValueTypeAnnotation.class))));
  }

  @Test
  public void parameterResolver() {
    handler.handleClass(ParameterResolver.class,
                        singletonList(TestingClass.class),
                        typeHandlerManager,
                        parsingContext,
                        BaseTypeBuilder.create(MetadataFormat.JAVA));

    assertThat(parsingContext.getTypeBuilder(TestingClass.class), is(empty()));

    final MetadataType type =
        parsingContext.getSubContext(ParameterResolver.class.getName()).getTypeBuilder(TestingClass.class).get().build();
    assertThat(type.getAnnotations(), hasItem(instanceOf(ParameterResolverTypeAnnotation.class)));
    assertThat(type.getAnnotations(),
               not(hasItems(instanceOf(LiteralTypeAnnotation.class),
                            instanceOf(TypedValueTypeAnnotation.class))));
  }

  @Test
  public void typedValue() {
    handler.handleClass(TypedValue.class,
                        singletonList(TestingClass.class),
                        typeHandlerManager,
                        parsingContext,
                        BaseTypeBuilder.create(MetadataFormat.JAVA));

    assertThat(parsingContext.getTypeBuilder(TestingClass.class), is(empty()));

    final MetadataType type =
        parsingContext.getSubContext(TypedValue.class.getName()).getTypeBuilder(TestingClass.class).get().build();
    assertThat(type.getAnnotations(), hasItem(instanceOf(TypedValueTypeAnnotation.class)));
    assertThat(type.getAnnotations(),
               not(hasItems(instanceOf(ParameterResolverTypeAnnotation.class),
                            instanceOf(LiteralTypeAnnotation.class))));
  }

  @Test
  public void literal() {
    handler.handleClass(Literal.class,
                        singletonList(TestingClass.class),
                        typeHandlerManager,
                        parsingContext,
                        BaseTypeBuilder.create(MetadataFormat.JAVA));

    assertThat(parsingContext.getTypeBuilder(TestingClass.class), is(empty()));

    final MetadataType type =
        parsingContext.getSubContext(Literal.class.getName()).getTypeBuilder(TestingClass.class).get().build();
    assertThat(type.getAnnotations(), hasItem(instanceOf(LiteralTypeAnnotation.class)));
    assertThat(type.getAnnotations(),
               not(hasItems(instanceOf(ParameterResolverTypeAnnotation.class),
                            instanceOf(TypedValueTypeAnnotation.class))));
  }

}
