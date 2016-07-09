/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.test;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.config.ConfigurationModel;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.runtime.extension.api.introspection.source.SourceModel;
import org.mule.runtime.extension.xml.dsl.api.DslElementDeclaration;
import org.mule.runtime.extension.xml.dsl.api.property.XmlModelProperty;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseXmlDeclarationTestCase
{

    protected static final String NAMESPACE = "mockns";
    protected static final String NAMESPACE_URI = "http://www.mulesoft.org/schema/mule/mockns";
    protected static final String SCHEMA_LOCATION = "http://www.mulesoft.org/schema/mule/mockns/current/mule-mockns.xsd";
    protected static final String PARAMETER_NAME = "myCamelCaseName";
    protected static final String EXTENSION_NAME = "extension";
    protected static final String OPERATION_NAME = "mockOperation";
    protected static final String SOURCE_NAME = "source";
    protected static final String CONFIGURATION_NAME = "configuration";
    protected static final String CONNECTION_PROVIDER_NAME = "connection";
    protected static final String IMPORT_NAMESPACE = "importns";
    protected static final String IMPORT_NAMESPACE_URI = "http://www.mulesoft.org/schema/mule/importns";
    protected static final String IMPORT_EXTENSION_NAME = "importExtension";
    protected static final BaseTypeBuilder<?> TYPE_BUILDER = BaseTypeBuilder.create(JAVA);

    @Mock
    protected ExtensionModel extension;

    @Mock
    protected ConfigurationModel configuration;

    @Mock
    protected OperationModel operation;

    @Mock
    protected ConnectionProviderModel connectionProvider;

    @Mock
    protected ParameterModel parameterModel;

    @Mock
    protected SourceModel source;

    protected ClassTypeLoader TYPE_LOADER = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

    @Before
    public void before()
    {
        when(extension.getName()).thenReturn(EXTENSION_NAME);
        when(extension.getConfigurationModels()).thenReturn(asList(configuration));
        when(extension.getConfigurationModels()).thenReturn(asList(configuration));
        when(extension.getOperationModels()).thenReturn(asList(operation));
        when(extension.getSourceModels()).thenReturn(asList(source));
        when(extension.getConnectionProviders()).thenReturn(asList(connectionProvider));

        when(extension.getModelProperty(SubTypesModelProperty.class)).thenReturn(Optional.empty());
        when(extension.getModelProperty(ImportedTypesModelProperty.class)).thenReturn(Optional.empty());
        when(extension.getModelProperty(XmlModelProperty.class)).thenReturn(
                Optional.of(new XmlModelProperty(EMPTY, NAMESPACE, NAMESPACE_URI, EMPTY, SCHEMA_LOCATION)));

        when(configuration.getOperationModels()).thenReturn(asList(operation));
        when(configuration.getSourceModels()).thenReturn(asList(source));
        when(configuration.getConnectionProviders()).thenReturn(asList(connectionProvider));

        when(parameterModel.getName()).thenReturn(PARAMETER_NAME);
        when(parameterModel.getExpressionSupport()).thenReturn(ExpressionSupport.SUPPORTED);

        when(source.getName()).thenReturn(SOURCE_NAME);
        when(operation.getName()).thenReturn(OPERATION_NAME);
        when(configuration.getName()).thenReturn(CONFIGURATION_NAME);
        when(connectionProvider.getName()).thenReturn(CONNECTION_PROVIDER_NAME);

        Stream.of(configuration, operation, connectionProvider, source).forEach(
                model -> when(model.getParameterModels()).thenReturn(asList(parameterModel)));
    }

    protected void assertChildElementDeclarationIs(boolean expected, DslElementDeclaration result)
    {
        assertThat("Expected attribute only declaration", result.supportsChildDeclaration(), is(expected));
    }

    protected void assertIsWrappedElement(boolean expected, DslElementDeclaration result)
    {
        assertThat("Expected no wrapping but element is wrapped", result.isWrapped(), is(expected));
    }

    protected void assertAttributeName(String expected, DslElementDeclaration result)
    {
        assertThat(result.getAttributeName(), equalTo(expected));
    }

    protected void assertElementName(String expected, DslElementDeclaration result)
    {
        assertThat(result.getElementName(), equalTo(expected));
    }

    protected void assertElementNamespace(String expected, DslElementDeclaration result)
    {
        assertThat(result.getElementNamespace(), equalTo(expected));
    }

}
