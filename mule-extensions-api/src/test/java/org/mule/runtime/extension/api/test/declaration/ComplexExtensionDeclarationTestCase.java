/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.declaration;

import static org.mule.runtime.api.meta.Category.COMMUNITY;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.ANOTHER_COMPLEX_TYPE;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.COMPLEX_TYPE;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.EXTENSION_DESCRIPTION;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.EXTENSION_NAME;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.LISTENER_CONFIG_DESCRIPTION;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.LISTENER_CONFIG_NAME;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.LISTEN_MESSAGE_SOURCE;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.PATH;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.PORT;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.REQUESTER_CONFIG_DESCRIPTION;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.REQUESTER_CONFIG_NAME;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.REQUESTER_PROVIDER;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.REQUEST_OPERATION_NAME;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.STATIC_RESOURCE_OPERATION_NAME;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.VENDOR;
import static org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer.VERSION;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import org.mule.metadata.api.model.BinaryType;
import org.mule.metadata.api.model.NumberType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.test.meta.model.tck.TestHttpConnectorDeclarer;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Set;

import org.junit.Test;

public class ComplexExtensionDeclarationTestCase extends BaseDeclarationTestCase {

  private TestHttpConnectorDeclarer testDeclarer = new TestHttpConnectorDeclarer();
  private ExtensionDeclaration extensionDeclaration = testDeclarer.getExtensionDeclarer().getDeclaration();

  @Test
  public void assertDeclaration() {
    assertThat(extensionDeclaration.getName(), is(EXTENSION_NAME));
    assertThat(extensionDeclaration.getDescription(), is(EXTENSION_DESCRIPTION));
    assertThat(extensionDeclaration.getVersion(), is(VERSION));
    assertThat(extensionDeclaration.getConfigurations(), hasSize(2));
    assertThat(extensionDeclaration.getVendor(), is(VENDOR));
    assertThat(extensionDeclaration.getCategory(), is(COMMUNITY));
    assertThat(extensionDeclaration.getOperations(), hasSize(1));
    assertThat(extensionDeclaration.getConnectionProviders(), is(empty()));
    assertThat(extensionDeclaration.getMessageSources(), is(empty()));
  }

  @Test
  public void listenerConfig() {
    ConfigurationDeclaration listener = extensionDeclaration.getConfigurations().get(0);
    assertThat(listener.getName(), is(LISTENER_CONFIG_NAME));
    assertThat(listener.getDescription(), is(LISTENER_CONFIG_DESCRIPTION));
    assertThat(listener.getOperations(), is(empty()));
    assertThat(listener.getConnectionProviders(), is(empty()));
    assertThat(listener.getMessageSources(), hasSize(1));
  }

  @Test
  public void listenerSource() {
    SourceDeclaration source = extensionDeclaration.getConfigurations().get(0).getMessageSources().get(0);
    assertThat(source.getName(), is(LISTEN_MESSAGE_SOURCE));
    assertDataType(source.getOutput().getType(), InputStream.class, BinaryType.class);
    assertDataType(source.getOutputAttributes().getType(), Serializable.class, ObjectType.class);
    assertThat(source.getAllParameters(), hasSize(1));

    ParameterDeclaration parameter = source.getAllParameters().get(0);
    assertThat(parameter.getName(), is(PORT));
    assertThat(parameter.isRequired(), is(false));
    assertDataType(parameter.getType(), Integer.class, NumberType.class);
  }

  @Test
  public void requesterConfig() {
    ConfigurationDeclaration requester = extensionDeclaration.getConfigurations().get(1);
    assertThat(requester.getName(), is(REQUESTER_CONFIG_NAME));
    assertThat(requester.getDescription(), is(REQUESTER_CONFIG_DESCRIPTION));
    assertThat(requester.getOperations(), hasSize(1));
    assertThat(requester.getConnectionProviders(), hasSize(1));
    assertThat(requester.getMessageSources(), is(empty()));
  }

  @Test
  public void requestOperation() {
    OperationDeclaration operation = extensionDeclaration.getConfigurations().get(1).getOperations().get(0);
    assertThat(operation.getName(), is(REQUEST_OPERATION_NAME));
    assertDataType(operation.getOutput().getType(), InputStream.class, BinaryType.class);
    assertThat(operation.getAllParameters(), hasSize(1));

    ParameterDeclaration parameter = operation.getAllParameters().get(0);
    assertThat(parameter.getName(), is(PATH));
    assertDataType(parameter.getType(), String.class, StringType.class);
  }

  @Test
  public void staticResourceOperation() {
    OperationDeclaration operation = extensionDeclaration.getOperations().get(0);
    assertThat(operation.getName(), is(STATIC_RESOURCE_OPERATION_NAME));
    assertDataType(operation.getOutput().getType(), InputStream.class, BinaryType.class);

    assertThat(operation.getAllParameters(), hasSize(1));
    ParameterDeclaration parameter = operation.getAllParameters().get(0);
    assertThat(parameter.getName(), is(PATH));
    assertDataType(parameter.getType(), String.class, StringType.class);
  }

  @Test
  public void connectionProvider() {
    ConnectionProviderDeclaration provider = extensionDeclaration.getConfigurations().get(1).getConnectionProviders().get(0);
    assertThat(provider.getName(), is(REQUESTER_PROVIDER));
    assertThat(provider.isSupportsConnectivityTesting(), is(false));
  }

  @Test
  public void types() {
    Set<ObjectType> types = extensionDeclaration.getTypes();
    assertThat(types, hasSize(2));
    assertThat(types, containsInAnyOrder(COMPLEX_TYPE, ANOTHER_COMPLEX_TYPE));
  }
}
