/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.runtime.extension.api.annotation.Parameter;
import org.mule.runtime.extension.api.annotation.ParameterGroup;
import org.mule.runtime.extension.api.annotation.dsl.xml.XmlHints;
import org.mule.runtime.extension.api.annotation.param.display.Text;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.FlattenedTypeAnnotation;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.TextTypeAnnotation;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.XmlHintsAnnotation;

import org.junit.Test;

public class ExtensionFieldHandlerTestCase {

  private ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

  @Test
  public void interfaceWithGetter() {
    ObjectType type = (ObjectType) typeLoader.load(HasGetter.class);
    assertThat(type.getFields(), hasSize(0));
  }

  @Test
  public void xmlElementStyle() {
    ObjectType type = (ObjectType) typeLoader.load(NoRefType.class);
    assertThat(type.getFields(), hasSize(1));
    assertThat(type.getFields().iterator().next().getAnnotation(XmlHintsAnnotation.class).isPresent(), is(true));
  }

  @Test
  public void flattenedField() {
    ObjectType type = (ObjectType) typeLoader.load(HasGroup.class);
    assertThat(type.getFields().isEmpty(), is(false));
    ObjectFieldType field =
        type.getFields().stream().filter(f -> f.getKey().getName().getLocalPart().equals("group")).findFirst().get();

    assertThat(field.getAnnotation(FlattenedTypeAnnotation.class).isPresent(), is(true));
    assertThat(field.getValue(), is(instanceOf(ObjectType.class)));
  }

  @Test
  public void textField() {
    ObjectType type = (ObjectType) typeLoader.load(Group.class);
    assertThat(type.getFields().isEmpty(), is(false));
    ObjectFieldType field =
        type.getFields().stream().filter(f -> f.getKey().getName().getLocalPart().equals("text")).findFirst().get();

    assertThat(field.getAnnotation(TextTypeAnnotation.class).isPresent(), is(true));
    assertThat(field.getValue(), is(instanceOf(StringType.class)));
  }

  interface HasGetter {

    String getSomeString();
  }

  public class NoRefType {

    @Parameter
    @XmlHints(allowReferences = false)
    private Object data;
  }

  public class HasGroup {

    @ParameterGroup
    private Group group;
  }

  public class Group {

    @Parameter
    @Text
    private String text;
  }
}
