/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.dsl;

import static org.mule.runtime.api.test.util.tck.ExtensionModelTestUtils.visitableMock;
import static org.mule.runtime.api.util.NameUtils.hyphenize;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.nested.NestedComponentModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ComposableXmlDeclarationTestCase extends BaseXmlDeclarationTestCase {

  private static final String INLINE_PARAMETER_NAME = "inlineParameterName";
  private static final String PARAMETER_GROUP_SHOW_IN_DSL_NAME = "InlineParameterGroup";

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
    when(parameterModelMock.getName()).thenReturn(INLINE_PARAMETER_NAME);
    when(parameterModelMock.getType()).thenReturn(TYPE_LOADER.load(String.class));
    when(parameterModelMock.getExpressionSupport()).thenReturn(ExpressionSupport.SUPPORTED);
    when(parameterModelMock.getModelProperty(any())).thenReturn(empty());
    when(parameterModelMock.getDslConfiguration()).thenReturn(ParameterDslConfiguration.getDefaultInstance());
    when(parameterModelMock.getLayoutModel()).thenReturn(empty());
    when(parameterModelMock.getRole()).thenReturn(ParameterRole.BEHAVIOUR);

    when(parameterGroupModelMock.getName()).thenReturn(PARAMETER_GROUP_SHOW_IN_DSL_NAME);
    when(parameterGroupModelMock.isShowInDsl()).thenReturn(true);
    when(parameterGroupModelMock.getParameterModels()).thenReturn(asList(parameterModelMock));


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
    Optional<DslElementSyntax> innerNested =
        result.getChild(firstNestedComponentModel.getName()).get().getChild(innerNestedComponentModel.getName());
    assertThat(innerNested.isPresent(), is(true));
    assertThat(innerNested.get().getElementName(), is(innerNestedComponentModel.getName()));
  }

  @Test
  public void nestedComposableComponentParameters() {
    DslElementSyntax result = getSyntaxResolver().resolve(operation);
    List<DslElementSyntax> firstNestedComponentChildren = new ArrayList<>();
    firstNestedComponentChildren.add(result.getChild(firstNestedComponentModel.getName()).get());
    firstNestedComponentChildren.add(result.getChild(secondNestedComponentModel.getName()).get());
    firstNestedComponentChildren.add(result.getChild(thirdNestedComponentModel.getName()).get());

    for (DslElementSyntax child : firstNestedComponentChildren) {
      if (parameterModel.getRole().equals(ParameterRole.BEHAVIOUR)) {
        Optional<DslElementSyntax> parameterDslSyntax = child.getAttribute(parameterModel.getName());
        assertThat(parameterDslSyntax.isPresent(), is(true));
        assertThat(parameterDslSyntax.get().getAttributeName(), is((parameterModel.getName())));
      } else {
        Optional<DslElementSyntax> parameterDslSyntax = child.getChild(parameterModel.getName());
        assertThat(parameterDslSyntax.isPresent(), is(true));
        assertThat(parameterDslSyntax.get().getElementName(), is((hyphenize(parameterModel.getName()))));
      }
    }
  }

  @Test
  public void innerNestedCompasableComponentInlineParameter() {
    DslElementSyntax result = getSyntaxResolver().resolve(operation);
    Optional<DslElementSyntax> firstNestedComponentModelSyntax = result.getChild(firstNestedComponentModel.getName());
    assertThat(firstNestedComponentModelSyntax.isPresent(), is(true));
    Optional<DslElementSyntax> innerNestedComponentModelSyntax =
        firstNestedComponentModelSyntax.get().getChild(innerNestedComponentModel.getName());
    assertThat(innerNestedComponentModelSyntax.isPresent(), is(true));
    Optional<DslElementSyntax> inlineParameterGroupSyntax =
        innerNestedComponentModelSyntax.get().getChild(PARAMETER_GROUP_SHOW_IN_DSL_NAME);
    assertThat(inlineParameterGroupSyntax.isPresent(), is(true));
    Optional<DslElementSyntax> parameterInInlineGroupSyntax =
        inlineParameterGroupSyntax.get().getAttribute(INLINE_PARAMETER_NAME);
    assertThat(parameterInInlineGroupSyntax.isPresent(), is(true));
    assertThat(parameterInInlineGroupSyntax.get().getAttributeName(), is(INLINE_PARAMETER_NAME));
  }

}
