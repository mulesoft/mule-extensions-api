/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader;

import static java.util.Collections.emptySet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mule.metadata.java.api.utils.JavaTypeUtils.getType;
import static org.mule.runtime.api.dsl.DslResolvingContext.getDefault;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.ExtensionModelLoader;
import org.mule.runtime.extension.internal.property.InfrastructureParameterModelProperty;

import java.util.HashMap;
import java.util.function.Consumer;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseExtensionModelFactoryTestCase {

  protected ExtensionModel extensionModel;
  private Consumer<ExtensionDeclarer> declarerConsumer = d -> {
  };
  protected ExtensionModelLoader loader = new ExtensionModelLoader() {

    @Override
    public String getId() {
      return "test";
    }

    @Override
    protected void declareExtension(ExtensionLoadingContext context) {
      declarerConsumer.accept(context.getExtensionDeclarer());
    }
  };

  protected ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

  protected ExtensionModel load() {
    return extensionModel = loader.loadExtensionModel(getClass().getClassLoader(), getDefault(emptySet()), new HashMap<>());
  }

  protected void assertParameter(ParameterModel parameterModel, String name, String description,
                                 ExpressionSupport expressionSupport, boolean required, MetadataType metadataType,
                                 Class<? extends MetadataType> qualifier, Object defaultValue) {
    assertThat(parameterModel, is(notNullValue()));
    assertThat(parameterModel.getName(), equalTo(name));
    assertThat(parameterModel.getDescription(), equalTo(description));
    assertThat(parameterModel.getExpressionSupport(), is(expressionSupport));
    assertThat(parameterModel.isRequired(), is(required));
    assertThat(parameterModel.getType(), is(instanceOf(qualifier)));

    if (!parameterModel.getModelProperty(InfrastructureParameterModelProperty.class).isPresent()) {
      assertThat(getType(parameterModel.getType()), equalTo(getType(metadataType)));
    }

    if (defaultValue != null) {
      assertThat(parameterModel.getDefaultValue(), equalTo(defaultValue));
    } else {
      assertThat(parameterModel.getDefaultValue(), is(nullValue()));
    }
  }

  protected void assertDataType(MetadataType metadataType, Class<?> expectedRawType,
                                Class<? extends MetadataType> typeQualifier) {
    assertThat(metadataType, is(instanceOf(typeQualifier)));
    assertThat(expectedRawType.isAssignableFrom(getType(metadataType)), is(true));
  }

  protected void declare(Consumer<ExtensionDeclarer> consumer) {
    this.declarerConsumer = consumer;
  }

}
