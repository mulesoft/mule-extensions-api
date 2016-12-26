/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.metadata.java.api.utils.JavaTypeUtils.getType;
import static org.mule.runtime.api.meta.Category.COMMUNITY;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.ADDRESS;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.ARG_LESS;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.BROADCAST;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.BROADCAST_DESCRIPTION;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.CALLBACK;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.CALLBACK_DESCRIPTION;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.COLLECTION_PARAMETER;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.CONFIG_DESCRIPTION;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.CONFIG_NAME;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.CONNECTION_PROVIDER_DESCRIPTION;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.CONNECTION_PROVIDER_NAME;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.CONSUMER;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.DEFAULT_PORT;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.GO_GET_THEM_TIGER;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.HAS_NO_ARGS;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.LISTENER;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.LISTEN_DESCRIPTION;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.MTOM_DESCRIPTION;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.MTOM_ENABLED;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.MULESOFT;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.OPERATION;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.PASSWORD;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.PASSWORD_DESCRIPTION;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.PORT;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.PORT_DESCRIPTION;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.SERVICE;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.SERVICE_ADDRESS;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.SERVICE_NAME;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.SERVICE_PORT;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.THE_OPERATION_TO_USE;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.URI_TO_FIND_THE_WSDL;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.URL;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.URL_DESCRIPTION;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.USERNAME;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.USERNAME_DESCRIPTION;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.VERSION;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.WSDL_LOCATION;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.WS_CONSUMER;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.WS_CONSUMER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_STRATEGY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_STRATEGY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.REDELIVERY_POLICY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.REDELIVERY_POLICY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TARGET_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.TARGET_PARAMETER_NAME;
import org.mule.metadata.api.builder.ArrayTypeBuilder;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.BinaryType;
import org.mule.metadata.api.model.BooleanType;
import org.mule.metadata.api.model.NumberType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.model.VoidType;
import org.mule.runtime.api.message.NullAttributes;
import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclarer;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer;
import org.mule.runtime.extension.api.declaration.type.RedeliveryPolicyTypeBuilder;
import org.mule.runtime.extension.api.declaration.type.ReconnectionStrategyTypeBuilder;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.exception.IllegalParameterModelDefinitionException;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FlatExtensionModelFactoryTestCase extends BaseExtensionModelFactoryTestCase {

  private static final MuleVersion MIN_MULE_VERSION = new MuleVersion("4.0");
  private final TestWebServiceConsumerDeclarer reference = new TestWebServiceConsumerDeclarer();

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Before
  public void before() {
    declare(this::declareBase);
    load();
  }

  @Test
  public void assertExtension() {
    assertThat(extensionModel.getName(), equalTo(WS_CONSUMER));
    assertThat(extensionModel.getDescription(), equalTo(WS_CONSUMER_DESCRIPTION));
    assertThat(extensionModel.getVersion(), equalTo(VERSION));
    assertThat(extensionModel.getConfigurationModels(), hasSize(1));
    assertThat(extensionModel.getVendor(), equalTo(MULESOFT));
  }

  @Test
  public void defaultConfiguration() throws Exception {
    ConfigurationModel configurationModel = extensionModel.getConfigurationModel(CONFIG_NAME).get();
    assertThat(configurationModel, is(notNullValue()));
    assertThat(configurationModel.getName(), equalTo(CONFIG_NAME));
    assertThat(configurationModel.getDescription(), equalTo(CONFIG_DESCRIPTION));

    List<ParameterModel> parameterModels = configurationModel.getAllParameterModels();
    assertThat(parameterModels, hasSize(4));
    assertParameter(parameterModels.get(0), ADDRESS, SERVICE_ADDRESS, SUPPORTED, true, typeLoader.load(String.class),
                    StringType.class, null);
    assertParameter(parameterModels.get(1), PORT, SERVICE_PORT, SUPPORTED, true, typeLoader.load(String.class), StringType.class,
                    null);
    assertParameter(parameterModels.get(2), SERVICE, SERVICE_NAME, SUPPORTED, true, typeLoader.load(String.class),
                    StringType.class, null);
    assertParameter(parameterModels.get(3), WSDL_LOCATION, URI_TO_FIND_THE_WSDL, NOT_SUPPORTED, true,
                    typeLoader.load(String.class), StringType.class, null);
  }

  @Test
  public void onlyOneConfig() throws Exception {
    assertThat(extensionModel.getConfigurationModels(), hasSize(1));
    assertThat(extensionModel.getConfigurationModels().get(0),
               is(sameInstance(extensionModel.getConfigurationModel(CONFIG_NAME).get())));
  }

  public void noSuchConfiguration() throws Exception {
    assertThat(extensionModel.getConfigurationModel("fake").isPresent(), is(false));
  }

  public void noSuchOperation() throws Exception {
    assertThat(extensionModel.getOperationModel("fake").isPresent(), is(false));
  }

  @Test
  public void operations() throws Exception {
    List<OperationModel> operationModels = extensionModel.getOperationModels();
    assertThat(operationModels, hasSize(3));
    assertConsumeOperation(operationModels);
    assertBroadcastOperation(operationModels);
    assertArglessOperation(operationModels);
  }

  @Test(expected = IllegalArgumentException.class)
  public void badExtensionVersion() {
    declare(declarer -> declarer.named("bad").onVersion("i'm new"));
    load();
  }

  @Test
  public void configurationsOrder() {
    final String alpha = "alpha";
    final String beta = "beta";
    final String gamma = "gamma";

    declare(extensionDeclarer -> {
      extensionDeclarer.named("test")
          .onVersion("1.0")
          .fromVendor("MuleSoft")
          .withCategory(COMMUNITY)
          .withMinMuleVersion(MIN_MULE_VERSION)
          .withXmlDsl(XmlDslModel.builder().build());

      extensionDeclarer.withConfig(gamma).describedAs(gamma);
      extensionDeclarer.withConfig(beta).describedAs(beta);
      extensionDeclarer.withConfig(alpha).describedAs(alpha);
    });

    ExtensionModel extensionModel = load();
    List<ConfigurationModel> configurationModels = extensionModel.getConfigurationModels();
    assertThat(configurationModels, hasSize(3));
    assertThat(configurationModels.get(0).getName(), equalTo(alpha));
    assertThat(configurationModels.get(1).getName(), equalTo(beta));
    assertThat(configurationModels.get(2).getName(), equalTo(gamma));
  }

  @Test
  public void operationsAlphaSorted() {
    assertThat(extensionModel.getOperationModels(), hasSize(3));
    assertThat(extensionModel.getOperationModels().get(0).getName(), equalTo(ARG_LESS));
    assertThat(extensionModel.getOperationModels().get(1).getName(), equalTo(BROADCAST));
    assertThat(extensionModel.getOperationModels().get(2).getName(), equalTo(CONSUMER));
  }

  @Test(expected = IllegalModelDefinitionException.class)
  public void nameClashes() {
    declare(extensionDeclarer -> reference.declareOn(extensionDeclarer).withConfig(CONFIG_NAME).describedAs(""));
    load();
  }

  @Test
  public void operationWithParameterNamedName() {
    exception.expect(IllegalModelDefinitionException.class);
    exception.expectMessage(containsString("is named after a reserved one"));

    declare(extensionDeclarer -> {
      extensionDeclarer = declareBase(extensionDeclarer);
      OperationDeclarer operation = extensionDeclarer.withOperation("invalidOperation").describedAs("");
      operation.onDefaultParameterGroup().withRequiredParameter("name").ofType(typeLoader.load(String.class));
      operation.withOutput().ofType(typeLoader.load(String.class));
      operation.withOutputAttributes().ofType(typeLoader.load(NullAttributes.class));
    });
    load();
  }

  @Test(expected = IllegalParameterModelDefinitionException.class)
  public void fixedParameterWithExpressionDefault() {
    declare(declarer -> {
      declarer = declareBase(declarer);
      OperationDeclarer operation = declarer.withOperation("invalidOperation").describedAs("");
      operation.withOutput().ofType(typeLoader.load(String.class));
      operation.withOutputAttributes().ofType(typeLoader.load(NullAttributes.class));
      operation.onDefaultParameterGroup().withOptionalParameter("fixed")
          .ofType(typeLoader.load(String.class)).withExpressionSupport(NOT_SUPPORTED).defaultingTo("#['hello']");
    });

    load();
  }

  public void operationWithParameterNamedTarget() {
    exception.expect(IllegalModelDefinitionException.class);
    exception.expectMessage(containsString("The following operations have parameters named after reserved words"));

    declare(declarer -> {
      declarer = declareBase(declarer);
      OperationDeclarer operation = declarer.withOperation("invalidOperation").describedAs("");
      operation.onDefaultParameterGroup().withOptionalParameter(TARGET_PARAMETER_NAME).ofType(typeLoader.load(String.class));
      operation.withOutput().ofType(typeLoader.load(String.class));
      operation.withOutputAttributes().ofType(typeLoader.load(NullAttributes.class));
    });

    load();
  }

  @Test(expected = IllegalParameterModelDefinitionException.class)
  public void expressionParameterWithFixedValue() {
    declare(declarer -> {
      declarer = declareBase(declarer);
      OperationDeclarer operation = declarer.withOperation("invalidOperation").describedAs("");
      operation.onDefaultParameterGroup().withOptionalParameter("expression")
          .ofType(typeLoader.load(String.class)).withExpressionSupport(REQUIRED).defaultingTo("static");
      operation.withOutput().ofType(typeLoader.load(String.class));
      operation.withOutputAttributes().ofType(typeLoader.load(NullAttributes.class));
    });

    load();
  }

  @Test
  public void nullVendor() {
    expectIllegalModel("Extension Vendor cannot be null");

    declare(declarer -> declareBase(declarer).fromVendor(null));
    load();
  }

  @Test
  public void nullCategory() {
    expectIllegalModel("Extension Category cannot be null");

    declare(declarer -> declareBase(declarer).withCategory(null).fromVendor("SomeVendor").withMinMuleVersion(MIN_MULE_VERSION));
    load();
  }

  @Test
  public void nullMinMuleVersion() {
    expectIllegalModel("Extension Minimum Mule Version cannot be null");

    declare(declarer -> declareBase(declarer).withMinMuleVersion(null).fromVendor("SomeVendor").withCategory(COMMUNITY));
    load();
  }

  private void expectIllegalModel(String messageSubString) {
    exception.expect(IllegalModelDefinitionException.class);
    exception.expectMessage(containsString(messageSubString));
  }

  @Test
  public void configlessDescriptor() {
    declare(declarer -> declarer.named("noConfigs")
        .onVersion("1.0")
        .fromVendor("MuleSoft")
        .withCategory(COMMUNITY)
        .withXmlDsl(XmlDslModel.builder().build())
        .withMinMuleVersion(MIN_MULE_VERSION));

    load();
  }

  @Test
  public void connectionProviders() {
    assertThat(extensionModel.getConnectionProviders(), hasSize(1));
    ConnectionProviderModel connectionProvider = extensionModel.getConnectionProviders().get(0);
    assertThat(connectionProvider, is(notNullValue()));
    assertThat(connectionProvider.getName(), is(CONNECTION_PROVIDER_NAME));
    assertThat(connectionProvider.getDescription(), is(CONNECTION_PROVIDER_DESCRIPTION));

    List<ParameterModel> parameters = connectionProvider.getAllParameterModels();
    assertParameter(parameters.get(0), RECONNECTION_STRATEGY_PARAMETER_NAME, RECONNECTION_STRATEGY_PARAMETER_DESCRIPTION,
                    NOT_SUPPORTED,
                    false, new ReconnectionStrategyTypeBuilder().builReconnectionStrategyType(), UnionType.class, null);
    assertParameter(parameters.get(1), USERNAME, USERNAME_DESCRIPTION, SUPPORTED, true, typeLoader.load(String.class),
                    StringType.class, null);
    assertParameter(parameters.get(2), PASSWORD, PASSWORD_DESCRIPTION, SUPPORTED, true, typeLoader.load(String.class),
                    StringType.class, null);
  }

  @Test
  public void messageSources() {
    assertThat(extensionModel.getSourceModels(), hasSize(1));
    SourceModel sourceModel = extensionModel.getSourceModels().get(0);
    assertThat(sourceModel, is(notNullValue()));
    assertThat(sourceModel.getName(), is(LISTENER));
    assertThat(sourceModel.getDescription(), is(LISTEN_DESCRIPTION));
    assertThat(getType(sourceModel.getOutput().getType()), is(equalTo(InputStream.class)));
    assertThat(getType(sourceModel.getOutputAttributes().getType()), is(equalTo(Serializable.class)));

    List<ParameterModel> parameters = sourceModel.getAllParameterModels();
    assertParameter(parameters.get(0), REDELIVERY_POLICY_PARAMETER_NAME, REDELIVERY_POLICY_PARAMETER_DESCRIPTION, NOT_SUPPORTED,
                    false, new RedeliveryPolicyTypeBuilder().buildRetryPolicyType(), ObjectType.class, null);
    assertParameter(parameters.get(1), URL, URL_DESCRIPTION, SUPPORTED, true, typeLoader.load(String.class), StringType.class,
                    null);
    assertParameter(parameters.get(2), PORT, PORT_DESCRIPTION, SUPPORTED, false, typeLoader.load(Integer.class), NumberType.class,
                    DEFAULT_PORT);
    assertParameter(parameters.get(3), RECONNECTION_STRATEGY_PARAMETER_NAME, RECONNECTION_STRATEGY_PARAMETER_DESCRIPTION,
                    NOT_SUPPORTED,
                    false, new ReconnectionStrategyTypeBuilder().builReconnectionStrategyType(), UnionType.class, null);
  }

  private void assertConsumeOperation(List<OperationModel> operationModels) {
    OperationModel operationModel = operationModels.get(2);
    assertThat(operationModel, is(sameInstance(extensionModel.getOperationModel(CONSUMER).get())));
    assertDataType(operationModel.getOutput().getType(), InputStream.class, BinaryType.class);

    assertThat(operationModel.getName(), equalTo(CONSUMER));
    assertThat(operationModel.getDescription(), equalTo(GO_GET_THEM_TIGER));

    List<ParameterModel> parameterModels = operationModel.getAllParameterModels();
    assertThat(parameterModels, hasSize(3));
    assertTargetParameter(parameterModels.get(0));
    assertParameter(parameterModels.get(1), OPERATION, THE_OPERATION_TO_USE, SUPPORTED, true, typeLoader.load(String.class),
                    StringType.class, null);
    assertParameter(parameterModels.get(2), MTOM_ENABLED, MTOM_DESCRIPTION, SUPPORTED, false, typeLoader.load(Boolean.class),
                    BooleanType.class, true);
  }

  private void assertBroadcastOperation(List<OperationModel> operationModels) {
    OperationModel operationModel = operationModels.get(1);
    assertThat(operationModel, is(sameInstance(extensionModel.getOperationModel(BROADCAST).get())));
    assertDataType(operationModel.getOutput().getType(), void.class, VoidType.class);

    assertThat(operationModel.getName(), equalTo(BROADCAST));
    assertThat(operationModel.getDescription(), equalTo(BROADCAST_DESCRIPTION));

    List<ParameterModel> parameterModels = operationModel.getAllParameterModels();
    assertThat(parameterModels, hasSize(3));

    ArrayTypeBuilder arrayTypeBuilder = BaseTypeBuilder.create(JAVA).arrayType();
    arrayTypeBuilder.id(List.class.getName());
    arrayTypeBuilder.of(BaseTypeBuilder.create(JAVA).stringType().id(String.class.getTypeName()));
    ArrayType arrayType = arrayTypeBuilder.build();

    assertParameter(parameterModels.get(0), COLLECTION_PARAMETER, THE_OPERATION_TO_USE, SUPPORTED, true,
                    arrayType, ArrayType.class, null);
    assertParameter(parameterModels.get(1), MTOM_ENABLED, MTOM_DESCRIPTION, SUPPORTED, false, typeLoader.load(Boolean.class),
                    BooleanType.class, true);
    assertParameter(parameterModels.get(2), CALLBACK, CALLBACK_DESCRIPTION, REQUIRED, true, typeLoader.load(OperationModel.class),
                    ObjectType.class, null);
  }

  private void assertTargetParameter(ParameterModel parameterModel) {
    assertParameter(parameterModel, TARGET_PARAMETER_NAME, TARGET_PARAMETER_DESCRIPTION, NOT_SUPPORTED, false,
                    typeLoader.load(String.class),
                    StringType.class, null);
  }

  private void assertArglessOperation(List<OperationModel> operationModels) {
    OperationModel operationModel = operationModels.get(0);
    assertThat(operationModel, is(sameInstance(extensionModel.getOperationModel(ARG_LESS).get())));
    assertDataType(operationModel.getOutput().getType(), Integer.class, NumberType.class);

    assertThat(operationModel.getName(), equalTo(ARG_LESS));
    assertThat(operationModel.getDescription(), equalTo(HAS_NO_ARGS));

    List<ParameterModel> parameterModels = operationModel.getAllParameterModels();
    assertThat(parameterModels, hasSize(1));
    assertTargetParameter(parameterModels.get(0));
  }

  private ExtensionDeclarer declareBase(ExtensionDeclarer extensionDeclarer) {
    reference.declareOn(extensionDeclarer);
    return extensionDeclarer;
  }
}
