/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.util;

import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.isReferableType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.impl.DefaultStringType;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;

import java.io.InputStream;
import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

public class ExtensionMetadataTypeUtilsTestCase {

  private ClassTypeLoader typeLoader;

  @Before
  public void setUp() throws Exception {
    typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
  }

  @Test
  public void isReferableTypeForNonClassBaseTypes() {
    DefaultStringType type = BaseTypeBuilder.create(JAVA).stringType().build();
    assertThat(isReferableType(type), is(false));
  }

  @Test
  public void isReferableTypeInputStream() {
    assertThat(isReferableType(typeLoader.load(InputStream.class)), is(false));
  }

  @Test
  public void isReferableTypeObject() {
    assertThat(isReferableType(typeLoader.load(Object.class)), is(true));
  }

  @Test
  public void isReferableTypeSerializable() {
    assertThat(isReferableType(typeLoader.load(Serializable.class)), is(true));
  }
}
