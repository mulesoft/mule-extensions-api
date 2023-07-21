/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.internal.loader.enricher;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.annotation.TypeIdAnnotation;
import org.mule.metadata.api.builder.ObjectFieldTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataFormat;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.impl.DefaultObjectType;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.api.meta.model.SubTypesModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OutputDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterDslAnnotation;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.internal.loader.enricher.ExtensionTypesDeclarationEnricher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mockito;

public class ExtensionTypesDeclarationEnricherTestCase {

  @Test
  public void subTypesAndTypesAreSameInstance() {
    final ExtensionLoadingContext extensionLoadingContext = mock(ExtensionLoadingContext.class);
    final ExtensionDeclarer extensionDeclarer = mock(ExtensionDeclarer.class);
    final ExtensionDeclaration extensionDeclaration = mock(ExtensionDeclaration.class);
    when(extensionDeclarer.getDeclaration()).thenReturn(extensionDeclaration);
    when(extensionLoadingContext.getExtensionDeclarer()).thenReturn(extensionDeclarer);

    final ParameterGroupDeclaration parameterGroupDeclaration = mock(ParameterGroupDeclaration.class);
    final OperationDeclaration operationDeclaration = mock(OperationDeclaration.class);
    when(operationDeclaration.getOutput()).thenReturn(mock(OutputDeclaration.class, RETURNS_DEEP_STUBS));
    when(operationDeclaration.getOutputAttributes()).thenReturn(mock(OutputDeclaration.class, RETURNS_DEEP_STUBS));
    when(operationDeclaration.getParameterGroups()).thenReturn(singletonList(parameterGroupDeclaration));
    when(extensionDeclaration.getOperations()).thenReturn(singletonList(operationDeclaration));

    ObjectFieldType paramTypeField = new ObjectFieldTypeBuilder(new MetadataFormat("label", "id")).key("key")
        .value(new ObjectTypeBuilder(new MetadataFormat("fieldLabel", "fieldKey")).build()).build();

    final Map<Class<? extends TypeAnnotation>, TypeAnnotation> typeAnnotations = new HashMap<>();
    typeAnnotations.put(TypeIdAnnotation.class, new TypeIdAnnotation("org.mule.test.SomeClass"));
    typeAnnotations.put(ParameterDslAnnotation.class, new ParameterDslAnnotation(false, false));
    typeAnnotations.put(ClassInformationAnnotation.class,
                        new ClassInformationAnnotation("org.mule.test.SomeClass", true, false, true, false, false,
                                                       emptyList(), null, emptyList(), false));
    final DefaultObjectType typeA = new DefaultObjectType(singletonList(paramTypeField), true, null, null, typeAnnotations);
    final DefaultObjectType typeB = new DefaultObjectType(singletonList(paramTypeField), true, null, null, typeAnnotations);
    final DefaultObjectType typeC = new DefaultObjectType(singletonList(paramTypeField), true, null, null, typeAnnotations);

    final ParameterDeclaration someParam = new ParameterDeclaration("someParam");
    someParam.setType(typeA, false);
    when(parameterGroupDeclaration.getParameters()).thenReturn(singletonList(someParam));
    when(operationDeclaration.getAllParameters()).thenReturn(singletonList(someParam));

    final Set<SubTypesModel> subTypes = new HashSet<>();
    subTypes.add(new SubTypesModel(typeB, singleton(typeC)));
    when(extensionDeclaration.getSubTypes()).thenReturn(subTypes);

    Set<ObjectType> extensionTypes = new HashSet<>();
    when(extensionDeclarer.withType(Mockito.any())).thenAnswer(inv -> {
      final ObjectType newType = inv.getArgument(0);
      extensionTypes.add(newType);
      return extensionDeclarer;
    });

    new ExtensionTypesDeclarationEnricher().enrich(extensionLoadingContext);

    assertThat("The type and subtypes are not normalized.", extensionTypes, hasItem(sameInstance(typeB)));
  }
}
