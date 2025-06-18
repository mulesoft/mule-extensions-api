/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.dsl;

import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;

import static java.util.Collections.singletonList;
import static java.util.Optional.of;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.mule.runtime.api.meta.model.ComponentModelVisitor;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.nested.NestedRouteModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;
import org.mule.runtime.extension.api.property.ListOfRoutesModelProperty;

import java.util.Optional;

import org.junit.Test;

public class ListOfRoutesXmlDeclarationTestCase extends BaseXmlDeclarationTestCase {

  public ListOfRoutesXmlDeclarationTestCase() {
    super(BEHAVIOUR);
  }

  @Test
  public void visitNestedRoute() {
    OperationModel routerModel = mock(OperationModel.class);
    when(routerModel.getName()).thenReturn("listOfRoutesRouter");

    NestableElementModel nestable = mock(NestableElementModel.class, withSettings().extraInterfaces(NestedRouteModel.class));
    when(nestable.getName()).thenReturn("listOfRoutes");
    when(nestable.getModelProperty(ListOfRoutesModelProperty.class)).thenReturn(of(ListOfRoutesModelProperty.INSTANCE));
    doAnswer(inv -> {
      inv.getArgument(0, ComponentModelVisitor.class)
          .visit((NestedRouteModel) inv.getMock());
      return null;
    })
        .when(nestable)
        .accept(any(ComponentModelVisitor.class));

    doReturn(singletonList(nestable))
        .when(routerModel).getNestedComponents();
    doAnswer(inv -> {
      inv.getArgument(0, ComponentModelVisitor.class)
          .visit((OperationModel) inv.getMock());
      return null;
    })
        .when(routerModel)
        .accept(any(ComponentModelVisitor.class));

    final DslElementSyntax dslElementSyntax = getSyntaxResolver().resolve(routerModel);

    final Optional<DslElementSyntax> listOfRoutes = dslElementSyntax.getChild("listOfRoutes");
    assertThat(listOfRoutes.isPresent(), is(true));
    final Optional<DslElementSyntax> listOfRoute = listOfRoutes.get().getChild("listOfRoute");
    assertThat(listOfRoute.isPresent(), is(true));
  }
}
