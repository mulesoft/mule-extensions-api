/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.internal.loader.validator;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;
import static org.mule.runtime.internal.dsl.DslConstants.NAME_ATTRIBUTE_NAME;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.annotation.param.ConfigOverride;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.api.property.SyntheticModelModelProperty;
import org.mule.runtime.extension.internal.loader.validator.ConfigurationModelValidator;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationModelValidatorTestCase {

  public static final String EXTENSION_NAME = "extension";
  public static final String CONFIG_NAME = "configName";
  public static final String OPERATION_NAME = "operation";
  public static final String PARAMETER_NAME = "parameterName";

  public static final String ALLOWLISTED_EXTENSION_NAME = "cxf";
  public static final String ALLOWLISTED_CONFIGURATION_NAME = "wsSecurity";

  @Rule
  public ExpectedException expectedException = none();

  @Mock(lenient = true)
  private ExtensionModel extensionModel;

  @Mock(answer = RETURNS_DEEP_STUBS, lenient = true)
  private ConfigurationModel configurationModel;

  @Mock(answer = RETURNS_DEEP_STUBS, lenient = true)
  private OperationModel operationModel;

  @Mock(lenient = true)
  private ParameterModel parameterModel;

  @Mock(lenient = true)
  private ParameterModel nameParameterModel;

  private ExtensionModelValidator validator = new ConfigurationModelValidator();
  private ProblemsReporter problemsReporter;

  @Before
  public void before() {
    problemsReporter = new ProblemsReporter(extensionModel);
    when(extensionModel.getName()).thenReturn(EXTENSION_NAME);
    when(configurationModel.getName()).thenReturn(CONFIG_NAME);
    when(operationModel.getName()).thenReturn(OPERATION_NAME);
    when(operationModel.getErrorModels()).thenReturn(emptySet());
    when(parameterModel.getName()).thenReturn(PARAMETER_NAME);
    when(parameterModel.isOverrideFromConfig()).thenReturn(false);
    when(nameParameterModel.getName()).thenReturn(NAME_ATTRIBUTE_NAME);
    when(nameParameterModel.isOverrideFromConfig()).thenReturn(false);
    when(nameParameterModel.getModelProperty(SyntheticModelModelProperty.class))
        .thenReturn(of(new SyntheticModelModelProperty()));
    when(extensionModel.getOperationModels()).thenReturn(asList(operationModel));
    when(operationModel.getAllParameterModels()).thenReturn(emptyList());
    when(operationModel.requiresConnection()).thenReturn(true);
    when(extensionModel.getOperationModels()).thenReturn(emptyList());
    when(extensionModel.getConfigurationModels()).thenReturn(singletonList(configurationModel));
    when(configurationModel.getConnectionProviders()).thenReturn(emptyList());
    when(configurationModel.getOperationModels()).thenReturn(singletonList(operationModel));
    List<ParameterModel> configurationParameters = new ArrayList<>();
    configurationParameters.add(parameterModel);
    configurationParameters.add(nameParameterModel);
    when(configurationModel.getAllParameterModels()).thenReturn(configurationParameters);
  }

  @Test
  public void validConfiguration() {
    validate();
  }

  @Test
  public void configurationWithConfigOverrideParameter() {
    when(parameterModel.isOverrideFromConfig()).thenReturn(true);
    validateError("Configuration '" + CONFIG_NAME
        + "' declares the parameters [" + PARAMETER_NAME + "] as '" + ConfigOverride.class.getSimpleName()
        + "', which is not allowed for this component parameters");
  }

  @Test
  public void configurationWithNonSyntheticNameParameter() {
    when(nameParameterModel.getModelProperty(SyntheticModelModelProperty.class)).thenReturn(empty());
    validateError("Configuration '" + CONFIG_NAME + "' declares a parameter whose name is 'name', which is not allowed");
  }

  @Test
  public void configurationWithNonSyntheticNameParameterFromAllowList() {
    when(extensionModel.getName()).thenReturn(ALLOWLISTED_EXTENSION_NAME);
    when(configurationModel.getName()).thenReturn(ALLOWLISTED_CONFIGURATION_NAME);
    when(nameParameterModel.getModelProperty(SyntheticModelModelProperty.class)).thenReturn(empty());
    validate();
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
