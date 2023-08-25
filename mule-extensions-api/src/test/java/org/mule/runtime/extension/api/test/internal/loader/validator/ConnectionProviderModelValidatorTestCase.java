/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.internal.loader.validator;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.annotation.param.ConfigOverride;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.internal.loader.validator.ConnectionProviderModelValidator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionProviderModelValidatorTestCase {

  public static final String EXTENSION_NAME = "extension";
  public static final String OPERATION_NAME = "operation";
  public static final String CONNECTION_PROVIDER_NAME = "connectionProviderName";
  public static final String PARAMETER_NAME = "parameterName";

  @Rule
  public ExpectedException expectedException = none();

  @Mock(lenient = true)
  private ExtensionModel extensionModel;

  @Mock(answer = RETURNS_DEEP_STUBS, lenient = true)
  private ConfigurationModel configurationModel;

  @Mock(answer = RETURNS_DEEP_STUBS, lenient = true)
  private ConnectionProviderModel connectionProviderModel;

  @Mock(answer = RETURNS_DEEP_STUBS, lenient = true)
  private OperationModel operationModel;

  @Mock(lenient = true)
  private ParameterModel parameterModel;

  private ExtensionModelValidator validator = new ConnectionProviderModelValidator();
  private ProblemsReporter problemsReporter;

  @Before
  public void before() {
    problemsReporter = new ProblemsReporter(extensionModel);
    when(extensionModel.getName()).thenReturn(EXTENSION_NAME);
    when(operationModel.getName()).thenReturn(OPERATION_NAME);
    when(connectionProviderModel.getName()).thenReturn(CONNECTION_PROVIDER_NAME);
    when(operationModel.getErrorModels()).thenReturn(emptySet());
    when(parameterModel.getName()).thenReturn(PARAMETER_NAME);
    when(parameterModel.isOverrideFromConfig()).thenReturn(false);
    when(extensionModel.getOperationModels()).thenReturn(asList(operationModel));
    when(operationModel.getAllParameterModels()).thenReturn(emptyList());
    when(operationModel.requiresConnection()).thenReturn(true);
    when(extensionModel.getOperationModels()).thenReturn(emptyList());
    when(extensionModel.getConfigurationModels()).thenReturn(singletonList(configurationModel));
    when(configurationModel.getConnectionProviders()).thenReturn(singletonList(connectionProviderModel));
    when(connectionProviderModel.getAllParameterModels()).thenReturn(asList(parameterModel));
    when(configurationModel.getOperationModels()).thenReturn(singletonList(operationModel));
  }

  @Test
  public void validConnectionProvider() {
    validate();
  }

  @Test
  public void connectionProviderWithConfigOverrideParameter() {
    when(parameterModel.isOverrideFromConfig()).thenReturn(true);
    validateError("Connection '" + CONNECTION_PROVIDER_NAME
        + "' declares the parameters [" + PARAMETER_NAME + "] as '" + ConfigOverride.class.getSimpleName()
        + "', which is not allowed for this component parameters");
  }

  private void validate() {
    validator.validate(extensionModel, problemsReporter);
    assertThat(problemsReporter.hasErrors(), is(false));
  }

  private void validateError(String errorMessage) {
    validator.validate(extensionModel, problemsReporter);
    assertThat(problemsReporter.hasErrors(), is(true));
    assertThat(problemsReporter.getErrors(), hasSize(1));
    assertThat(problemsReporter.getErrors().get(0).getMessage(), containsString(errorMessage));
  }
}
