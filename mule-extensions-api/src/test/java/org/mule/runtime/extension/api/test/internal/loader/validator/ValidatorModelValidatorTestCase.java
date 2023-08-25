/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.internal.loader.validator;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.api.meta.model.stereotype.StereotypeModelBuilder.newStereotype;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.PROCESSOR;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.VALIDATOR;
import org.mule.metadata.api.model.StringType;
import org.mule.metadata.api.model.VoidType;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.error.ImmutableErrorModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.error.MuleErrors;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.internal.loader.validator.ValidatorModelValidator;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(Parameterized.class)
public class ValidatorModelValidatorTestCase {

  @Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        {"Mule error and stereotype", false, false}, {"Child error and mule stereotype", true, false},
        {"Child error and Child stereotype", true, true}, {"Mule error and child stereotype", false, true},
    });
  }

  private final boolean childError;
  private final boolean childStereotype;

  public ValidatorModelValidatorTestCase(String name, boolean childError, boolean childStereotype) {
    this.childError = childError;
    this.childStereotype = childStereotype;
  }

  @Mock
  private ExtensionModel extensionModel;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private OperationModel operationModel;

  private ValidatorModelValidator validator = new ValidatorModelValidator();
  private ProblemsReporter reporter = new ProblemsReporter(extensionModel);

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);
    StereotypeModel validationStereotype = newStereotype(VALIDATOR.getType(), VALIDATOR.getNamespace())
        .withParent(newStereotype(PROCESSOR.getType(), PROCESSOR.getNamespace()).build())
        .build();

    if (childStereotype) {
      validationStereotype = newStereotype("customValidator", "customExtension")
          .withParent(validationStereotype)
          .build();
    }

    when(operationModel.getStereotype()).thenReturn(validationStereotype);

    ErrorModel errorModel = new ImmutableErrorModel(MuleErrors.VALIDATION.name(), "MULE", true, null);
    if (childError) {
      errorModel = new ImmutableErrorModel("customValidation", "customExtension", true, errorModel);
    }

    when(operationModel.getErrorModels()).thenReturn(singleton(errorModel));
    when(operationModel.getOutput().getType()).thenReturn(mock(VoidType.class));
    when(extensionModel.getOperationModels()).thenReturn(singletonList(operationModel));
  }

  @Test
  public void valid() {
    validator.validate(extensionModel, reporter);
    assertThat(reporter.hasErrors(), is(false));
  }

  private void assertFail() {
    validator.validate(extensionModel, reporter);
    assertThat(reporter.hasErrors(), is(true));
  }

  @Test
  public void nonVoidType() {
    when(operationModel.getOutput().getType()).thenReturn(mock(StringType.class));
    assertFail();
  }

  @Test
  public void nonValidationOrphanError() {
    ErrorModel errorModel = new ImmutableErrorModel("custom", "custom", true, null);
    when(operationModel.getErrorModels()).thenReturn(singleton(errorModel));

    assertFail();
  }

  @Test
  public void nonValidationChildError() {
    ErrorModel errorModel = new ImmutableErrorModel("custom", "custom", true, null);
    errorModel = new ImmutableErrorModel("customChild", "custom", true, errorModel);
    when(operationModel.getErrorModels()).thenReturn(singleton(errorModel));

    assertFail();
  }
}
