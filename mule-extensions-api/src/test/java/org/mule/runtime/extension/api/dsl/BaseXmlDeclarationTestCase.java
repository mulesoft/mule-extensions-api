/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Optional.empty;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.CONTENT;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.PRIMARY_CONTENT;
import static org.mule.runtime.api.util.ExtensionModelTestUtils.visitableMock;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.isContent;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import static org.mule.runtime.extension.api.util.NameUtils.singularize;
import static org.mule.runtime.internal.dsl.DslConstants.VALUE_ATTRIBUTE_NAME;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.annotation.TypeIdAnnotation;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.SubTypesModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.type.TypeCatalog;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.dsl.model.ExtensibleType;
import org.mule.runtime.extension.api.dsl.model.SubType;
import org.mule.runtime.extension.api.dsl.model.SuperType;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DslSyntaxResolver;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseXmlDeclarationTestCase {

  static final String PREFIX = "mockns";
  static final String NAMESPACE = "http://www.mulesoft.org/schema/mule/mockns";
  static final String SCHEMA_LOCATION = "http://www.mulesoft.org/schema/mule/mockns/current/mule-mockns.xsd";
  static final String PARAMETER_NAME = "myCamelCaseName";
  static final String COLLECTION_NAME = "myCamelCaseNames";
  static final String SINGULARIZABLE_PARAMETER_NAME = "singularizableNames";
  static final String EXTENSION_NAME = "extension";
  static final String OPERATION_NAME = "mockOperation";
  static final String SOURCE_NAME = "source";
  static final String CONFIGURATION_NAME = "test";
  static final String CONNECTION_PROVIDER_NAME = "test";
  static final BaseTypeBuilder TYPE_BUILDER = BaseTypeBuilder.create(JAVA);
  static final String EXTENSIBLE_TYPE_LIST_NAME = "extensibleTypeList";
  static final String IMPORT_PREFIX = "importns";
  static final String IMPORT_NAMESPACE = "http://www.mulesoft.org/schema/mule/importns";
  static final String IMPORT_SCHEMA_LOCATION = "http://www.mulesoft.org/schema/mule/importns/current/mule-importns.xsd";
  static final String IMPORT_WITH_XML_SCHEMA_LOCATION =
      "http://www.mulesoft.org/schema/mule/importns/current/mule-import-extension-with-xml.xsd";

  static final String IMPORT_EXTENSION_NAME = "importExtension";
  static final String IMPORT_EXTENSION_NAME_WITH_XML = "importExtensionWithXml";

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        {BEHAVIOUR}, {CONTENT}, {PRIMARY_CONTENT}
    });
  }

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
  protected ParameterGroupModel parameterGroupModel;

  @Mock(answer = RETURNS_DEEP_STUBS)
  protected SourceModel source;

  @Mock
  protected DslResolvingContext dslContext;

  @Mock
  protected TypeCatalog typeCatalog;


  protected final ParameterRole role;
  protected ClassTypeLoader TYPE_LOADER = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
  protected SubTypesModel subTypesModel =
      new SubTypesModel(TYPE_LOADER.load(SuperType.class), singleton(TYPE_LOADER.load(SubType.class)));

  public BaseXmlDeclarationTestCase(ParameterRole role) {
    this.role = role;
  }

  @Before
  public void before() {
    initMocks(this);
    when(extension.getName()).thenReturn(EXTENSION_NAME);
    when(extension.getXmlDslModel()).thenReturn(createXmlDslModel());
    when(extension.getConfigurationModels()).thenReturn(asList(configuration));
    when(extension.getOperationModels()).thenReturn(asList(operation));
    when(extension.getSourceModels()).thenReturn(asList(source));
    when(extension.getConnectionProviders()).thenReturn(asList(connectionProvider));

    when(extension.getSubTypes()).thenReturn(singleton(subTypesModel));
    when(extension.getImportedTypes()).thenReturn(emptySet());
    when(extension.getXmlDslModel()).thenReturn(XmlDslModel.builder()
        .setXsdFileName(EMPTY)
        .setPrefix(PREFIX)
        .setNamespace(NAMESPACE)
        .setSchemaLocation(SCHEMA_LOCATION)
        .setSchemaVersion(EMPTY)
        .build());

    when(configuration.getOperationModels()).thenReturn(asList(operation));
    when(configuration.getSourceModels()).thenReturn(asList(source));
    when(configuration.getConnectionProviders()).thenReturn(asList(connectionProvider));

    when(parameterModel.getName()).thenReturn(PARAMETER_NAME);
    when(parameterModel.getExpressionSupport()).thenReturn(ExpressionSupport.SUPPORTED);
    when(parameterModel.getModelProperty(any())).thenReturn(empty());
    when(parameterModel.getDslConfiguration()).thenReturn(ParameterDslConfiguration.getDefaultInstance());
    when(parameterModel.getLayoutModel()).thenReturn(empty());
    when(parameterModel.getRole()).thenReturn(role);

    when(parameterGroupModel.getName()).thenReturn("GENERAL");
    when(parameterGroupModel.isShowInDsl()).thenReturn(false);
    when(parameterGroupModel.getParameterModels()).thenReturn(asList(parameterModel));

    when(source.getName()).thenReturn(SOURCE_NAME);
    when(source.getParameterGroupModels()).thenReturn(asList(parameterGroupModel));
    when(source.getSuccessCallback()).thenReturn(empty());
    when(source.getErrorCallback()).thenReturn(empty());

    when(operation.getName()).thenReturn(OPERATION_NAME);
    when(operation.getParameterGroupModels()).thenReturn(asList(parameterGroupModel));

    visitableMock(operation, source);

    when(configuration.getName()).thenReturn(CONFIGURATION_NAME);
    when(configuration.getParameterGroupModels()).thenReturn(asList(parameterGroupModel));

    when(connectionProvider.getName()).thenReturn(CONNECTION_PROVIDER_NAME);
    when(connectionProvider.getParameterGroupModels()).thenReturn(asList(parameterGroupModel));

    when(typeCatalog.getSubTypes(any())).thenReturn(emptySet());
    when(typeCatalog.getSuperTypes(any())).thenReturn(emptySet());
    when(typeCatalog.getAllBaseTypes()).thenReturn(emptySet());
    when(typeCatalog.getAllSubTypes()).thenReturn(emptySet());
    when(typeCatalog.getTypes()).thenReturn(emptySet());
    when(typeCatalog.getType(any())).thenReturn(empty());
    when(typeCatalog.containsBaseType(any())).thenReturn(false);

    when(dslContext.getExtension(any())).thenReturn(Optional.of(extension));
    when(dslContext.getExtensions()).thenReturn(singleton(extension));
    when(dslContext.getTypeCatalog()).thenReturn(typeCatalog);

    Stream.of(configuration, operation, connectionProvider, source).forEach(
                                                                            model -> when(model.getAllParameterModels())
                                                                                .thenReturn(asList(parameterModel)));
  }

  void assertParameterChildElementDeclaration(boolean expected, DslElementSyntax result) {
    if (isContent(parameterModel)) {
      expected = true;
    }

    assertChildElementDeclarationIs(expected, result);
  }

  void assertChildElementDeclarationIs(boolean expected, DslElementSyntax result) {
    assertThat(format("Expected 'supportsChildDeclaration' declaration to be %s", expected), result.supportsChildDeclaration(),
               is(expected));
  }

  void assertTopLevelDeclarationSupportIs(boolean expected, DslElementSyntax result) {
    assertThat(format("Expected 'supportsTopLevelDeclaration' declaration to be %s", expected),
               result.supportsTopLevelDeclaration(), is(expected));
  }

  void assertIsWrappedElement(boolean expected, DslElementSyntax result) {
    assertThat(format("Expected 'isWrapped' declaration to be %s", expected), result.isWrapped(), is(expected));
  }

  void assertAttributeDeclaration(boolean expected, DslElementSyntax result) {
    assertThat(format("Expected 'supportsAttributeDeclaration' declaration to be %s", expected),
               result.supportsAttributeDeclaration(), is(expected));
  }

  void assertAttributeName(String expected, DslElementSyntax result) {
    assertAttributeDeclaration(true, result);
    assertThat(result.getAttributeName(), equalTo(expected));
  }

  void assertEmptyAttributeName(DslElementSyntax result) {
    assertThat(result.getAttributeName(), is(""));
  }

  void assertElementName(String expected, DslElementSyntax result) {
    assertThat(result.getElementName(), equalTo(expected));
  }

  void assertElementNamespace(String expected, DslElementSyntax result) {
    assertThat(result.getPrefix(), equalTo(expected));
  }

  void assertTopElementDeclarationIs(boolean expected, DslElementSyntax result) {
    assertThat("Expected the element to support Top Level definitions", result.supportsTopLevelDeclaration(), is(expected));
  }

  DslSyntaxResolver getSyntaxResolver() {
    return DslSyntaxResolver.getDefault(extension, dslContext);
  }

  protected void assertComplexTypeDslFields(DslElementSyntax topDsl) {

    assertExtensibleListParameter(topDsl);

    assertRecursiveChildParameter(topDsl);

    assertComplexTypeFromGroupRecursion(topDsl);

    assertSimplePojoField(topDsl);

    assertNonGlobalTypePojo(topDsl);

    assertGroupedTextField(topDsl);

    assertContentInsideGroup(topDsl);

    assertSkippedGroupFields(topDsl);
  }

  private void assertContentInsideGroup(DslElementSyntax topDsl) {
    String groupedFieldAsContent = "anotherGroupedFieldAsContent";
    DslElementSyntax anotherGroupedFieldDsl = getChildFieldDsl(groupedFieldAsContent, topDsl);
    assertThat(topDsl.getAttribute(groupedFieldAsContent).isPresent(), is(false));
    assertElementName(hyphenize(groupedFieldAsContent), anotherGroupedFieldDsl);
    assertElementNamespace(PREFIX, anotherGroupedFieldDsl);
    assertChildElementDeclarationIs(true, anotherGroupedFieldDsl);
    assertIsWrappedElement(false, anotherGroupedFieldDsl);
    assertNoAttributes(anotherGroupedFieldDsl);
    assertNoChilds(anotherGroupedFieldDsl);
  }

  private void assertSkippedGroupFields(DslElementSyntax topDsl) {
    String parameterGroupType = "parameterGroupType";
    assertThat(topDsl.getChild(parameterGroupType).isPresent(), is(false));
    assertThat(topDsl.getAttribute(parameterGroupType).isPresent(), is(false));

    String complexTypeFieldGroup = "complexTypeFieldGroup";
    assertThat(topDsl.getChild(complexTypeFieldGroup).isPresent(), is(false));
    assertThat(topDsl.getAttribute(complexTypeFieldGroup).isPresent(), is(false));
  }

  private void assertGroupedTextField(DslElementSyntax topDsl) {
    String groupedField = "groupedField";
    assertThat(topDsl.getAttribute(groupedField).isPresent(), is(false));
    DslElementSyntax groupedFieldDsl = getChildFieldDsl(groupedField, topDsl);
    assertElementName(hyphenize(groupedField), groupedFieldDsl);
    assertElementNamespace(PREFIX, groupedFieldDsl);
    assertChildElementDeclarationIs(true, groupedFieldDsl);
    assertIsWrappedElement(false, groupedFieldDsl);
  }

  private void assertNonGlobalTypePojo(DslElementSyntax topDsl) {
    String notGlobalName = "notGlobalType";
    DslElementSyntax notGlobalAttrDsl = getAttributeDsl(notGlobalName, topDsl);
    assertAttributeDeclaration(true, notGlobalAttrDsl);
    assertAttributeName(notGlobalName, notGlobalAttrDsl);

    DslElementSyntax notGlobalDsl = getChildFieldDsl(notGlobalName, topDsl);
    assertElementName(hyphenize(notGlobalName), notGlobalDsl);
    assertElementNamespace(PREFIX, notGlobalDsl);
    assertChildElementDeclarationIs(true, notGlobalDsl);
    assertIsWrappedElement(false, notGlobalDsl);
  }

  private void assertSimplePojoField(DslElementSyntax topDsl) {

    String simplePojoName = "simplePojo";
    DslElementSyntax simplePojoAttrDsl = getAttributeDsl(simplePojoName, topDsl);
    assertAttributeDeclaration(true, simplePojoAttrDsl);
    assertAttributeName(simplePojoName, simplePojoAttrDsl);

    DslElementSyntax simplePojoDsl = getChildFieldDsl(simplePojoName, topDsl);
    assertElementName(hyphenize(simplePojoName), simplePojoDsl);
    assertElementNamespace(PREFIX, simplePojoDsl);
    assertChildElementDeclarationIs(true, simplePojoDsl);
    assertIsWrappedElement(false, simplePojoDsl);
    assertTopElementDeclarationIs(false, simplePojoDsl);

    // Verify contained elements
    String sampleStringName = "sampleString";
    assertThat(topDsl.getChild(sampleStringName).isPresent(), is(false));
    DslElementSyntax sampleStringAttrDsl = getAttributeDsl(sampleStringName, simplePojoDsl);
    assertAttributeName(sampleStringName, sampleStringAttrDsl);
    assertElementName("", sampleStringAttrDsl);
    assertElementNamespace("", sampleStringAttrDsl);
    assertChildElementDeclarationIs(false, sampleStringAttrDsl);
    assertIsWrappedElement(false, sampleStringAttrDsl);
    assertNoAttributes(sampleStringAttrDsl);
    assertNoChilds(sampleStringAttrDsl);

    String otherNumberName = "otherNumber";
    assertThat(topDsl.getChild(otherNumberName).isPresent(), is(false));
    DslElementSyntax otherNumberAttrDsl = getAttributeDsl(otherNumberName, simplePojoDsl);
    assertAttributeName(otherNumberName, otherNumberAttrDsl);
    assertElementName("", otherNumberAttrDsl);
    assertElementNamespace("", otherNumberAttrDsl);
    assertChildElementDeclarationIs(false, otherNumberAttrDsl);
    assertIsWrappedElement(false, otherNumberAttrDsl);
    assertNoAttributes(otherNumberAttrDsl);
    assertNoChilds(otherNumberAttrDsl);

    String textFieldName = "textField";
    assertThat(topDsl.getAttribute(textFieldName).isPresent(), is(false));
    DslElementSyntax textFieldDsl = getChildFieldDsl(textFieldName, simplePojoDsl);
    assertAttributeDeclaration(false, textFieldDsl);
    assertElementName(hyphenize(textFieldName), textFieldDsl);
    assertElementNamespace(PREFIX, textFieldDsl);
    assertChildElementDeclarationIs(true, textFieldDsl);
    assertIsWrappedElement(false, textFieldDsl);
    assertTopElementDeclarationIs(false, textFieldDsl);
    assertNoAttributes(textFieldDsl);
    assertNoChilds(textFieldDsl);
  }

  protected void assertNoChilds(DslElementSyntax textFieldDsl) {
    assertThat(textFieldDsl.getChilds().isEmpty(), is(true));
  }

  private void assertComplexTypeFromGroupRecursion(DslElementSyntax topDsl) {
    String recursiveFromGroupName = "complexFieldsType";
    DslElementSyntax recursiveFromGroupAttrDsl = getAttributeDsl(recursiveFromGroupName, topDsl);
    assertAttributeDeclaration(true, recursiveFromGroupAttrDsl);
    assertAttributeName(recursiveFromGroupName, recursiveFromGroupAttrDsl);

    DslElementSyntax recursiveFromGroupDsl = getChildFieldDsl(recursiveFromGroupName, topDsl);
    assertElementName(hyphenize(recursiveFromGroupName), recursiveFromGroupDsl);
    assertElementNamespace(PREFIX, recursiveFromGroupDsl);
    assertChildElementDeclarationIs(true, recursiveFromGroupDsl);
    assertTopLevelDeclarationSupportIs(false, recursiveFromGroupDsl);
    assertIsWrappedElement(false, recursiveFromGroupDsl);
  }

  private void assertExtensibleListParameter(DslElementSyntax topDsl) {
    DslElementSyntax listAttrDsl = getAttributeDsl(EXTENSIBLE_TYPE_LIST_NAME, topDsl);
    assertAttributeDeclaration(true, listAttrDsl);
    assertAttributeName(EXTENSIBLE_TYPE_LIST_NAME, listAttrDsl);

    DslElementSyntax listDsl = getChildFieldDsl(EXTENSIBLE_TYPE_LIST_NAME, topDsl);
    assertElementName(hyphenize(EXTENSIBLE_TYPE_LIST_NAME), listDsl);
    assertElementNamespace(PREFIX, listDsl);
    assertChildElementDeclarationIs(true, listDsl);
    assertIsWrappedElement(false, listDsl);

    MetadataType listItemType = TYPE_LOADER.load(ExtensibleType.class);
    DslElementSyntax listItemDsl = getGenericTypeDsl(listItemType, listDsl);
    assertElementName(getTopLevelTypeName(listItemType), listItemDsl);
    assertElementNamespace(PREFIX, listItemDsl);
    assertChildElementDeclarationIs(true, listItemDsl);
    assertTopElementDeclarationIs(false, listItemDsl);
    assertIsWrappedElement(true, listItemDsl);
    assertNoAttributes(listItemDsl);
    assertNoChilds(listItemDsl);
  }

  protected void assertExtensibleTypeDslStructure(DslElementSyntax extensibleTypeDsl) {
    MetadataType type = TYPE_LOADER.load(ExtensibleType.class);
    assertElementName(getTopLevelTypeName(type), extensibleTypeDsl);
    assertChildElementDeclarationIs(true, extensibleTypeDsl);
    assertTopElementDeclarationIs(false, extensibleTypeDsl);
    assertIsWrappedElement(true, extensibleTypeDsl);

    String sampleStringName = "sampleString";
    assertThat(extensibleTypeDsl.getChild(sampleStringName).isPresent(), is(false));
    DslElementSyntax sampleStringAttrDsl = getAttributeDsl(sampleStringName, extensibleTypeDsl);
    assertAttributeName(sampleStringName, sampleStringAttrDsl);
    assertElementName("", sampleStringAttrDsl);
    assertElementNamespace("", sampleStringAttrDsl);
    assertChildElementDeclarationIs(false, sampleStringAttrDsl);
    assertIsWrappedElement(false, sampleStringAttrDsl);
    assertNoAttributes(sampleStringAttrDsl);
    assertThat(sampleStringAttrDsl.getChilds().isEmpty(), is(true));

    String otherNumberName = "otherNumber";
    assertThat(extensibleTypeDsl.getChild(otherNumberName).isPresent(), is(false));
    DslElementSyntax otherNumberAttrDsl = getAttributeDsl(otherNumberName, extensibleTypeDsl);
    assertAttributeName(otherNumberName, otherNumberAttrDsl);
    assertElementName("", otherNumberAttrDsl);
    assertElementNamespace("", otherNumberAttrDsl);
    assertChildElementDeclarationIs(false, otherNumberAttrDsl);
    assertIsWrappedElement(false, otherNumberAttrDsl);
    assertThat(otherNumberAttrDsl.getAttributes().isEmpty(), is(true));
    assertThat(otherNumberAttrDsl.getChilds().isEmpty(), is(true));

    String childNumbersName = "childNumbers";
    DslElementSyntax childNumbersAttrDsl = getAttributeDsl(childNumbersName, extensibleTypeDsl);
    assertAttributeName(childNumbersName, childNumbersAttrDsl);

    DslElementSyntax childNumbersListDsl = getChildFieldDsl(childNumbersName, extensibleTypeDsl);
    assertElementName(hyphenize(childNumbersName), childNumbersListDsl);
    assertChildElementDeclarationIs(true, childNumbersListDsl);
    assertIsWrappedElement(false, childNumbersListDsl);
    assertNoAttributes(childNumbersListDsl);
    assertNoChilds(childNumbersListDsl);

    MetadataType listItemType = TYPE_LOADER.load(Integer.class);
    DslElementSyntax listItemDsl = getGenericTypeDsl(listItemType, childNumbersListDsl);
    assertElementName(hyphenize(singularize(childNumbersName)), listItemDsl);
    assertAttributeDeclaration(false, listItemDsl);
    assertChildElementDeclarationIs(true, listItemDsl);
    assertIsWrappedElement(false, listItemDsl);
    getAttributeDsl(VALUE_ATTRIBUTE_NAME, listItemDsl);
    assertNoChilds(listItemDsl);

  }

  protected void assertNoAttributes(DslElementSyntax otherNumberListDsl) {
    assertThat(otherNumberListDsl.getAttributes().isEmpty(), is(true));
  }

  private void assertRecursiveChildParameter(DslElementSyntax topDsl) {
    String recursiveChildName = "recursiveChild";
    DslElementSyntax recursiveAttrChildDsl = getAttributeDsl(recursiveChildName, topDsl);
    assertAttributeDeclaration(true, recursiveAttrChildDsl);
    assertAttributeName(recursiveChildName, recursiveAttrChildDsl);

    DslElementSyntax recursiveChildDsl = getChildFieldDsl(recursiveChildName, topDsl);
    assertElementName(hyphenize(recursiveChildName), recursiveChildDsl);
    assertElementNamespace(PREFIX, recursiveChildDsl);
    assertChildElementDeclarationIs(true, recursiveChildDsl);
    assertTopLevelDeclarationSupportIs(false, recursiveChildDsl);
    assertIsWrappedElement(false, recursiveChildDsl);
  }

  protected DslElementSyntax getGenericTypeDsl(MetadataType itemType, DslElementSyntax result) {
    Optional<DslElementSyntax> genericDsl = result.getGeneric(itemType);
    assertThat("No generic element found for type [" + getId(itemType).orElse("") + "] for element ["
        + result.getElementName() + "]",
               genericDsl.isPresent(), is(true));

    return genericDsl.get();
  }

  protected DslElementSyntax getAttributeDsl(String name, DslElementSyntax parent) {
    Optional<DslElementSyntax> childDsl = parent.getAttribute(name);
    assertThat("No attribute element found with name [" + name + "] for element [" + parent.getElementName() + "]",
               childDsl.isPresent(), is(true));

    return childDsl.get();
  }

  protected DslElementSyntax getChildFieldDsl(String name, DslElementSyntax parent) {
    Optional<DslElementSyntax> childDsl = parent.getChild(name);
    assertThat("No child element found with name [" + name + "] for element [" + parent.getElementName() + "]",
               childDsl.isPresent(), is(true));

    return childDsl.get();
  }

  protected void mockImportedTypes(ExtensionModel extensionModel, String originExtension, Class<?> type) {
    ObjectType metadataType = (ObjectType) TYPE_LOADER.load(type);
    String typeId = metadataType.getAnnotation(TypeIdAnnotation.class).get().getValue();
    when(extension.getImportedTypes()).thenReturn(singleton(new ImportedTypeModel(metadataType)));
    when(typeCatalog.getDeclaringExtension(typeId)).thenReturn(Optional.of(originExtension));
    when(dslContext.getExtensionForType(typeId)).thenReturn(Optional.of(extensionModel));
  }

  protected void ifContentParameter(Runnable test, Runnable orElse) {
    if (isContent(parameterModel)) {
      test.run();
    } else {
      orElse.run();
    }
  }

  protected XmlDslModel createXmlDslModel() {
    return XmlDslModel.builder()
        .setXsdFileName("mule-mockns.xsd")
        .setPrefix(PREFIX)
        .setNamespace(NAMESPACE)
        .setSchemaLocation(SCHEMA_LOCATION)
        .setSchemaVersion("4.0")
        .build();
  }

  protected XmlDslModel createImportedXmlDslModel() {
    return XmlDslModel.builder()
        .setXsdFileName("mule-importns.xsd")
        .setPrefix(IMPORT_PREFIX)
        .setNamespace(IMPORT_NAMESPACE)
        .setSchemaLocation(IMPORT_SCHEMA_LOCATION)
        .setSchemaVersion("4.0")
        .build();
  }

  protected ParameterModel mockBehaviourParam(String name, Class<?> clazz) {
    ParameterModel model = mock(ParameterModel.class);
    when(model.getName()).thenReturn(name);
    when(model.getExpressionSupport()).thenReturn(ExpressionSupport.SUPPORTED);
    when(model.getModelProperty(any())).thenReturn(empty());
    when(model.getDslConfiguration()).thenReturn(ParameterDslConfiguration.getDefaultInstance());
    when(model.getLayoutModel()).thenReturn(empty());
    when(model.getRole()).thenReturn(BEHAVIOUR);
    when(model.getType()).thenReturn(TYPE_LOADER.load(clazz));
    return model;
  }


  @Xml(prefix = IMPORT_PREFIX, namespace = IMPORT_NAMESPACE)
  @Extension(name = IMPORT_EXTENSION_NAME_WITH_XML)
  protected static final class ExtensionForImportsDeclaresXml {

  }


  @Extension(name = IMPORT_EXTENSION_NAME)
  protected static final class ExtensionForImportsNoXml {

  }
}
