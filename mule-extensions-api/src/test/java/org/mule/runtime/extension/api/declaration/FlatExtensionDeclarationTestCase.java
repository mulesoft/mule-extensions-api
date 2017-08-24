/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.api.meta.Category.SELECT;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.*;
import org.mule.metadata.api.model.BinaryType;
import org.mule.metadata.api.model.NumberType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.metadata.api.model.VoidType;
import org.mule.runtime.api.meta.model.ExternalLibraryModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.declaration.fluent.BaseDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.FunctionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * This class tests the fluent API that allows
 * to perform declarations which describes an extension.
 *
 * @since 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class FlatExtensionDeclarationTestCase extends BaseDeclarationTestCase {

  private TestWebServiceConsumerDeclarer testDeclaration;
  private ExtensionDeclaration extensionDeclaration;

  @Before
  public void before() {
    testDeclaration = new TestWebServiceConsumerDeclarer();
    extensionDeclaration = testDeclaration.getExtensionDeclarer().getDeclaration();
  }

  @Test
  public void assertDeclaration() {
    assertThat(extensionDeclaration.getName(), is(WS_CONSUMER));
    assertThat(extensionDeclaration.getDescription(), is(WS_CONSUMER_DESCRIPTION));
    assertThat(extensionDeclaration.getVersion(), is(VERSION));
    assertThat(extensionDeclaration.getConfigurations(), hasSize(1));
    assertThat(extensionDeclaration.getVendor(), is(MULESOFT));
    assertThat(extensionDeclaration.getMinMuleVersion(), is(MIN_MULE_VERSION));
    assertThat(extensionDeclaration.getCategory(), is(SELECT));

    assertExternalLibraries(extensionDeclaration.getExternalLibraryModels());
    assertModelProperties(extensionDeclaration, EXTENSION_MODEL_PROPERTY);
  }

  @Test
  public void defaultConfiguration() throws Exception {
    assertThat(extensionDeclaration.getConfigurations(), hasSize(1));
    ConfigurationDeclaration configuration = extensionDeclaration.getConfigurations().get(0);
    assertThat(configuration, is(notNullValue()));
    assertThat(configuration.getName(), is(CONFIG_NAME));
    assertThat(configuration.getDescription(), is(CONFIG_DESCRIPTION));
    assertModelProperties(configuration, CONFIGURATION_MODEL_PROPERTY);

    List<ParameterDeclaration> parameters = assertGroupAndGetParameters(configuration, CONFIG_PARAMETER_GROUP);
    assertThat(parameters, hasSize(4));
    assertParameter(parameters.get(0), ADDRESS, SERVICE_ADDRESS, SUPPORTED, true, typeLoader.load(String.class), null);
    assertParameter(parameters.get(1), PORT, SERVICE_PORT, SUPPORTED, true, typeLoader.load(String.class), null);
    assertParameter(parameters.get(2), SERVICE, SERVICE_NAME, SUPPORTED, true, typeLoader.load(String.class), null);
    assertParameter(parameters.get(3), WSDL_LOCATION, URI_TO_FIND_THE_WSDL, NOT_SUPPORTED, true, typeLoader.load(String.class),
                    null);

    assertExternalLibraries(configuration.getExternalLibraryModels());
    assertModelProperties(parameters.get(3), PARAMETER_MODEL_PROPERTY);
  }

  @Test
  public void operations() throws Exception {
    List<OperationDeclaration> operations = extensionDeclaration.getOperations();
    assertThat(operations, hasSize(3));
    assertArgLessOperation(operations);
    assertBroadcastOperation(operations);
    assertConsumeOperation(operations);
  }

  @Test
  public void functions() throws Exception {
    List<FunctionDeclaration> functions = extensionDeclaration.getFunctions();
    assertThat(functions, hasSize(1));

    FunctionDeclaration function = functions.get(0);
    assertThat(function.getName(), is(FUNCTION_NAME));
    assertThat(function.getDescription(), is(FUNCTION_DESCRIPTION));
    assertDataType(function.getOutput().getType(), Integer.class, NumberType.class);;

    List<ParameterDeclaration> parameters = assertGroupAndGetParameters(function, DEFAULT_GROUP_NAME);
    assertThat(parameters, hasSize(1));
    assertParameter(parameters.get(0), USERNAME, USERNAME_DESCRIPTION, SUPPORTED, true, typeLoader.load(String.class), null);
  }


  @Test
  public void connectionProvider() throws Exception {
    List<ConnectionProviderDeclaration> connectionProviders = extensionDeclaration.getConnectionProviders();
    assertThat(connectionProviders, hasSize(1));

    ConnectionProviderDeclaration connectionProvider = connectionProviders.get(0);
    assertThat(connectionProvider, is(notNullValue()));
    assertThat(connectionProvider.getName(), is(CONNECTION_PROVIDER_NAME));
    assertThat(connectionProvider.getDescription(), is(CONNECTION_PROVIDER_DESCRIPTION));
    assertExternalLibraries(connectionProvider.getExternalLibraryModels());

    List<ParameterDeclaration> parameters = assertGroupAndGetParameters(connectionProvider, CONNECTION_PROVIDER_PARAMETER_GROUP);
    assertThat(parameters, hasSize(2));
    assertParameter(parameters.get(0), USERNAME, USERNAME_DESCRIPTION, SUPPORTED, true, typeLoader.load(String.class), null);
    assertParameter(parameters.get(1), PASSWORD, PASSWORD_DESCRIPTION, SUPPORTED, true, typeLoader.load(String.class), null);
  }

  @Test
  public void messageSource() throws Exception {
    List<SourceDeclaration> sources = extensionDeclaration.getMessageSources();
    assertThat(sources, hasSize(1));

    SourceDeclaration source = sources.get(0);
    assertThat(source, is(notNullValue()));
    assertThat(source.getName(), is(LISTENER));
    assertThat(source.getDescription(), is(LISTEN_DESCRIPTION));
    assertDataType(source.getOutput().getType(), InputStream.class, BinaryType.class);
    assertDataType(source.getOutputAttributes().getType(), Serializable.class, ObjectType.class);

    List<ParameterDeclaration> parameters = assertGroupAndGetParameters(source, SOURCE_PARAMETER_GROUP);
    assertThat(parameters, hasSize(2));
    assertParameter(parameters.get(0), URL, URL_DESCRIPTION, SUPPORTED, true, typeLoader.load(String.class), null);
    assertParameter(parameters.get(1), PORT, PORT_DESCRIPTION, SUPPORTED, false, typeLoader.load(Integer.class), DEFAULT_PORT);
  }

  private void assertConsumeOperation(List<OperationDeclaration> operations) {
    OperationDeclaration operation = operations.get(2);
    assertThat(operation.getName(), is(CONSUMER));
    assertThat(operation.getDescription(), is(GO_GET_THEM_TIGER));
    assertDataType(operation.getOutput().getType(), InputStream.class, BinaryType.class);
    assertDataType(operation.getOutputAttributes().getType(), String.class, StringType.class);
    assertModelProperties(operation, OPERATION_MODEL_PROPERTY);

    List<ParameterDeclaration> parameters = assertGroupAndGetParameters(operation, OPERATION_PARAMETER_GROUP);
    assertThat(parameters, hasSize(2));
    assertParameter(parameters.get(0), OPERATION, THE_OPERATION_TO_USE, SUPPORTED, true, typeLoader.load(String.class), null);
    assertParameter(parameters.get(1), MTOM_ENABLED, MTOM_DESCRIPTION, SUPPORTED, false, typeLoader.load(Boolean.class), true);
  }

  private void assertBroadcastOperation(List<OperationDeclaration> operations) {
    OperationDeclaration operation = operations.get(1);

    assertThat(operation.getName(), is(BROADCAST));
    assertThat(operation.getDescription(), is(BROADCAST_DESCRIPTION));
    assertDataType(operation.getOutput().getType(), void.class, VoidType.class);

    List<ParameterDeclaration> parameters = assertGroupAndGetParameters(operation, OPERATION_PARAMETER_GROUP);
    assertThat(parameters, hasSize(3));
    assertParameter(parameters.get(0), COLLECTION_PARAMETER, THE_OPERATION_TO_USE, SUPPORTED, true, typeBuilder.arrayType()
        .of(typeBuilder.stringType())
        .build(), null);
    assertParameter(parameters.get(1), MTOM_ENABLED, MTOM_DESCRIPTION, SUPPORTED, false, typeLoader.load(Boolean.class), true);
    assertParameter(parameters.get(2), CALLBACK, CALLBACK_DESCRIPTION, REQUIRED, true, typeLoader.load(OperationModel.class),
                    null);
  }

  private void assertArgLessOperation(List<OperationDeclaration> operations) {
    OperationDeclaration operation = operations.get(0);

    assertThat(operation.getName(), is(ARG_LESS));
    assertThat(operation.getDescription(), is(HAS_NO_ARGS));
    assertDataType(operation.getOutput().getType(), Integer.class, NumberType.class);

    List<ParameterGroupDeclaration> parameterGroups = operation.getParameterGroups();
    assertThat(parameterGroups, is(notNullValue()));
    assertThat(parameterGroups.isEmpty(), is(true));

    List<ParameterDeclaration> parameters = operation.getAllParameters();
    assertThat(parameters, is(notNullValue()));
    assertThat(parameters.isEmpty(), is(true));
  }

  private void assertModelProperties(BaseDeclaration<?> declaration, ModelProperty... modelProperty) {
    List<ModelProperty> expected = Arrays.asList(modelProperty);
    Set<ModelProperty> properties = declaration.getModelProperties();
    assertThat(properties, is(not(nullValue())));
    assertThat(properties.size(), is(expected.size()));
    expected.forEach(mp -> {
      assertThat(properties.contains(mp), is(true));
      assertThat(declaration.getModelProperty(mp.getClass()).get(), is(sameInstance(mp)));
    });
  }

  private List<ParameterDeclaration> assertGroupAndGetParameters(ParameterizedDeclaration declaration, String groupName) {
    List<ParameterGroupDeclaration> groupDeclarations = declaration.getParameterGroups();
    assertThat(groupDeclarations, hasSize(1));
    ParameterGroupDeclaration groupDeclaration = groupDeclarations.get(0);
    assertThat(groupDeclaration.getName(), is(groupName));

    return groupDeclaration.getParameters();
  }

  private void assertExternalLibraries(Set<ExternalLibraryModel> externalLibraryModels) {
    assertThat(externalLibraryModels, is(notNullValue()));
    assertThat(externalLibraryModels, hasSize(1));
    assertThat(externalLibraryModels.iterator().next(), is(EXTERNAL_LIBRARY_MODEL));
  }
}
