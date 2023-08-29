/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.internal.loader.validator;

import static org.mule.runtime.api.test.util.tck.ExtensionModelTestUtils.visitableMock;

import static java.util.Collections.singletonList;
import static java.util.Optional.of;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.construct.ConstructModel;
import org.mule.runtime.api.meta.model.function.FunctionModel;
import org.mule.runtime.api.meta.model.nested.NestedChainModel;
import org.mule.runtime.api.meta.model.nested.NestedComponentModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.api.property.NoWrapperModelProperty;
import org.mule.runtime.extension.internal.loader.validator.NoWrapperModelValidator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NoWrapperModelValidatorTestCase {

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ExtensionModel extensionModel;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private OperationModel operationModel;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private SourceModel sourceModel;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private FunctionModel functionModel;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ConstructModel constructModel;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ConnectionProviderModel connectionProviderModel;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ConfigurationModel configurationModel;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ParameterGroupModel parameterGroupModel;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ParameterModel parameterModel;

  private NoWrapperModelValidator noWrapperModelValidator;
  private ProblemsReporter problemsReporter;
  private ClassTypeLoader TYPE_LOADER = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

  @Before
  public void setUp() {
    noWrapperModelValidator = new NoWrapperModelValidator();
    problemsReporter = new ProblemsReporter(extensionModel);

    when(extensionModel.getOperationModels()).thenReturn(singletonList(operationModel));
    when(extensionModel.getFunctionModels()).thenReturn(singletonList(functionModel));
    when(extensionModel.getConfigurationModels()).thenReturn(singletonList(configurationModel));
    when(configurationModel.getConnectionProviders()).thenReturn(singletonList(connectionProviderModel));
    when(extensionModel.getConstructModels()).thenReturn((singletonList(constructModel)));
    when(configurationModel.getSourceModels()).thenReturn(singletonList(sourceModel));

    when(operationModel.getName()).thenReturn("operationName");
    when(configurationModel.getName()).thenReturn("configName");
    when(parameterGroupModel.getName()).thenReturn("parameterGroupName");
    when(parameterModel.getName()).thenReturn("parameterName");
    when(sourceModel.getName()).thenReturn("sourceName");
    when(connectionProviderModel.getName()).thenReturn("connectionProviderName");

    when(parameterGroupModel.getParameterModels()).thenReturn(singletonList(parameterModel));

    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(String.class));
    when(operationModel.getParameterGroupModels()).thenReturn(singletonList(parameterGroupModel));
    visitableMock(operationModel, sourceModel);
  }

  @Test
  public void nonArrayOrMapParameterWithNoWrapper() {
    when(parameterModel.getModelProperty(NoWrapperModelProperty.class)).thenReturn(of(NoWrapperModelProperty.INSTANCE));
    validateError("Parameter named parameterName is enriched with the NoWrapperModelProperty. Parameters that are not array types or maps cannot be enriched with the NoWrapperModelProperty.");
  }

  @Test
  public void operationEnrichedWithNoWrapper() {
    when(operationModel.getModelProperty(NoWrapperModelProperty.class)).thenReturn(of(NoWrapperModelProperty.INSTANCE));
    validateError("Operation named operationName is enriched with the NoWrapperModelProperty. Operations are not allowed to be enriched with it.");
  }

  @Test
  public void sourceEnrichedWithNoWrapper() {
    when(sourceModel.getModelProperty(NoWrapperModelProperty.class)).thenReturn(of(NoWrapperModelProperty.INSTANCE));
    validateError("Source named sourceName is enriched with the NoWrapperModelProperty. Sources are not allowed to be enriched with it.");
  }

  @Test
  public void configurationEnrichedWithNoWrapper() {
    when(configurationModel.getModelProperty(NoWrapperModelProperty.class)).thenReturn(of(NoWrapperModelProperty.INSTANCE));
    validateError("Configuration named configName is enriched with the NoWrapperModelProperty. Configurations are not allowed to be enriched with it.");
  }

  @Test
  public void connectionProviderEnrichedWithNoWrapper() {
    when(connectionProviderModel.getModelProperty(NoWrapperModelProperty.class)).thenReturn(of(NoWrapperModelProperty.INSTANCE));
    validateError("Connection provider named connectionProviderName is enriched with the NoWrapperModelProperty. Connection providers are not allowed to be enriched with it.");
  }

  @Test
  public void parameterGroupEnrichedWithNoWrapper() {
    when(parameterGroupModel.getModelProperty(NoWrapperModelProperty.class)).thenReturn(of(NoWrapperModelProperty.INSTANCE));
    validateError("Parameter group named parameterGroupName is enriched with the NoWrapperModelProperty. Parameter groups are not allowed to be enriched with it.");
  }

  @Test
  public void ownerCannotBeChainIfNoWrapped() {
    NestedChainModel chainModelMock = mock(NestedChainModel.class);
    visitableMock(chainModelMock);
    List nestedComponents = new ArrayList();
    nestedComponents.add(chainModelMock);
    NestedComponentModel nestedComponentModel = mock(NestedComponentModel.class);
    when(nestedComponentModel.getName()).thenReturn("innerNestedComponent");
    List innerNestedComponents = new ArrayList();
    innerNestedComponents.add(nestedComponentModel);
    when(nestedComponentModel.getModelProperty(NoWrapperModelProperty.class)).thenReturn(of(NoWrapperModelProperty.INSTANCE));
    when(operationModel.getNestedComponents()).thenReturn(nestedComponents);
    when(chainModelMock.getNestedComponents()).thenReturn(innerNestedComponents);
    validateError("Nestable component named innerNestedComponent is enriched with the NoWrapperModelProperty. A component cannot be enriched with the NoWrapperModelProperty if its owner is a Chain Model.");
  }

  @Test
  public void ownerCannotBeNoWrappedIfChildIsNoWrapped() {
    NestedComponentModel nestedComponentModelMock = mock(NestedComponentModel.class);
    visitableMock(nestedComponentModelMock);
    List nestedComponents = new ArrayList();
    nestedComponents.add(nestedComponentModelMock);
    NestedComponentModel innerNestedComponentModel = mock(NestedComponentModel.class);
    when(innerNestedComponentModel.getName()).thenReturn("innerNestedComponent");
    List innerNestedComponents = new ArrayList();
    innerNestedComponents.add(innerNestedComponentModel);
    when(innerNestedComponentModel.getModelProperty(NoWrapperModelProperty.class))
        .thenReturn(of(NoWrapperModelProperty.INSTANCE));
    when(nestedComponentModelMock.getModelProperty(NoWrapperModelProperty.class))
        .thenReturn(of(NoWrapperModelProperty.INSTANCE));
    when(operationModel.getNestedComponents()).thenReturn(nestedComponents);
    when(nestedComponentModelMock.getNestedComponents()).thenReturn(innerNestedComponents);
    validateError("Nestable component named innerNestedComponent is enriched with the NoWrapperModelProperty. Its owner is also enriched with the NoWrapperModelProperty. The owner and its child cannot be both enriched with it.");
  }


  @Test
  public void noWrappedElementCannotHaveParameters() {
    NestedComponentModel nestedComponentModelMock = mock(NestedComponentModel.class);
    visitableMock(nestedComponentModelMock);
    List nestedComponents = new ArrayList();
    nestedComponents.add(nestedComponentModelMock);
    NestedComponentModel innerNestedComponentModel = mock(NestedComponentModel.class);
    when(innerNestedComponentModel.getName()).thenReturn("innerNestedComponent");
    List innerNestedComponents = new ArrayList();
    innerNestedComponents.add(innerNestedComponentModel);
    when(innerNestedComponentModel.getModelProperty(NoWrapperModelProperty.class))
        .thenReturn(of(NoWrapperModelProperty.INSTANCE));
    when(innerNestedComponentModel.getAllParameterModels()).thenReturn(singletonList(parameterModel));
    when(operationModel.getNestedComponents()).thenReturn(nestedComponents);
    when(nestedComponentModelMock.getNestedComponents()).thenReturn(innerNestedComponents);
    validateError("Nestable component named innerNestedComponent is enriched with the NoWrapperModelProperty. A component cannot be enriched with the NoWrapperModelProperty and have parameters.");
  }

  @Test
  public void ownerCanOnlyHaveOneNoWrappedChainAsChild() {
    NestedComponentModel chainModelMock = mock(NestedComponentModel.class);
    when(chainModelMock.getName()).thenReturn("nestedComponent");
    visitableMock(chainModelMock);
    List nestedComponents = new ArrayList();
    nestedComponents.add(chainModelMock);
    NestedChainModel nestedChainModel = mock(NestedChainModel.class);
    when(nestedChainModel.getName()).thenReturn("nestedChain");
    NestedChainModel otherNestedChainModel = mock(NestedChainModel.class);
    when(otherNestedChainModel.getName()).thenReturn("otherNestedChain");
    List innerChainComponents = new ArrayList();
    innerChainComponents.add(nestedChainModel);
    innerChainComponents.add(otherNestedChainModel);
    when(nestedChainModel.getModelProperty(NoWrapperModelProperty.class)).thenReturn(of(NoWrapperModelProperty.INSTANCE));
    when(otherNestedChainModel.getModelProperty(NoWrapperModelProperty.class)).thenReturn(of(NoWrapperModelProperty.INSTANCE));
    when(operationModel.getNestedComponents()).thenReturn(nestedComponents);
    when(chainModelMock.getNestedComponents()).thenReturn(innerChainComponents);
    validateError("Component named nestedComponent has more than one nested chain enriched with the NoWrapperModelProperty [nestedChain, otherNestedChain]. Only one chain child can be enriched with it.");
  }


  private void validateError(String errorMessage) {
    noWrapperModelValidator.validate(extensionModel, problemsReporter);
    assertThat(problemsReporter.hasErrors(), is(true));
    assertThat(problemsReporter.getErrors(), hasSize(1));
    assertThat(problemsReporter.getErrors().get(0).getMessage(), containsString(errorMessage));
  }

}
