/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.internal.loader.validator;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.function.FunctionModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.internal.loader.validator.NameModelValidator;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NameModelValidatorTestCase {

  @Mock(answer = RETURNS_DEEP_STUBS)
  ExtensionModel extensionModel;

  @Mock
  OperationModel operationModel;

  @Mock
  FunctionModel functionModel;

  private NameModelValidator nameModelValidator;
  private ProblemsReporter problemsReporter;

  @Before
  public void setUp() {
    nameModelValidator = new NameModelValidator();
    problemsReporter = new ProblemsReporter(extensionModel);
    when(extensionModel.getOperationModels()).thenReturn(singletonList(operationModel));
    when(extensionModel.getFunctionModels()).thenReturn(singletonList(functionModel));
    when(operationModel.getName()).thenReturn("validName");
    when(functionModel.getName()).thenReturn("validName");
  }

  @Test
  public void camelCaseName() {
    when(operationModel.getName()).thenReturn("validName");
    nameModelValidator.validate(extensionModel, problemsReporter);
    assertSuccess();
  }

  @Test
  public void xmlTypeName() {
    when(operationModel.getName()).thenReturn("xmlTypeName");
    nameModelValidator.validate(extensionModel, problemsReporter);
    assertSuccess();
  }

  @Test
  public void invalidOperationName() {
    when(operationModel.getName()).thenReturn("inval;id");
    nameModelValidator.validate(extensionModel, problemsReporter);
    assertError("Names should follow: http://www.w3.org/TR/xmlschema-2/#NCName");
  }

  @Test
  public void functionStartingWithANumber() {
    when(functionModel.getName()).thenReturn("123");
    nameModelValidator.validate(extensionModel, problemsReporter);
    assertError("Function names can't start with a number.");
  }

  @Test
  public void functionNotAlphanumeric() {
    when(functionModel.getName()).thenReturn("hi**");
    nameModelValidator.validate(extensionModel, problemsReporter);
    assertError("Function names must be alpha numeric.");
  }

  private void assertError(String errorMessage) {
    List<Problem> errors = problemsReporter.getErrors();
    assertThat(errors, is(not(empty())));
    assertThat(errors.get(0).getMessage(), containsString(errorMessage));
  }

  private void assertSuccess() {
    List<Problem> errors = problemsReporter.getErrors();
    assertThat(errors, is(empty()));
  }
}
