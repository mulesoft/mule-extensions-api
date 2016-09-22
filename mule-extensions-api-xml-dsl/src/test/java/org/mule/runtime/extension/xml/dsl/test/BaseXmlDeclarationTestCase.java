/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.metadata.utils.MetadataTypeUtils.getTypeId;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.introspection.EnrichableModel;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.config.ConfigurationModel;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.property.ConfigTypeModelProperty;
import org.mule.runtime.extension.api.introspection.property.ConnectivityModelProperty;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.PagedOperationModelProperty;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.runtime.extension.api.introspection.source.SourceModel;
import org.mule.runtime.extension.xml.dsl.api.DslElementSyntax;
import org.mule.runtime.extension.xml.dsl.api.property.XmlModelProperty;
import org.mule.runtime.extension.xml.dsl.api.resolver.DslResolvingContext;
import org.mule.runtime.extension.xml.dsl.api.resolver.DslSyntaxResolver;
import org.mule.runtime.extension.xml.dsl.test.model.ExtensibleType;
import org.mule.runtime.extension.xml.dsl.test.model.SubType;
import org.mule.runtime.extension.xml.dsl.test.model.SuperType;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseXmlDeclarationTestCase {

  static final String NAMESPACE = "mockns";
  static final String NAMESPACE_URI = "http://www.mulesoft.org/schema/mule/mockns";
  static final String SCHEMA_LOCATION = "http://www.mulesoft.org/schema/mule/mockns/current/mule-mockns.xsd";
  static final String PARAMETER_NAME = "myCamelCaseName";
  static final String SINGULARIZABLE_PARAMETER_NAME = "singularizableNames";
  static final String EXTENSION_NAME = "extension";
  static final String OPERATION_NAME = "mockOperation";
  static final String SOURCE_NAME = "source";
  static final String CONFIGURATION_NAME = "configuration";
  static final String CONNECTION_PROVIDER_NAME = "connection";
  static final BaseTypeBuilder<?> TYPE_BUILDER = BaseTypeBuilder.create(JAVA);

  @Mock
  protected ExtensionModel extension;

  @Mock
  protected ConfigurationModel configuration;

  @Mock
  protected OperationModel operation;

  @Mock
  protected ConnectionProviderModel connectionProvider;

  @Mock
  protected ParameterModel parameterModel;

  @Mock
  protected SourceModel source;

  @Mock
  protected DslResolvingContext dslContext;

  protected ClassTypeLoader TYPE_LOADER = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
  SubTypesModelProperty subTypesModelProperty =
      new SubTypesModelProperty(singletonMap(TYPE_LOADER.load(SuperType.class), singletonList(TYPE_LOADER.load(SubType.class))));

  @Before
  public void before() {
    when(extension.getName()).thenReturn(EXTENSION_NAME);
    when(extension.getConfigurationModels()).thenReturn(asList(configuration));
    when(extension.getConfigurationModels()).thenReturn(asList(configuration));
    when(extension.getOperationModels()).thenReturn(asList(operation));
    when(extension.getSourceModels()).thenReturn(asList(source));
    when(extension.getConnectionProviders()).thenReturn(asList(connectionProvider));

    when(extension.getModelProperty(SubTypesModelProperty.class)).thenReturn(Optional.of(subTypesModelProperty));
    when(extension.getModelProperty(ImportedTypesModelProperty.class)).thenReturn(empty());
    when(extension.getModelProperty(XmlModelProperty.class))
        .thenReturn(of(new XmlModelProperty(EMPTY, NAMESPACE, NAMESPACE_URI, EMPTY, SCHEMA_LOCATION)));

    when(configuration.getOperationModels()).thenReturn(asList(operation));
    when(configuration.getSourceModels()).thenReturn(asList(source));
    when(configuration.getConnectionProviders()).thenReturn(asList(connectionProvider));

    when(parameterModel.getName()).thenReturn(PARAMETER_NAME);
    when(parameterModel.getExpressionSupport()).thenReturn(ExpressionSupport.SUPPORTED);
    when(parameterModel.getModelProperty(any())).thenReturn(empty());

    when(source.getName()).thenReturn(SOURCE_NAME);
    when(operation.getName()).thenReturn(OPERATION_NAME);
    when(configuration.getName()).thenReturn(CONFIGURATION_NAME);
    when(connectionProvider.getName()).thenReturn(CONNECTION_PROVIDER_NAME);

    noConfigOn(connectionProvider, operation, source, configuration);
    noConnectionOn(connectionProvider, operation, source, configuration);
    when(dslContext.getExtension(any())).thenReturn(empty());

    Stream.of(configuration, operation, connectionProvider, source).forEach(
                                                                            model -> when(model.getParameterModels())
                                                                                .thenReturn(asList(parameterModel)));
  }

  private void noConfigOn(EnrichableModel... models) {
    for (EnrichableModel model : models) {
      when(model.getModelProperty(ConfigTypeModelProperty.class)).thenReturn(empty());
    }
  }

  private void noConnectionOn(EnrichableModel... models) {
    for (EnrichableModel model : models) {
      when(model.getModelProperty(ConnectivityModelProperty.class)).thenReturn(empty());
      when(model.getModelProperty(PagedOperationModelProperty.class)).thenReturn(empty());
    }
  }

  void assertChildElementDeclarationIs(boolean expected, DslElementSyntax result) {
    assertThat("Expected attribute only declaration", result.supportsChildDeclaration(), is(expected));
  }

  void assertTopLevelDeclarationSupportIs(boolean expected, DslElementSyntax result) {
    assertThat("Wrong TopLevel declaration support", result.supportsTopLevelDeclaration(), is(expected));
  }

  void assertIsWrappedElement(boolean expected, DslElementSyntax result) {
    assertThat("Expected no wrapping but element is wrapped", result.isWrapped(), is(expected));
  }

  void assertAttributeName(String expected, DslElementSyntax result) {
    assertThat(result.getAttributeName(), equalTo(expected));
  }

  void assertElementName(String expected, DslElementSyntax result) {
    assertThat(result.getElementName(), equalTo(expected));
  }

  void assertElementNamespace(String expected, DslElementSyntax result) {
    assertThat(result.getNamespace(), equalTo(expected));
  }

  void assertTopElementDeclarationIs(boolean expected, DslElementSyntax result) {
    assertThat("Expected the element to support Top Level definitions", result.supportsTopLevelDeclaration(), is(expected));
  }

  DslSyntaxResolver getSyntaxResolver() {
    return new DslSyntaxResolver(extension, dslContext);
  }

  protected void assertComplexTypeDslFields(DslElementSyntax topDsl) {
    String extensibleTypeListName = "extensibleTypeList";
    DslElementSyntax listDsl = getChildFieldDsl(extensibleTypeListName, topDsl);
    assertAttributeName(extensibleTypeListName, listDsl);
    assertElementName(hyphenize(extensibleTypeListName), listDsl);
    assertElementNamespace(NAMESPACE, listDsl);
    assertChildElementDeclarationIs(true, listDsl);
    assertIsWrappedElement(false, listDsl);

    MetadataType listItemType = TYPE_LOADER.load(ExtensibleType.class);
    DslElementSyntax listItemDsl = getGenericTypeDsl(listItemType, listDsl);
    assertElementName(getTopLevelTypeName(listItemType), listItemDsl);
    assertElementNamespace(NAMESPACE, listItemDsl);
    assertChildElementDeclarationIs(true, listItemDsl);
    assertTopElementDeclarationIs(false, listItemDsl);
    assertIsWrappedElement(true, listItemDsl);

    String recursiveChildName = "recursiveChild";
    DslElementSyntax recursiveChildDsl = getChildFieldDsl(recursiveChildName, topDsl);
    assertAttributeName(recursiveChildName, recursiveChildDsl);
    assertElementName(hyphenize(recursiveChildName), recursiveChildDsl);
    assertElementNamespace(NAMESPACE, recursiveChildDsl);
    assertChildElementDeclarationIs(true, recursiveChildDsl);
    assertTopLevelDeclarationSupportIs(false, recursiveChildDsl);
    assertIsWrappedElement(false, recursiveChildDsl);

    String simplePojoName = "simplePojo";
    DslElementSyntax simplePojoDsl = getChildFieldDsl(simplePojoName, topDsl);
    assertAttributeName(simplePojoName, simplePojoDsl);
    assertElementName(hyphenize(simplePojoName), simplePojoDsl);
    assertElementNamespace(NAMESPACE, simplePojoDsl);
    assertChildElementDeclarationIs(true, simplePojoDsl);
    assertIsWrappedElement(false, simplePojoDsl);
    assertTopElementDeclarationIs(false, simplePojoDsl);

    String notGlobalName = "notGlobalType";
    DslElementSyntax notGlobalDsl = getChildFieldDsl(notGlobalName, topDsl);
    assertAttributeName(notGlobalName, notGlobalDsl);
    assertElementName(hyphenize(notGlobalName), notGlobalDsl);
    assertElementNamespace(NAMESPACE, notGlobalDsl);
    assertChildElementDeclarationIs(true, notGlobalDsl);
    assertIsWrappedElement(false, notGlobalDsl);

    String groupedField = "groupedField";
    DslElementSyntax groupedFieldDsl = getChildFieldDsl(groupedField, topDsl);
    assertAttributeName(groupedField, groupedFieldDsl);
    assertElementName("", groupedFieldDsl);
    assertElementNamespace("", groupedFieldDsl);
    assertChildElementDeclarationIs(false, groupedFieldDsl);
    assertIsWrappedElement(false, groupedFieldDsl);

    String anotherGroupedField = "anotherGroupedField";
    DslElementSyntax anotherGroupedFieldDsl = getChildFieldDsl(anotherGroupedField, topDsl);
    assertAttributeName(anotherGroupedField, anotherGroupedFieldDsl);
    assertElementName("", anotherGroupedFieldDsl);
    assertElementNamespace("", anotherGroupedFieldDsl);
    assertChildElementDeclarationIs(false, anotherGroupedFieldDsl);
    assertIsWrappedElement(false, anotherGroupedFieldDsl);

    String parameterGroupType = "parameterGroupType";
    assertThat(topDsl.getChild(parameterGroupType).isPresent(), is(false));
  }

  protected DslElementSyntax getGenericTypeDsl(MetadataType itemType, DslElementSyntax result) {
    Optional<DslElementSyntax> genericDsl = result.getGeneric(itemType);
    assertThat("No generic element found for type [" + getTypeId(itemType).orElse("") + "] for element ["
                 + result.getElementName() + "]",
               genericDsl.isPresent(), is(true));

    return genericDsl.get();
  }

  protected DslElementSyntax getChildFieldDsl(String name, DslElementSyntax parent) {
    Optional<DslElementSyntax> childDsl = parent.getChild(name);
    assertThat("No child element found with name [" + name + "] for element [" + parent.getElementName() + "]",
               childDsl.isPresent(), is(true));

    return childDsl.get();
  }

}
