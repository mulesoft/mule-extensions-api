/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.metadata.api.model.MetadataFormat.JSON;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.annotation.TypeIdAnnotation;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.extension.api.annotation.Alias;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class NameUtilsTestCase {

  private static final String TYPE_ALIAS = "Batman";

  private ObjectType objectType;

  @Before
  public void setup() {
    objectType = mock(ObjectType.class);
    when(objectType.getAnnotation(TypeAliasAnnotation.class)).thenReturn(empty());
    doAnswer(invocation -> {
      MetadataTypeVisitor visitor = (MetadataTypeVisitor) invocation.getArguments()[0];
      visitor.visitObject(objectType);
      return null;
    }).when(objectType).accept(Mockito.any(MetadataTypeVisitor.class));
  }

  @Test
  public void getTopLevelTypeNameByTypeAlias() {
    when(objectType.getAnnotation(TypeAliasAnnotation.class)).thenReturn(of(new TypeAliasAnnotation(TYPE_ALIAS)));
    assertThat(getTopLevelTypeName(objectType), is(hyphenize(TYPE_ALIAS)));
  }

  @Test
  public void getTopLevelTypeNameByTypeId() {
    final String typeId = getClass().getName();
    when(objectType.getMetadataFormat()).thenReturn(JSON);
    when(objectType.getAnnotation(TypeIdAnnotation.class)).thenReturn(of(new TypeIdAnnotation(typeId)));

    assertThat(getTopLevelTypeName(objectType), is(hyphenize(typeId)));
  }

  @Test
  public void getTopLevelTypeNameByAliasAnnotation() {
    when(objectType.getMetadataFormat()).thenReturn(JAVA);
    when(objectType.getAnnotation(TypeIdAnnotation.class)).thenReturn(of(new TypeIdAnnotation(AliasedClass.class.getName())));
    when(objectType.getAnnotation(ClassInformationAnnotation.class)).thenReturn(empty());

    assertThat(getTopLevelTypeName(objectType), is(hyphenize(TYPE_ALIAS)));
  }

  @Test
  public void getTopLevelTypeNameByClassInformationAnnotaion() {
    when(objectType.getMetadataFormat()).thenReturn(JAVA);
    when(objectType.getAnnotation(TypeIdAnnotation.class)).thenReturn(empty());
    when(objectType.getAnnotation(ClassInformationAnnotation.class))
        .thenReturn(of(new ClassInformationAnnotation(AliasedClass.class)));

    assertThat(getTopLevelTypeName(objectType), is(hyphenize(TYPE_ALIAS)));
  }


  @Alias(TYPE_ALIAS)
  private static class AliasedClass {

  }
}
