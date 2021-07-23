/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.api.util.ExtensionModelTestUtils.visitableMock;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.nested.NestedComponentModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ComposableXmlDeclarationTestCase extends BaseXmlDeclarationTestCase {

  private NestedComponentModel firstNestedComponentModel = mock(NestedComponentModel.class);
  private NestedComponentModel secondNestedComponentModel = mock(NestedComponentModel.class);
  private NestedComponentModel thirdNestedComponentModel = mock(NestedComponentModel.class);

  private NestedComponentModel innerNestedComponentModel = mock(NestedComponentModel.class);


  public ComposableXmlDeclarationTestCase(ParameterRole role) {
    super(role);
  }

  @Before
  public void mockComposableModels() {
    List operationNestedComponents = new ArrayList<>();
    operationNestedComponents.add(firstNestedComponentModel);
    operationNestedComponents.add(secondNestedComponentModel);
    operationNestedComponents.add(thirdNestedComponentModel);
    visitableMock(operation);
    when(operation.getNestedComponents()).thenReturn(operationNestedComponents);
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(String.class));

    mockFirstNestedComponent();
    mockSecondNestedComponent();
    mockThirdNestedComponent();
    mockInnerNesterComponent();
  }

  private void mockFirstNestedComponent() {
    mockNestedComponent("first-nested", firstNestedComponentModel, 0, 200);

    List innerNestedComponents = new ArrayList();
    innerNestedComponents.add(innerNestedComponentModel);
    when(firstNestedComponentModel.getNestedComponents()).thenReturn(innerNestedComponents);
  }

  private void mockSecondNestedComponent() {
    mockNestedComponent("second-nested", secondNestedComponentModel, 3, null);
  }

  private void mockThirdNestedComponent() {
    mockNestedComponent("third-nested", thirdNestedComponentModel, 0, null);
  }

  private void mockInnerNesterComponent() {
    mockNestedComponent("inner-nested", innerNestedComponentModel, 2, 5);

    ParameterModel parameterModelMock = mock(ParameterModel.class);
    ParameterGroupModel parameterGroupModelMock = mock(ParameterGroupModel.class);
    when(parameterModelMock.getName()).thenReturn("inlineParameterName");
    when(parameterModelMock.getType()).thenReturn(TYPE_LOADER.load(String.class));
    when(parameterModelMock.getExpressionSupport()).thenReturn(ExpressionSupport.SUPPORTED);
    when(parameterModelMock.getModelProperty(any())).thenReturn(empty());
    when(parameterModelMock.getDslConfiguration()).thenReturn(ParameterDslConfiguration.getDefaultInstance());
    when(parameterModelMock.getLayoutModel()).thenReturn(empty());
    when(parameterModelMock.getRole()).thenReturn(role);

    when(parameterGroupModelMock.getName()).thenReturn("InlineParameterGroup");
    when(parameterGroupModelMock.isShowInDsl()).thenReturn(true);
    when(parameterGroupModelMock.getParameterModels()).thenReturn(asList(parameterModel));


    when(innerNestedComponentModel.getParameterGroupModels()).thenReturn(asList(parameterGroupModelMock));
    when(innerNestedComponentModel.getAllParameterModels()).thenReturn(asList(parameterModelMock));
  }

  private void mockNestedComponent(String componentName, NestedComponentModel nestedComponentModelMock, int minOccurs,
                                   Integer maxOccurs) {
    when(nestedComponentModelMock.getName()).thenReturn(componentName);
    when(nestedComponentModelMock.getParameterGroupModels()).thenReturn(asList(parameterGroupModel));
    when(nestedComponentModelMock.getAllParameterModels()).thenReturn(asList(parameterModel));
    when(nestedComponentModelMock.getMinOccurs()).thenReturn(minOccurs);
    when(nestedComponentModelMock.getMaxOccurs()).thenReturn(ofNullable(maxOccurs));
    when(nestedComponentModelMock.isRequired()).thenReturn(minOccurs != 0);
    visitableMock(nestedComponentModelMock);
  }

  @Test
  public void nestedComposableComponents() {
    DslElementSyntax result = getSyntaxResolver().resolve(operation);
    List<String> childNames = result.getChilds().stream().map(DslElementSyntax::getElementName).collect(Collectors.toList());
    assertThat(childNames, hasItems(firstNestedComponentModel.getName(), secondNestedComponentModel.getName(),
                                    thirdNestedComponentModel.getName()));
  }

  @Test
  public void innerNestedComposableComponent() {
    DslElementSyntax result = getSyntaxResolver().resolve(operation);
  }

  @Test
  public void nestedComposableComponentParameters() {
    DslElementSyntax result = getSyntaxResolver().resolve(operation);
  }

  @Test
  public void innerNestedCompasableComponentInlineParameter() {
    DslElementSyntax result = getSyntaxResolver().resolve(operation);
  }


}
