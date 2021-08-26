/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.annotation.DefaultValueAnnotation;
import org.mule.metadata.api.model.NumberType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.dsl.xml.ParameterDsl;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.param.display.Text;
import org.mule.runtime.extension.api.declaration.type.annotation.DisplayTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.FlattenedTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.LayoutTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterDslAnnotation;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;

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
    assertThat(type.getFields().iterator().next().getAnnotation(ParameterDslAnnotation.class).isPresent(), is(true));
  }

  @Test
  public void xmlElementStyleWithSdkApi() {
    ObjectType type = (ObjectType) typeLoader.load(SdkNoRefType.class);
    assertThat(type.getFields(), hasSize(1));
    assertThat(type.getFields().iterator().next().getAnnotation(ParameterDslAnnotation.class).isPresent(), is(true));
  }

  @Test(expected = IllegalModelDefinitionException.class)
  public void invalidXmlElementStyle() {
    ObjectType type = (ObjectType) typeLoader.load(InvalidNoRefType.class);
    assertThat(type.getFields(), hasSize(1));
    assertThat(type.getFields().iterator().next().getAnnotation(ParameterDslAnnotation.class).isPresent(), is(true));
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
  public void layoutField() {
    ObjectType type = (ObjectType) typeLoader.load(Group.class);
    assertThat(type.getFields().isEmpty(), is(false));
    ObjectFieldType field = getField(type, "text");

    assertThat(field.getAnnotation(LayoutTypeAnnotation.class).isPresent(), is(true));
    assertThat(field.getAnnotation(LayoutTypeAnnotation.class).get().isText(), is(true));
    assertThat(field.getValue(), is(instanceOf(StringType.class)));

    field = getField(type, "password");
    assertThat(field.getAnnotation(LayoutTypeAnnotation.class).isPresent(), is(true));
    assertThat(field.getAnnotation(LayoutTypeAnnotation.class).get().isPassword(), is(true));
    assertThat(field.getValue(), is(instanceOf(StringType.class)));

    field = getField(type, "placement");
    assertThat(field.getAnnotation(LayoutTypeAnnotation.class).isPresent(), is(true));
    assertThat(field.getAnnotation(LayoutTypeAnnotation.class).get().getTabName().get(), is("tab"));
    assertThat(field.getAnnotation(LayoutTypeAnnotation.class).get().getOrder().get(), is(5));
    assertThat(field.getValue(), is(instanceOf(NumberType.class)));
  }

  private ObjectFieldType getField(ObjectType type, String fieldName) {
    return type.getFields().stream().filter(f -> f.getKey().getName().getLocalPart().equals(fieldName)).findFirst().get();
  }

  @Test
  public void displayField() {
    ObjectType type = (ObjectType) typeLoader.load(DisplayField.class);
    assertThat(type.getFields().isEmpty(), is(false));
    ObjectFieldType field = getField(type, "displayName");

    assertThat(field.getAnnotation(DisplayTypeAnnotation.class).isPresent(), is(true));
    assertThat(field.getAnnotation(DisplayTypeAnnotation.class).get().getDisplayName(), is("Display name"));
    assertThat(field.getValue(), is(instanceOf(StringType.class)));

    field = getField(type, "summary");
    assertThat(field.getAnnotation(DisplayTypeAnnotation.class).get().getSummary(), is("summary value"));
    assertThat(field.getValue(), is(instanceOf(StringType.class)));
  }

  @Test
  public void contentField() {
    ObjectType type = (ObjectType) typeLoader.load(ContentWithDefaultValue.class);
    assertThat(type.getFields().isEmpty(), is(false));

    ObjectFieldType field = getField(type, "contentWithDefaultValue");
    assertThat(field.getAnnotation(DefaultValueAnnotation.class).get().getValue(), is("some value"));
    assertThat(field.isRequired(), is(false));
    assertThat(field.getValue(), is(instanceOf(StringType.class)));

    field = getField(type, "contentWithoutDefaultValue");
    assertThat(field.getAnnotation(DefaultValueAnnotation.class).isPresent(), is(false));
    assertThat(field.isRequired(), is(false));
    assertThat(field.getValue(), is(instanceOf(StringType.class)));

    field = getField(type, "requiredContent");
    assertThat(field.getAnnotation(DefaultValueAnnotation.class).isPresent(), is(false));
    assertThat(field.isRequired(), is(true));
    assertThat(field.getValue(), is(instanceOf(StringType.class)));


    field = getField(type, "primaryContentWithDefaultValue");
    assertThat(field.getAnnotation(DefaultValueAnnotation.class).get().getValue(), is("this is not #[payload]"));
    assertThat(field.isRequired(), is(false));
    assertThat(field.getValue(), is(instanceOf(StringType.class)));

    type = (ObjectType) typeLoader.load(PrimaryContentWithoutDefaultValue.class);

    field = getField(type, "primaryContentWithoutDefaultValue");
    assertThat(field.getAnnotation(DefaultValueAnnotation.class).get().getValue(), is("#[payload]"));
    assertThat(field.isRequired(), is(false));
    assertThat(field.getValue(), is(instanceOf(StringType.class)));

  }

  @Test(expected = IllegalModelDefinitionException.class)
  public void invalidAnnotatedFields() {
    typeLoader.load(InvalidAnnotatedFields.class);
  }

  interface HasGetter {

    String getSomeString();
  }

  public class NoRefType {

    @Parameter
    @ParameterDsl(allowReferences = false)
    private Object data;
  }

  public class SdkNoRefType {

    @Parameter
    @org.mule.sdk.api.annotation.dsl.xml.ParameterDsl(allowReferences = false)
    private Object data;
  }

  public class InvalidNoRefType {

    @Parameter
    @org.mule.sdk.api.annotation.dsl.xml.ParameterDsl(allowReferences = false)
    @ParameterDsl(allowReferences = false)
    private Object data;
  }

  public class HasGroup {

    @ParameterGroup(name = "group")
    private Group group;
  }

  public class DisplayField {

    @Parameter
    @Summary("summary value")
    private String summary;

    @Parameter
    @DisplayName("Display name")
    private String displayName;
  }

  public class Group {

    @Parameter
    @Text
    private String text;

    @Parameter
    @Password
    private String password;

    @Parameter
    @Placement(tab = "tab", order = 5)
    private Integer placement;
  }

  public class InvalidAnnotatedFields {

    @Optional
    private String notAParameter;

    @Expression(NOT_SUPPORTED)
    private Object iSaidNotAParameterDoNotInsist;

  }

  public class ContentWithDefaultValue {

    @Parameter
    @Content
    @Optional(defaultValue = "some value")
    private String contentWithDefaultValue;

    @Parameter
    @Content
    @Optional
    private String contentWithoutDefaultValue;

    @Parameter
    @Content
    private String requiredContent;

    @Parameter
    @Content(primary = true)
    @Optional(defaultValue = "this is not #[payload]")
    private String primaryContentWithDefaultValue;
  }

  public class PrimaryContentWithoutDefaultValue {

    @Parameter
    @Content(primary = true)
    private String primaryContentWithoutDefaultValue;
  }
}
