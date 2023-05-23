/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.internal.loader.enricher;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.annotation.TypeIdAnnotation;
import org.mule.metadata.api.builder.ObjectFieldTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataFormat;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.impl.DefaultObjectType;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.type.TypeCatalog;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterDslAnnotation;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.internal.loader.enricher.ParameterDslDeclarationEnricher;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ParameterDslDeclarationEnricherTestCase {

  private ParameterDslDeclarationEnricher enricher;
  private ExtensionLoadingContext extensionLoadingContext;
  private ExtensionDeclaration declaration;
  private ParameterGroupDeclaration parameterGroupDeclaration;
  private ObjectFieldType paramTypeField;

  @Before
  public void before() {
    enricher = new ParameterDslDeclarationEnricher();
    extensionLoadingContext = mock(ExtensionLoadingContext.class);

    declaration = mock(ExtensionDeclaration.class);
    parameterGroupDeclaration = mock(ParameterGroupDeclaration.class);
    final OperationDeclaration operationDeclaration = mock(OperationDeclaration.class);
    when(operationDeclaration.getParameterGroups()).thenReturn(singletonList(parameterGroupDeclaration));
    when(declaration.getOperations()).thenReturn(singletonList(operationDeclaration));

    final ExtensionDeclarer extensionDeclarer = mock(ExtensionDeclarer.class);
    when(extensionDeclarer.getDeclaration()).thenReturn(declaration);
    when(extensionLoadingContext.getExtensionDeclarer()).thenReturn(extensionDeclarer);

    final DslResolvingContext dslResolvingContext = mock(DslResolvingContext.class);
    when(dslResolvingContext.getTypeCatalog()).thenReturn(mock(TypeCatalog.class));
    when(extensionLoadingContext.getDslResolvingContext()).thenReturn(dslResolvingContext);

    paramTypeField = new ObjectFieldTypeBuilder(new MetadataFormat("label", "id")).key("key")
        .value(new ObjectTypeBuilder(new MetadataFormat("fieldLabel", "fieldKey")).build()).build();
  }

  @Test
  public void sameAsNormalized() {
    final Map<Class<? extends TypeAnnotation>, TypeAnnotation> typeAnnotations = new HashMap<>();
    typeAnnotations.put(TypeIdAnnotation.class, new TypeIdAnnotation("org.mule.test.SomeClass"));
    typeAnnotations.put(ParameterDslAnnotation.class, new ParameterDslAnnotation(false, false));
    typeAnnotations.put(ClassInformationAnnotation.class,
                        new ClassInformationAnnotation("org.mule.test.SomeClass", true, false, true, false, false,
                                                       emptyList(), null, emptyList(), false));
    final DefaultObjectType type = new DefaultObjectType(singletonList(paramTypeField), true, null, null, typeAnnotations);

    final ParameterDeclaration someParam = new ParameterDeclaration("someParam");
    someParam.setType(type, false);

    when(declaration.getTypes()).thenReturn(singleton(type));


    when(parameterGroupDeclaration.getParameters()).thenReturn(singletonList(someParam));

    enricher.enrich(extensionLoadingContext);

    final ParameterDslConfiguration dslConfiguration = someParam.getDslConfiguration();

    assertThat(dslConfiguration.allowsInlineDefinition(), is(false));
    assertThat(dslConfiguration.allowsReferences(), is(true));
    assertThat(dslConfiguration.allowTopLevelDefinition(), is(false));
  }

  @Test
  public void parameterSpecificAnnotation() {
    final Map<Class<? extends TypeAnnotation>, TypeAnnotation> typeAnnotations = new HashMap<>();
    typeAnnotations.put(TypeIdAnnotation.class, new TypeIdAnnotation("org.mule.test.SomeClass"));
    final DefaultObjectType extensionType =
        new DefaultObjectType(singletonList(paramTypeField), true, null, null, typeAnnotations);

    final Map<Class<? extends TypeAnnotation>, TypeAnnotation> paramTypeAnnotations = new HashMap<>();
    paramTypeAnnotations.put(TypeIdAnnotation.class, new TypeIdAnnotation("org.mule.test.SomeClass"));
    paramTypeAnnotations.put(ParameterDslAnnotation.class, new ParameterDslAnnotation(false, false));
    paramTypeAnnotations.put(ClassInformationAnnotation.class,
                             new ClassInformationAnnotation("org.mule.test.SomeClass", true, false, true, false, false,
                                                            emptyList(), null, emptyList(), false));
    final DefaultObjectType paramType =
        new DefaultObjectType(singletonList(paramTypeField), true, null, null, paramTypeAnnotations);

    final ParameterDeclaration someParam = new ParameterDeclaration("someParam");
    someParam.setType(paramType, false);

    when(declaration.getTypes()).thenReturn(singleton(extensionType));


    when(parameterGroupDeclaration.getParameters()).thenReturn(singletonList(someParam));

    enricher.enrich(extensionLoadingContext);

    final ParameterDslConfiguration dslConfiguration = someParam.getDslConfiguration();

    assertThat(dslConfiguration.allowsInlineDefinition(), is(false));
    assertThat(dslConfiguration.allowsReferences(), is(true));
    assertThat(dslConfiguration.allowTopLevelDefinition(), is(false));
  }

}
