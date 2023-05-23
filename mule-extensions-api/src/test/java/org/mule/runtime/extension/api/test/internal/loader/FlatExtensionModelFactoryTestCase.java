/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.internal.loader;

import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.metadata.java.api.utils.JavaTypeUtils.getType;
import static org.mule.runtime.api.meta.Category.COMMUNITY;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.meta.model.connection.ConnectionManagementType.NONE;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.ADDRESS;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.ARG_LESS;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.BROADCAST;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.BROADCAST_DESCRIPTION;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.CALLBACK;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.CALLBACK_DESCRIPTION;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.COLLECTION_PARAMETER;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.CONFIG_DESCRIPTION;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.CONFIG_NAME;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.CONNECTION_PROVIDER_DESCRIPTION;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.CONNECTION_PROVIDER_NAME;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.CONSUMER;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.DEFAULT_PORT;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.DSL_PREFIX;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.GO_GET_THEM_TIGER;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.HAS_NO_ARGS;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.LISTENER;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.LISTEN_DESCRIPTION;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.MTOM_DESCRIPTION;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.MTOM_ENABLED;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.MULESOFT;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.OPERATION;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.PASSWORD;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.PASSWORD_DESCRIPTION;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.PORT;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.PORT_DESCRIPTION;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.SERVICE;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.SERVICE_ADDRESS;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.SERVICE_NAME;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.SERVICE_PORT;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.THE_OPERATION_TO_USE;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.URI_TO_FIND_THE_WSDL;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.URL;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.URL_DESCRIPTION;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.USERNAME;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.USERNAME_DESCRIPTION;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.VERSION;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.WSDL_LOCATION;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.WS_CONSUMER;
import static org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer.WS_CONSUMER_DESCRIPTION;
import static org.mule.runtime.api.util.NameUtils.underscorize;
import static org.mule.runtime.extension.api.ExtensionConstants.EXPIRATION_POLICY_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.EXPIRATION_POLICY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.NAME_PARAM_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_CONFIG_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_CONFIG_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_STRATEGY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.RECONNECTION_STRATEGY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.REDELIVERY_POLICY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.STREAMING_STRATEGY_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.STREAMING_STRATEGY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TARGET_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.TARGET_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TARGET_VALUE_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.TARGET_VALUE_PARAMETER_NAME;
import static org.mule.runtime.extension.api.annotation.param.Optional.PAYLOAD;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.CONFIG;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.CONNECTION;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.PROCESSOR;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.SOURCE;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.mule.metadata.api.builder.ArrayTypeBuilder;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.WithAnnotation;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.BinaryType;
import org.mule.metadata.api.model.BooleanType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.NumberType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.model.VoidType;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclarer;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.api.test.meta.model.tck.TestWebServiceConsumerDeclarer;
import org.mule.runtime.extension.api.declaration.type.DynamicConfigExpirationTypeBuilder;
import org.mule.runtime.extension.api.declaration.type.ReconnectionStrategyTypeBuilder;
import org.mule.runtime.extension.api.declaration.type.RedeliveryPolicyTypeBuilder;
import org.mule.runtime.extension.api.declaration.type.StreamingStrategyTypeBuilder;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.exception.IllegalParameterModelDefinitionException;
import org.mule.runtime.extension.internal.property.PagedOperationModelProperty;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FlatExtensionModelFactoryTestCase extends BaseExtensionModelFactoryTestCase {

  private static final String NAMESPACE = DSL_PREFIX.toUpperCase();
  private final TestWebServiceConsumerDeclarer reference = new TestWebServiceConsumerDeclarer() {

    @Override
    protected BooleanType getBooleanType() {
      return super.withType(typeBuilder.booleanType(), Boolean.class).build();
    }

    @Override
    protected <T extends WithAnnotation<?>> T withType(T builder, Class<?> type) {
      return (T) super.withType(builder, type)
          .with(new ClassInformationAnnotation(type));
    }
  };
  private final MetadataType voidType = typeLoader.load(void.class);
  private final MetadataType stringType = typeLoader.load(String.class);
  private final MetadataType targetValue = typeLoader.load(String.class);

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
    assertThat(parameterModels, hasSize(6));
    assertParameter(parameterModels.get(0), "name", NAME_PARAM_DESCRIPTION,
                    NOT_SUPPORTED, true, stringType, StringType.class, null);
    assertThat(parameterModels.get(0).isComponentId(), is(true));
    assertParameter(parameterModels.get(1), EXPIRATION_POLICY_PARAMETER_NAME, EXPIRATION_POLICY_DESCRIPTION,
                    NOT_SUPPORTED,
                    false, new DynamicConfigExpirationTypeBuilder().buildExpirationPolicyType(),
                    ObjectType.class, null);
    assertParameter(parameterModels.get(2), ADDRESS, SERVICE_ADDRESS, SUPPORTED, true, stringType,
                    StringType.class, null);
    assertParameter(parameterModels.get(3), PORT, SERVICE_PORT, SUPPORTED, true, stringType, StringType.class,
                    null);
    assertParameter(parameterModels.get(4), SERVICE, SERVICE_NAME, SUPPORTED, true, stringType,
                    StringType.class, null);
    assertParameter(parameterModels.get(5), WSDL_LOCATION, URI_TO_FIND_THE_WSDL, NOT_SUPPORTED, true,
                    stringType, StringType.class, null);
  }

  @Test
  public void onlyOneConfig() throws Exception {
    assertThat(extensionModel.getConfigurationModels(), hasSize(1));
    assertThat(extensionModel.getConfigurationModels().get(0),
               is(sameInstance(extensionModel.getConfigurationModel(CONFIG_NAME).get())));
  }

  @Test
  public void noSuchConfiguration() throws Exception {
    assertThat(extensionModel.getConfigurationModel("fake").isPresent(), is(false));
  }

  @Test
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

    operationModels.forEach(operation -> {
      StereotypeModel stereotypeModel = operation.getStereotype();
      assertThat(stereotypeModel.getType(), equalTo(underscorize(operation.getName()).toUpperCase()));
      assertThat(stereotypeModel.getNamespace(), equalTo(NAMESPACE));

      StereotypeModel parent = stereotypeModel.getParent().get();
      assertThat(parent.getNamespace(), equalTo(NAMESPACE));
      assertThat(parent.getType(), equalTo(PROCESSOR.getType()));
      assertThat(parent.getParent().get(), is(PROCESSOR));
    });
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
          .withXmlDsl(XmlDslModel.builder().setPrefix(DSL_PREFIX).build());

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

  @Test(expected = IllegalParameterModelDefinitionException.class)
  public void fixedParameterWithExpressionDefault() {
    declare(declarer -> {
      declarer = declareBase(declarer);
      OperationDeclarer operation = declarer.withOperation("invalidOperation").describedAs("");
      operation.withOutput().ofType(stringType);
      operation.withOutputAttributes().ofType(voidType);
      operation.onDefaultParameterGroup().withOptionalParameter("fixed")
          .ofType(stringType).withExpressionSupport(NOT_SUPPORTED).defaultingTo("#['hello']");
    });

    load();
  }

  public void operationWithParameterNamedTarget() {
    exception.expect(IllegalModelDefinitionException.class);
    exception.expectMessage(containsString("The following operations have parameters named after reserved words"));

    declare(declarer -> {
      declarer = declareBase(declarer);
      OperationDeclarer operation = declarer.withOperation("invalidOperation").describedAs("");
      operation.onDefaultParameterGroup().withOptionalParameter(TARGET_PARAMETER_NAME).ofType(stringType);
      operation.withOutput().ofType(stringType);
      operation.withOutputAttributes().ofType(voidType);
    });

    load();
  }

  @Test(expected = IllegalParameterModelDefinitionException.class)
  public void expressionParameterWithFixedValue() {
    declare(declarer -> {
      declarer = declareBase(declarer);
      OperationDeclarer operation = declarer.withOperation("invalidOperation").describedAs("");
      operation.onDefaultParameterGroup().withOptionalParameter("expression")
          .ofType(stringType).withExpressionSupport(REQUIRED).defaultingTo("static");
      operation.withOutput().ofType(stringType);
      operation.withOutputAttributes().ofType(voidType);
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

    declare(declarer -> declareBase(declarer).withCategory(null).fromVendor("SomeVendor"));
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
        .withXmlDsl(XmlDslModel.builder().build()));

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
    assertParameter(parameters.get(0), USERNAME, USERNAME_DESCRIPTION, SUPPORTED, true, stringType,
                    StringType.class, null);
    assertParameter(parameters.get(1), PASSWORD, PASSWORD_DESCRIPTION, SUPPORTED, true, stringType,
                    StringType.class, null);
    assertParameter(parameters.get(2), RECONNECTION_CONFIG_PARAMETER_NAME, RECONNECTION_CONFIG_PARAMETER_DESCRIPTION,
                    NOT_SUPPORTED,
                    false, new ReconnectionStrategyTypeBuilder().buildReconnectionConfigType(), ObjectType.class, null);
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
    assertByteStreamingStrategyParameter(parameters.get(0));
    assertRedeliveryPolicyParameter(parameters.get(1));
    assertParameter(parameters.get(2), URL, URL_DESCRIPTION, SUPPORTED, true, stringType, StringType.class,
                    null);
    assertParameter(parameters.get(3), PORT, PORT_DESCRIPTION, SUPPORTED, false, typeLoader.load(Integer.class), NumberType.class,
                    DEFAULT_PORT);
    assertParameter(parameters.get(4), RECONNECTION_STRATEGY_PARAMETER_NAME, RECONNECTION_STRATEGY_PARAMETER_DESCRIPTION,
                    NOT_SUPPORTED,
                    false, new ReconnectionStrategyTypeBuilder().buildReconnectionStrategyType(), UnionType.class, null);

    StereotypeModel stereotype = sourceModel.getStereotype();
    assertThat(stereotype.getType(), equalTo(LISTENER.toUpperCase()));
    assertThat(stereotype.getNamespace(), equalTo(NAMESPACE));

    StereotypeModel parent = stereotype.getParent().get();
    assertThat(parent.getType(), equalTo(SOURCE.getType()));
    assertThat(parent.getNamespace(), equalTo(NAMESPACE));
    assertThat(parent.getParent().get(), is(SOURCE));
  }

  @Test
  public void objectStreaming() {
    final String LIST_TYPES_OPERATION = "LIST_TYPES";
    declare(extensionDeclarer -> {
      reference.declareOn(extensionDeclarer);
      OperationDeclarer operation = extensionDeclarer.withOperation(LIST_TYPES_OPERATION).describedAs("List types");
      operation.supportsStreaming(true).withOutput().ofType(typeLoader.load(Iterator.class));
      operation.withOutputAttributes().ofType(voidType);
      operation.withModelProperty(new PagedOperationModelProperty());
    });

    ExtensionModel extensionModel = load();

    OperationModel operation = extensionModel.getOperationModel(LIST_TYPES_OPERATION).orElseThrow(IllegalArgumentException::new);
    ParameterModel streamingStrategy = operation.getAllParameterModels().stream()
        .filter(p -> p.getName().equals(STREAMING_STRATEGY_PARAMETER_NAME))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);


    assertObjectStreamingStrategyParameter(streamingStrategy);
  }

  @Test
  @Issue("MULE-18457")
  @Description("For crafted extensions that do not set a stereotype in the models, a default is set.")
  public void configDefaultStereotype() {
    declare(extensionDeclarer -> {
      declareBase(extensionDeclarer).withConfig("myConfig");
    });

    ExtensionModel extensionModel = load();

    StereotypeModel configStereotype = extensionModel.getConfigurationModel("myConfig").get().getStereotype();
    assertThat(configStereotype.getType(), equalTo("MY_CONFIG"));
    assertThat(configStereotype.getNamespace(), equalTo(DSL_PREFIX.toUpperCase()));
    assertThat(configStereotype.getParent().get(), is(CONFIG));
  }

  @Test
  @Issue("MULE-18457")
  @Description("For crafted extensions that do not set a stereotype in the models, a default is set.")
  public void connectionDefaultStereotype() {
    declare(extensionDeclarer -> {
      declareBase(extensionDeclarer)
          .withConnectionProvider("myConnection")
          .withConnectionManagementType(NONE);
    });

    ExtensionModel extensionModel = load();

    StereotypeModel connectionStereotype = extensionModel.getConnectionProviderModel("myConnection").get().getStereotype();
    assertThat(connectionStereotype.getType(), equalTo("MY_CONNECTION"));
    assertThat(connectionStereotype.getNamespace(), equalTo(NAMESPACE));
    assertThat(connectionStereotype.getParent().get(), is(CONNECTION));
  }

  private void assertConsumeOperation(List<OperationModel> operationModels) {
    OperationModel operationModel = operationModels.get(2);
    assertThat(operationModel, is(sameInstance(extensionModel.getOperationModel(CONSUMER).get())));
    assertDataType(operationModel.getOutput().getType(), InputStream.class, BinaryType.class);

    assertThat(operationModel.getName(), equalTo(CONSUMER));
    assertThat(operationModel.getDescription(), equalTo(GO_GET_THEM_TIGER));

    List<ParameterModel> parameterModels = operationModel.getAllParameterModels();
    assertThat(parameterModels, hasSize(6));

    assertByteStreamingStrategyParameter(parameterModels.get(0));
    assertParameter(parameterModels.get(1), OPERATION, THE_OPERATION_TO_USE, SUPPORTED, true, stringType,
                    StringType.class, null);
    assertParameter(parameterModels.get(2), MTOM_ENABLED, MTOM_DESCRIPTION, SUPPORTED, false,
                    typeLoader.load(Boolean.class), BooleanType.class, true);
    assertTargetParameter(parameterModels.get(3), parameterModels.get(4));
  }

  private void assertBroadcastOperation(List<OperationModel> operationModels) {
    OperationModel operationModel = operationModels.get(1);
    assertThat(operationModel, is(sameInstance(extensionModel.getOperationModel(BROADCAST).get())));
    assertDataType(operationModel.getOutput().getType(), void.class, VoidType.class);

    assertThat(operationModel.getName(), equalTo(BROADCAST));
    assertThat(operationModel.getDescription(), equalTo(BROADCAST_DESCRIPTION));

    List<ParameterModel> parameterModels = operationModel.getAllParameterModels();
    assertThat(parameterModels, hasSize(4));

    ArrayTypeBuilder arrayTypeBuilder = BaseTypeBuilder.create(JAVA).arrayType();
    arrayTypeBuilder.of(BaseTypeBuilder.create(JAVA).stringType());
    ArrayType arrayType = arrayTypeBuilder.build();

    assertParameter(parameterModels.get(0), COLLECTION_PARAMETER, THE_OPERATION_TO_USE, SUPPORTED, true,
                    arrayType, ArrayType.class, null);
    assertParameter(parameterModels.get(1), MTOM_ENABLED, MTOM_DESCRIPTION, SUPPORTED, false,
                    typeLoader.load(Boolean.class), BooleanType.class, true);
    assertParameter(parameterModels.get(2), CALLBACK, CALLBACK_DESCRIPTION, REQUIRED, true, typeLoader.load(OperationModel.class),
                    ObjectType.class, null);
  }

  private void assertTargetParameter(ParameterModel target, ParameterModel targetValue) {
    assertParameter(target, TARGET_PARAMETER_NAME, TARGET_PARAMETER_DESCRIPTION, NOT_SUPPORTED, false,
                    stringType,
                    StringType.class, null);
    assertParameter(targetValue, TARGET_VALUE_PARAMETER_NAME, TARGET_VALUE_PARAMETER_DESCRIPTION, REQUIRED, false,
                    this.targetValue,
                    StringType.class, PAYLOAD);
  }


  private void assertByteStreamingStrategyParameter(ParameterModel parameter) {
    assertStreamingStrategyParameter(parameter, new StreamingStrategyTypeBuilder().getByteStreamingStrategyType());
  }

  private void assertObjectStreamingStrategyParameter(ParameterModel parameter) {
    assertStreamingStrategyParameter(parameter, new StreamingStrategyTypeBuilder().getObjectStreamingStrategyType());
  }

  private void assertStreamingStrategyParameter(ParameterModel parameter, MetadataType type) {
    assertParameter(parameter, STREAMING_STRATEGY_PARAMETER_NAME, STREAMING_STRATEGY_PARAMETER_DESCRIPTION, NOT_SUPPORTED, false,
                    type, UnionType.class, null);
  }

  private void assertRedeliveryPolicyParameter(ParameterModel redelivery) {
    assertThat(redelivery.getName(), is(REDELIVERY_POLICY_PARAMETER_NAME));
    assertThat(redelivery.getType(), is(equalTo(new RedeliveryPolicyTypeBuilder().buildRedeliveryPolicyType())));
  }

  private void assertArglessOperation(List<OperationModel> operationModels) {
    OperationModel operationModel = operationModels.get(0);
    assertThat(operationModel, is(sameInstance(extensionModel.getOperationModel(ARG_LESS).get())));
    assertDataType(operationModel.getOutput().getType(), Integer.class, NumberType.class);

    assertThat(operationModel.getName(), equalTo(ARG_LESS));
    assertThat(operationModel.getDescription(), equalTo(HAS_NO_ARGS));

    List<ParameterModel> parameterModels = operationModel.getAllParameterModels();
    assertThat(parameterModels, hasSize(3));
    assertTargetParameter(parameterModels.get(0), parameterModels.get(1));
  }

  private ExtensionDeclarer declareBase(ExtensionDeclarer extensionDeclarer) {
    reference.declareOn(extensionDeclarer);
    return extensionDeclarer;
  }
}
