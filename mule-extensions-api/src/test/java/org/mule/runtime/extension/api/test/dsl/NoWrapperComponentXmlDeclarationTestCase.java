/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.dsl;

import static org.mule.runtime.api.test.util.tck.ExtensionModelTestUtils.visitableMock;

import static java.util.Arrays.asList;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mule.runtime.api.meta.model.nested.NestedComponentModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;
import org.mule.runtime.extension.api.property.NoWrapperModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class NoWrapperComponentXmlDeclarationTestCase extends BaseXmlDeclarationTestCase {

  private NestedComponentModel grandParentComponentModel = mock(NestedComponentModel.class);
  private NestedComponentModel parentComponentModel = mock(NestedComponentModel.class);
  private NestedComponentModel child1ComponentModel = mock(NestedComponentModel.class);
  private NestedComponentModel child2ComponentModel = mock(NestedComponentModel.class);
  private NestedComponentModel anotherGrandParentComponentModel = mock(NestedComponentModel.class);
  private NestedComponentModel anotherParentComponentModel = mock(NestedComponentModel.class);
  private NestedComponentModel anotherChildComponentModel = mock(NestedComponentModel.class);

  public NoWrapperComponentXmlDeclarationTestCase(ParameterRole role) {
    super(role);
  }

  @Before
  public void setup() {
    List operationNestedComponents = new ArrayList<>();
    operationNestedComponents.add(grandParentComponentModel);
    operationNestedComponents.add(anotherGrandParentComponentModel);
    visitableMock(operation);
    when(operation.getNestedComponents()).thenReturn(operationNestedComponents);
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(String.class));

    mockNestedComponent("grand-parent", grandParentComponentModel, 0, 10, parentComponentModel);
    mockNestedComponent("parent", parentComponentModel, 0, 10, child1ComponentModel, child2ComponentModel);
    mockNestedComponent("child1", child1ComponentModel, 0, 10);
    mockNestedComponent("child2", child2ComponentModel, 0, 10);

    mockNestedComponent("another-grand-parent", anotherGrandParentComponentModel, 0, 10, anotherParentComponentModel);
    mockNestedComponent("another-parent", anotherParentComponentModel, 0, 10, anotherChildComponentModel);
    mockNestedComponent("another-child", anotherChildComponentModel, 0, 10);
  }

  @Test
  public void testNestedComponentWithNoWrapperModelProperty() {
    // this component model will not be included on the Dsl Syntax tree
    when(parentComponentModel.getModelProperty(NoWrapperModelProperty.class)).thenReturn(of(NoWrapperModelProperty.INSTANCE));

    DslElementSyntax result = getSyntaxResolver().resolve(operation);
    List<String> grandparents = result.getChilds().stream().map(DslElementSyntax::getElementName).collect(Collectors.toList());
    assertThat(grandparents, hasItems(grandParentComponentModel.getName(), anotherGrandParentComponentModel.getName()));

    DslElementSyntax grandParentDsl = result.getChild(grandParentComponentModel.getName()).get();
    DslElementSyntax anotherGrandParentDsl = result.getChild(anotherGrandParentComponentModel.getName()).get();

    // verify that syntax for the original parent, with the NoWrapper model property, is not present on the DSL syntax tree
    assertThat(grandParentDsl.getChild(parentComponentModel.getName()).isPresent(), is(false));
    // verify that original children are now children of the grandparent on the Dsl Syntax tree
    assertThat(grandParentDsl.getChild(child1ComponentModel.getName()).isPresent(), is(true));
    assertThat(grandParentDsl.getChild(child2ComponentModel.getName()).isPresent(), is(true));

    // verify that the DSL syntax tree was generated as expected for the component models without the model property
    assertThat(anotherGrandParentDsl.getChild(anotherParentComponentModel.getName()).isPresent(), is(true));
    assertThat(anotherGrandParentDsl.getChild(anotherParentComponentModel.getName()).get()
        .getChild(anotherChildComponentModel.getName()).isPresent(), is(true));
  }

  private void mockNestedComponent(String componentName, NestedComponentModel nestedComponentModelMock, int minOccurs,
                                   Integer maxOccurs, NestedComponentModel... children) {
    when(nestedComponentModelMock.getName()).thenReturn(componentName);
    when(nestedComponentModelMock.getParameterGroupModels()).thenReturn(asList(parameterGroupModel));
    when(nestedComponentModelMock.getAllParameterModels()).thenReturn(asList(parameterModel));
    when(nestedComponentModelMock.getMinOccurs()).thenReturn(minOccurs);
    when(nestedComponentModelMock.getMaxOccurs()).thenReturn(ofNullable(maxOccurs));
    when(nestedComponentModelMock.isRequired()).thenReturn(minOccurs != 0);
    List nestedComponentModels = asList(children);
    when(nestedComponentModelMock.getNestedComponents()).thenReturn(nestedComponentModels);
    visitableMock(nestedComponentModelMock);
  }

}
