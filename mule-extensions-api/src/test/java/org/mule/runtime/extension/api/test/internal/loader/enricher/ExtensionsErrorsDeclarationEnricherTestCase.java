/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.internal.loader.enricher;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mule.runtime.api.meta.model.error.ErrorModelBuilder.newError;
import static org.mule.runtime.api.meta.model.stereotype.StereotypeModelBuilder.newStereotype;
import static org.mule.sdk.api.error.MuleErrors.VALIDATION;
import static org.mule.sdk.api.stereotype.MuleStereotypes.VALIDATOR;

import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.stereotype.ImmutableStereotypeModel;
import org.mule.runtime.extension.api.error.MuleErrors;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.internal.loader.enricher.ExtensionsErrorsDeclarationEnricher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExtensionsErrorsDeclarationEnricherTestCase {

  private static final String NAMESPACE = "TEST";

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ExtensionLoadingContext extensionLoadingContext;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ExtensionDeclarer declarer;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ExtensionDeclaration extensionDeclaration;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private OperationDeclaration operationDeclaration;

  private ExtensionsErrorsDeclarationEnricher enricher = new ExtensionsErrorsDeclarationEnricher();

  @Before
  public void before() {
    when(extensionLoadingContext.getExtensionDeclarer()).thenReturn(declarer);
    when(declarer.getDeclaration()).thenReturn(extensionDeclaration);
    when(extensionDeclaration.getXmlDslModel()).thenReturn(XmlDslModel.builder()
        .setNamespace(NAMESPACE)
        .setPrefix(NAMESPACE)
        .build());
    when(extensionDeclaration.getOperations()).thenReturn(asList(operationDeclaration));
  }

  @Test
  public void addValidationErrorOnOperationAndExtension() {
    setValidatorStereotype(operationDeclaration);
    enricher.enrich(extensionLoadingContext);
    ArgumentCaptor<ErrorModel> errorCaptor = forClass(ErrorModel.class);
    verify(operationDeclaration).addErrorModel(errorCaptor.capture());

    ErrorModel operationError = errorCaptor.getValue();
    assertThat(operationError, is(notNullValue()));

    errorCaptor = forClass(ErrorModel.class);

    verify(extensionDeclaration).addErrorModel(errorCaptor.capture());
    ErrorModel extensionError = errorCaptor.getValue();
    assertThat(extensionError, is(sameInstance(operationError)));

    assertThat(extensionError.getNamespace(), equalTo(NAMESPACE));
    assertThat(extensionError.getType(), equalTo(VALIDATION.getType()));
    ErrorModel errorParent = extensionError.getParent().get();
    assertThat(errorParent.getType(), equalTo(VALIDATION.getType()));
    assertThat(errorParent.getNamespace(), equalTo("MULE"));
  }

  @Test
  public void reuseValidationError() {
    ErrorModel validationError = buildValidationError();
    when(extensionDeclaration.getErrorModels()).thenReturn(singleton(validationError));

    setValidatorStereotype(operationDeclaration);
    enricher.enrich(extensionLoadingContext);

    ArgumentCaptor<ErrorModel> errorCaptor = forClass(ErrorModel.class);
    verify(operationDeclaration).addErrorModel(errorCaptor.capture());

    ErrorModel operationError = errorCaptor.getValue();
    assertThat(operationError, is(sameInstance(validationError)));
    assertNoErrorAdded(extensionDeclaration);
  }

  @Test
  public void validationErrorPresent() {
    setValidatorStereotype(operationDeclaration);
    ErrorModel validationError = buildValidationError();
    when(operationDeclaration.getErrorModels()).thenReturn(singleton(validationError));

    enricher.enrich(extensionLoadingContext);
    assertNoErrorAdded(extensionDeclaration, operationDeclaration);
  }

  @Test
  public void doNotAddValidationErrorOnNonValidatorOperation() {
    when(operationDeclaration.getStereotype()).thenReturn(new ImmutableStereotypeModel("PEPITA", "PISTOLERA", null));
    enricher.enrich(extensionLoadingContext);
    assertNoErrorAdded(extensionDeclaration, operationDeclaration);
  }

  private void assertNoErrorAdded(ExtensionDeclaration extensionDeclaration) {
    verify(extensionDeclaration, never()).addErrorModel(any());
  }

  private void assertNoErrorAdded(ExtensionDeclaration extensionDeclaration, ComponentDeclaration... components) {
    assertNoErrorAdded(extensionDeclaration);
    for (ComponentDeclaration component : components) {
      verify(component, never()).addErrorModel(any());
    }
  }

  private ErrorModel buildValidationError() {
    return newError(VALIDATION.getType(), NAMESPACE)
        .withParent(newError(MuleErrors.VALIDATION.getType(), "MULE").build())
        .build();
  }

  private void setValidatorStereotype(OperationDeclaration operationDeclaration) {
    when(operationDeclaration.getStereotype()).thenReturn(newStereotype(VALIDATOR.getType(), NAMESPACE)
        .withParent(VALIDATOR)
        .build());
  }
}
