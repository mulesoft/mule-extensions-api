/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mule.extension.api.persistence.JsonSerializationConstants.DISPLAY_MODEL_PROPERTY;

import org.mule.api.connection.ConnectionProvider;
import org.mule.extension.api.introspection.ConnectionProviderFactory;
import org.mule.extension.api.introspection.ExpressionSupport;
import org.mule.extension.api.introspection.ExtensionModel;
import org.mule.extension.api.introspection.ImmutableConnectionProviderModel;
import org.mule.extension.api.introspection.ImmutableExtensionModel;
import org.mule.extension.api.introspection.ImmutableOperationModel;
import org.mule.extension.api.introspection.ImmutableParameterModel;
import org.mule.extension.api.introspection.ImmutableRuntimeConnectionProviderModel;
import org.mule.extension.api.introspection.ImmutableRuntimeExtensionModel;
import org.mule.extension.api.introspection.ModelProperty;
import org.mule.extension.api.introspection.property.DisplayModelProperty;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.java.JavaTypeLoader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ExtensionModelPersistenceTestCase
{

    private final NonExternalizableModelProperty nonExternalizableModelProperty = new NonExternalizableModelProperty();
    private final ExternalizableModelProperty externalizableModelProperty = new ExternalizableModelProperty();
    private final Set<ModelProperty> modelProperties = new HashSet<>(Arrays.asList(nonExternalizableModelProperty, externalizableModelProperty));
    private final JavaTypeLoader javaTypeLoader = new JavaTypeLoader(ExtensionModelPersistenceTestCase.class.getClassLoader());
    private final MetadataType stringType = javaTypeLoader.load(String.class);
    private final String SERIALIZED_DISPLAY_MODEL_PROPERTY = "{\"displayName\":\"Car Name\",\"password\":false,\"text\":true,\"order\":0}";
    private final String GET_CAR_OPERATION_NAME = "getCar";
    private final String CAR_NAME_PARAMETER_NAME = "carName";
    private final String MODEL_PROPERTIES_NODE = "modelProperties";
    private final String OPERATIONS_NODE = "operations";
    private final String PARAMETER_MODELS_NODE = "parameterModels";

    private ExtensionModel deserializedExtensionModel;
    private ImmutableExtensionModel originalExtensionModel;
    private ImmutableOperationModel getCarOperation;
    private JsonElement serializedExtensionModel;
    private JsonObject operationModelProperties;

    @Before
    public void setUp()
    {
        final ImmutableParameterModel carNameParameter = new ImmutableParameterModel(CAR_NAME_PARAMETER_NAME, "Name of the car", stringType, true, ExpressionSupport.SUPPORTED, "", Collections.singleton(new DisplayModelProperty("Car Name", false, true, 0, null, null)));
        final ImmutableParameterModel usernameParameter = new ImmutableParameterModel("username", "Username", stringType, true, ExpressionSupport.SUPPORTED, "", Collections.singleton(new DisplayModelProperty("Username", false, true, 0, null, null)));
        final ImmutableParameterModel passwordParameter = new ImmutableParameterModel("password", "Password", stringType, true, ExpressionSupport.SUPPORTED, "", Collections.singleton(new DisplayModelProperty("Password", true, true, 0, null, null)));

        getCarOperation = new ImmutableOperationModel(GET_CAR_OPERATION_NAME, "Obtains a car", Collections.singletonList(carNameParameter), stringType, stringType, modelProperties);
        final ImmutableRuntimeConnectionProviderModel<String, Integer> basicAuth = new ImmutableRuntimeConnectionProviderModel<>("BasicAuth", "Basic Auth Config", String.class, Integer.class, new DefaultConnectionProviderFactory(), Arrays.asList(usernameParameter, passwordParameter), Collections.emptySet());
        originalExtensionModel = new ImmutableRuntimeExtensionModel("DummyExtension", "Test extension", "4.0.0", "MuleSoft", Collections.emptyList(), Collections.singletonList(getCarOperation), Collections.singletonList(basicAuth), Collections.emptyList(), Collections.emptySet(), Optional.empty());
        final ExtensionModelJsonSerializer extensionModelJsonSerializer = new ExtensionModelJsonSerializer(true);
        final String serializedExtensionModelString = extensionModelJsonSerializer.serialize(originalExtensionModel);
        serializedExtensionModel = new JsonParser().parse(serializedExtensionModelString);
        deserializedExtensionModel = extensionModelJsonSerializer.deserialize(serializedExtensionModelString);
        operationModelProperties = serializedExtensionModel.getAsJsonObject().get(OPERATIONS_NODE).getAsJsonObject().get(GET_CAR_OPERATION_NAME).getAsJsonObject().get(MODEL_PROPERTIES_NODE).getAsJsonObject();
    }

    @Test
    public void friendlyNameIsUsedForAlreadyKnowModelProperty()
    {
        final JsonObject carNameParameter = serializedExtensionModel.getAsJsonObject()
                .get(OPERATIONS_NODE).getAsJsonObject()
                .get(GET_CAR_OPERATION_NAME).getAsJsonObject()
                .get(PARAMETER_MODELS_NODE).getAsJsonArray()
                .get(0).getAsJsonObject();

        assertThat(carNameParameter.get("name").getAsString(), is(CAR_NAME_PARAMETER_NAME));
        assertThat(getModelProperty(carNameParameter, DISPLAY_MODEL_PROPERTY).toString(), is(SERIALIZED_DISPLAY_MODEL_PROPERTY));
    }

    @Test
    public void fullQualifiedNameIsUsedForUnknownModelProperty()
    {
        assertThat(operationModelProperties.has(ExternalizableModelProperty.class.getName()), is(true));
    }

    @Test
    public void nonExternalizableModelsAreNotSerialized()
    {
        assertThat(getCarOperation.getModelProperties().contains(nonExternalizableModelProperty), is(true));
        assertThat(getCarOperation.getModelProperties().size(), is(2));

        assertThat(operationModelProperties.has(NonExternalizableModelProperty.class.getName()), is(false));
        assertThat(operationModelProperties.entrySet().size(), is(1));
    }

    @Test
    public void runtimeModelsAreDeserializedIntoNonRuntimeModels()
    {
        assertThat(originalExtensionModel, instanceOf(ImmutableRuntimeExtensionModel.class));
        assertThat(deserializedExtensionModel, instanceOf(ImmutableExtensionModel.class));

        assertThat(originalExtensionModel.getConnectionProviders().get(0), instanceOf(ImmutableRuntimeConnectionProviderModel.class));
        assertThat(deserializedExtensionModel.getConnectionProviders().get(0), instanceOf(ImmutableConnectionProviderModel.class));
    }

    @Test
    public void validateJsonStructure() throws IOException
    {
        final JsonElement expectedSerializedExtensionModel = new JsonParser().parse(getResourceAsString("serialized-extension-model.json"));
        assertThat(serializedExtensionModel.equals(expectedSerializedExtensionModel), is(true));
    }

    private JsonElement getModelProperty(JsonObject object, String modelPropertyName)
    {
        final JsonObject modelProperties = object.get(MODEL_PROPERTIES_NODE).getAsJsonObject();
        if (modelProperties.has(modelPropertyName))
        {
            return modelProperties.get(modelPropertyName);
        }
        return null;
    }

    private String getResourceAsString(String fileName) throws IOException
    {
        final InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        return IOUtils.toString(resourceAsStream);
    }

    private class DefaultConnectionProviderFactory implements ConnectionProviderFactory
    {

        @Override
        public ConnectionProvider newInstance()
        {
            return null;
        }

        @Override
        public Class<? extends ConnectionProvider> getObjectType()
        {
            return null;
        }
    }

    private class NonExternalizableModelProperty implements ModelProperty
    {

        @Override
        public String getName()
        {
            return "NonExternalizableModelProperty";
        }

        @Override
        public boolean isExternalizable()
        {
            return false;
        }
    }

    private class ExternalizableModelProperty implements ModelProperty
    {

        @Override
        public String getName()
        {
            return "ExternalizableModelProperty";
        }

        @Override
        public boolean isExternalizable()
        {
            return true;
        }
    }
}
