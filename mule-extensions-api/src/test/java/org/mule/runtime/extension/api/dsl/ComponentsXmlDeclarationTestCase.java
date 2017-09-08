/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.extension.api.dsl.model.ComplexFieldsType;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;

import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ComponentsXmlDeclarationTestCase extends BaseXmlDeclarationTestCase {

  private final static String CONNECTION_PROVIDER_SUFFIX = "connection";
  private final static String CONFIGURATION_SUFFIX = "config";
  private static final String COMPLEX_PARAMETER = "complexParameter";
  private static final String GROUPED_COMPLEX_PARAMETER = "groupedComplexParameter";
  private static final String SIMPLE_PARAMETER = "simpleParameter";
  private static final String INLINE_GROUP = "inlineGroup";

  public ComponentsXmlDeclarationTestCase(ParameterRole role) {
    super(role);
  }

  @Before
  public void enrichParameters() {
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(String.class));

    ParameterModel complexParameterModel =
        mockBehaviourParam(COMPLEX_PARAMETER, ComplexFieldsType.class);

    when(parameterGroupModel.getParameterModels()).thenReturn(asList(parameterModel, complexParameterModel));

    // Setup inline group
    ParameterModel simpleParameterModel =
        mockBehaviourParam(SIMPLE_PARAMETER, Integer.class);

    ParameterModel groupedComplexParameterModel =
        mockBehaviourParam(GROUPED_COMPLEX_PARAMETER, ComplexFieldsType.class);

    ParameterGroupModel inlineGroupModel = mock(ParameterGroupModel.class);
    when(inlineGroupModel.getName()).thenReturn(INLINE_GROUP);
    when(inlineGroupModel.isShowInDsl()).thenReturn(true);
    when(inlineGroupModel.getParameterModels()).thenReturn(asList(simpleParameterModel, groupedComplexParameterModel));

    when(source.getParameterGroupModels()).thenReturn(asList(parameterGroupModel, inlineGroupModel));
    when(operation.getParameterGroupModels()).thenReturn(asList(parameterGroupModel, inlineGroupModel));
    when(configuration.getParameterGroupModels()).thenReturn(asList(parameterGroupModel, inlineGroupModel));
    when(connectionProvider.getParameterGroupModels()).thenReturn(asList(parameterGroupModel, inlineGroupModel));

    Stream.of(configuration, operation, connectionProvider, source).forEach(
                                                                            model -> {
                                                                              when(connectionProvider.getParameterGroupModels())
                                                                                  .thenReturn(asList(parameterGroupModel,
                                                                                                     inlineGroupModel));
                                                                              when(model.getAllParameterModels())
                                                                                  .thenReturn(asList(parameterModel,
                                                                                                     complexParameterModel,
                                                                                                     simpleParameterModel,
                                                                                                     groupedComplexParameterModel));
                                                                            });
  }

  @Test
  public void testOperationDeclaration() {
    DslElementSyntax result = getSyntaxResolver().resolve(operation);

    assertThat(result.getElementName(), is(hyphenize(OPERATION_NAME)));
    assertThat(result.getPrefix(), is(PREFIX));
    assertThat(result.requiresConfig(), is(true));
    assertTopElementDeclarationIs(false, result);
    assertChildElementDeclarationIs(true, result);
    assertAttributeDeclaration(false, result);
    assertIsWrappedElement(false, result);

    assertStringTypeComponentParameter(result);
    assertComplexTypeComponentParameter(result);
    assertInlineParameterGroup(result);
  }

  @Test
  public void testSourceDeclaration() {
    DslElementSyntax result = getSyntaxResolver().resolve(source);

    assertThat(result.getAttributeName(), is(""));
    assertThat(result.getElementName(), is(hyphenize(SOURCE_NAME)));
    assertThat(result.getPrefix(), is(PREFIX));
    assertThat(result.requiresConfig(), is(true));
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);

    assertStringTypeComponentParameter(result);
    assertComplexTypeComponentParameter(result);
    assertInlineParameterGroup(result);
  }

  @Test
  public void testConfigurationDeclaration() {
    DslElementSyntax result = getSyntaxResolver().resolve(configuration);

    assertThat(result.getAttributeName(), is(""));
    assertThat(result.getElementName(), is(CONFIGURATION_NAME + "-" + CONFIGURATION_SUFFIX));
    assertThat(result.getPrefix(), is(PREFIX));
    assertThat(result.requiresConfig(), is(false));
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);

    assertStringTypeComponentParameter(result);
    assertComplexTypeComponentParameter(result);
    assertInlineParameterGroup(result);
  }

  @Test
  public void testConfigurationDeclarationWithConfigSuffix() {
    when(configuration.getName()).thenReturn(CONFIGURATION_NAME + "Config");
    testConfigurationDeclaration();
  }

  @Test
  public void testDefaultConfigurationDeclarationWithConfigSuffix() {
    String defaultName = "config";
    when(configuration.getName()).thenReturn(defaultName);
    DslElementSyntax result = getSyntaxResolver().resolve(configuration);
    assertThat(result.getElementName(), is(defaultName));
  }

  @Test
  public void testConnectionProviderDeclarationWithConnectionSuffix() {
    when(connectionProvider.getName()).thenReturn(CONNECTION_PROVIDER_NAME + "Connection");
    testConnectionProviderDeclaration();
  }

  @Test
  public void testDefaultConnectionProviderDeclarationWithConnectionSuffix() {
    String defaultName = "connection";
    when(connectionProvider.getName()).thenReturn(defaultName);
    DslElementSyntax result = getSyntaxResolver().resolve(connectionProvider);
    assertThat(result.getElementName(), is(defaultName));
  }

  @Test
  public void testConnectionProviderDeclaration() {
    DslElementSyntax result = getSyntaxResolver().resolve(connectionProvider);

    assertThat(result.getAttributeName(), is(""));
    assertThat(result.getElementName(), is(CONNECTION_PROVIDER_NAME + "-" + CONNECTION_PROVIDER_SUFFIX));
    assertThat(result.getPrefix(), is(PREFIX));
    assertThat(result.requiresConfig(), is(false));
    assertChildElementDeclarationIs(true, result);
    assertIsWrappedElement(false, result);

    assertStringTypeComponentParameter(result);
    assertComplexTypeComponentParameter(result);
    assertInlineParameterGroup(result);
  }

  @Test
  public void connectedOperation() {
    when(operation.requiresConnection()).thenReturn(true);
    DslElementSyntax result = getSyntaxResolver().resolve(operation);

    assertThat(result.requiresConfig(), is(true));
  }

  private void assertStringTypeComponentParameter(DslElementSyntax result) {

    ifContentParameter(() -> assertThat(result.getChild(PARAMETER_NAME).isPresent(), is(true)),
                       () -> {
                         DslElementSyntax childDsl = getAttributeDsl(PARAMETER_NAME, result);
                         assertAttributeName(PARAMETER_NAME, childDsl);
                         assertParameterChildElementDeclaration(false, childDsl);
                         assertIsWrappedElement(false, childDsl);
                       });
  }

  private void assertInlineParameterGroup(DslElementSyntax result) {
    assertThat(result.getAttribute(INLINE_GROUP).isPresent(), is(false));

    DslElementSyntax inlineDsl = getChildFieldDsl(INLINE_GROUP, result);

    assertThat(inlineDsl.getElementName(), is(hyphenize(INLINE_GROUP)));
    assertThat(inlineDsl.getPrefix(), is(PREFIX));
    assertChildElementDeclarationIs(true, inlineDsl);
    assertTopElementDeclarationIs(false, inlineDsl);
    assertAttributeDeclaration(false, inlineDsl);
    assertIsWrappedElement(false, inlineDsl);

    DslElementSyntax attributeDsl = getAttributeDsl(SIMPLE_PARAMETER, inlineDsl);
    assertThat(result.getChild(SIMPLE_PARAMETER).isPresent(), is(false));
    assertAttributeName(SIMPLE_PARAMETER, attributeDsl);
    assertElementName("", attributeDsl);
    assertElementPrefix("", attributeDsl);
    assertChildElementDeclarationIs(false, attributeDsl);
    assertIsWrappedElement(false, attributeDsl);
    assertNoAttributes(attributeDsl);
    assertNoChilds(attributeDsl);

    DslElementSyntax complexAttributeDsl = getAttributeDsl(GROUPED_COMPLEX_PARAMETER, inlineDsl);
    assertAttributeName(GROUPED_COMPLEX_PARAMETER, complexAttributeDsl);

    DslElementSyntax childDsl = getChildFieldDsl(GROUPED_COMPLEX_PARAMETER, inlineDsl);
    assertComplexTypeDslFields(childDsl);
  }

  private void assertComplexTypeComponentParameter(DslElementSyntax result) {
    DslElementSyntax attributeDsl = getAttributeDsl(COMPLEX_PARAMETER, result);
    assertAttributeName(COMPLEX_PARAMETER, attributeDsl);

    DslElementSyntax childDsl = getChildFieldDsl(COMPLEX_PARAMETER, result);
    assertComplexTypeDslFields(childDsl);
  }

}
