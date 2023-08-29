/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.declaration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mule.metadata.java.api.JavaTypeLoader.JAVA;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.annotation.TypeIdAnnotation;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.NullType;
import org.mule.metadata.api.model.VoidType;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;

import java.util.Optional;

public abstract class BaseDeclarationTestCase {

  protected final BaseTypeBuilder typeBuilder = BaseTypeBuilder.create(JAVA);
  protected final ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

  protected void assertParameter(ParameterDeclaration parameter,
                                 String name,
                                 String description,
                                 ExpressionSupport expressionSupport,
                                 boolean required,
                                 MetadataType type,
                                 Object defaultValue) {
    assertThat(parameter, is(notNullValue()));
    assertThat(parameter.getName(), is(name));
    assertThat(parameter.getDescription(), is(description));
    assertThat(parameter.getExpressionSupport(), is(expressionSupport));
    assertThat(parameter.isRequired(), is(required));
    assertThat(parameter.getDefaultValue(), equalTo(defaultValue));
    assertThat(parameter.getType().getClass(), equalTo(type.getClass()));
    assertThat(getId(parameter.getType()).get(), equalTo(getId(type).get()));
  }

  protected void assertDataType(MetadataType type, Class<?> expectedRawType, Class<? extends MetadataType> typeQualifier) {
    assertThat(type, is(instanceOf(typeQualifier)));

    if (type instanceof NullType || type instanceof VoidType) {
      return;
    }

    Optional<TypeIdAnnotation> typeId = type.getAnnotation(TypeIdAnnotation.class);
    assertThat(typeId.isPresent(), is(true));
    TypeIdAnnotation typeIdAnnotation = typeId.get();
    assertThat(expectedRawType.getName(), is(equalTo(typeIdAnnotation.getValue())));
  }
}
