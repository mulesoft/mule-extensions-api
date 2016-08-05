/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mule.metadata.utils.MetadataTypeUtils.getSingleAnnotation;
import static org.mule.runtime.extension.api.Category.COMMUNITY;
import static org.mule.runtime.extension.api.introspection.connection.ConnectionManagementType.NONE;
import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.extension.api.persistence.JsonSerializationConstants.LAYOUT_MODEL_PROPERTY;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.MuleVersion;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.ImmutableExtensionModel;
import org.mule.runtime.extension.api.introspection.ImmutableOutputModel;
import org.mule.runtime.extension.api.introspection.ImmutableRuntimeExtensionModel;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderFactory;
import org.mule.runtime.extension.api.introspection.connection.ImmutableConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.connection.ImmutableRuntimeConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.TypeAliasAnnotation;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.api.introspection.operation.ImmutableOperationModel;
import org.mule.runtime.extension.api.introspection.parameter.ImmutableParameterModel;
import org.mule.runtime.extension.api.introspection.property.LayoutModelProperty;
import org.mule.runtime.extension.api.persistence.model.ComplexFieldsType;
import org.mule.runtime.extension.api.persistence.model.ExtensibleType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ExtensionModelPersistenceTestCase extends BasePersistenceTestCase
{

    private static final String SERIALIZED_EXTENSION_MODEL_JSON = "extension/serialized-extension-model.json";
    private static final String LIST_OF_SERIALIZED_EXTENSION_MODEL_JSON = "extension/list-of-serialized-extension-model.json";
    private final NonExternalizableModelProperty nonExternalizableModelProperty = new NonExternalizableModelProperty();
    private final ExternalizableModelProperty externalizableModelProperty = new ExternalizableModelProperty();
    private final Set<ModelProperty> modelProperties = new HashSet<>(asList(nonExternalizableModelProperty, externalizableModelProperty));
    private final MetadataType stringType = typeLoader.load(String.class);
    private final String SERIALIZED_DISPLAY_MODEL_PROPERTY = "{\"password\":false,\"text\":true,\"order\":0}";
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
    private List<ExtensionModel> extensionModelList;
    private ExtensionModelJsonSerializer extensionModelJsonSerializer;

    @Before
    public void setUp()
    {
        final ImmutableParameterModel carNameParameter = new ImmutableParameterModel(CAR_NAME_PARAMETER_NAME, "Name of the car", stringType, false, true, SUPPORTED, "", singleton(new LayoutModelProperty(false, true, 0, null, null)));
        final ImmutableParameterModel usernameParameter = new ImmutableParameterModel("username", "Username", stringType, true, true, SUPPORTED, "", singleton(new LayoutModelProperty(false, true, 0, null, null)));
        final ImmutableParameterModel passwordParameter = new ImmutableParameterModel("password", "Password", stringType, false, true, SUPPORTED, "", singleton(new LayoutModelProperty(true, true, 0, null, null)));
        final ImmutableParameterModel complexParameter = new ImmutableParameterModel("complex", "complex type to serialize",
                                                                                     ExtensionsTypeLoaderFactory.getDefault().createTypeLoader().load(ComplexFieldsType.class),
                                                                                     false, true, SUPPORTED, null, emptySet());

        getCarOperation = new ImmutableOperationModel(GET_CAR_OPERATION_NAME, "Obtains a car", asList(carNameParameter, complexParameter),
                                                      new ImmutableOutputModel("MuleMessage.Payload", stringType, true, emptySet()),
                                                      new ImmutableOutputModel("MuleMessage.Attributes", stringType, false, emptySet()),
                                                      modelProperties);
        final ImmutableRuntimeConnectionProviderModel basicAuth = new ImmutableRuntimeConnectionProviderModel("BasicAuth", "Basic Auth Config", Integer.class, new DefaultConnectionProviderFactory(), asList(usernameParameter, passwordParameter), NONE, emptySet());
        originalExtensionModel = new ImmutableRuntimeExtensionModel("DummyExtension", "Test extension", "4.0.0", "MuleSoft", COMMUNITY, new MuleVersion("4.0"), emptyList(), singletonList(getCarOperation), singletonList(basicAuth), emptyList(), emptySet(), Optional.empty());
        extensionModelJsonSerializer = new ExtensionModelJsonSerializer(true);
        final String serializedExtensionModelString = extensionModelJsonSerializer.serialize(originalExtensionModel);
        serializedExtensionModel = new JsonParser().parse(serializedExtensionModelString);
        deserializedExtensionModel = extensionModelJsonSerializer.deserialize(serializedExtensionModelString);
        operationModelProperties = serializedExtensionModel.getAsJsonObject().get(OPERATIONS_NODE).getAsJsonObject().get(GET_CAR_OPERATION_NAME).getAsJsonObject().get(MODEL_PROPERTIES_NODE).getAsJsonObject();
        extensionModelList = asList(deserializedExtensionModel, originalExtensionModel);
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
        assertThat(getModelProperty(carNameParameter, LAYOUT_MODEL_PROPERTY).toString(), is(SERIALIZED_DISPLAY_MODEL_PROPERTY));
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
        final JsonElement expectedSerializedExtensionModel = new JsonParser().parse(getResourceAsString(SERIALIZED_EXTENSION_MODEL_JSON));
        assertThat(serializedExtensionModel, is(expectedSerializedExtensionModel));
    }

    @Test
    public void validateJsonListStructure() throws IOException
    {
        final JsonParser jsonParser = new JsonParser();
        final JsonElement expectedSerializedExtensionModel = jsonParser.parse(getResourceAsString(LIST_OF_SERIALIZED_EXTENSION_MODEL_JSON));
        final String serializedList = extensionModelJsonSerializer.serializeList(extensionModelList);
        final JsonElement parse = jsonParser.parse(serializedList);

        assertThat(parse, is(expectedSerializedExtensionModel));
    }

    @Test
    public void validateCustomTypeAnnotations() throws IOException
    {
        MetadataType complexType = deserializedExtensionModel.getOperationModels().get(0).getParameterModels().get(1).getType();


        assertThat(complexType, instanceOf(ObjectType.class));
        assertThat(getSingleAnnotation(complexType, TypeAliasAnnotation.class).isPresent(), is(true));
        assertThat(getSingleAnnotation(complexType, TypeAliasAnnotation.class).get().getValue(), is(ComplexFieldsType.ALIAS));

        ArrayType extensibleTypeList = (ArrayType) ((ObjectType) complexType).getFieldByName("extensibleTypeList").get().getValue();
        assertThat(getSingleAnnotation(extensibleTypeList.getType(), ExtensibleTypeAnnotation.class).isPresent(), is(true));
        assertThat(getSingleAnnotation(extensibleTypeList.getType(), TypeAliasAnnotation.class).get().getValue(), is(ExtensibleType.ALIAS));

        ObjectType simplePojo = (ObjectType) ((ObjectType) complexType).getFieldByName("simplePojo").get().getValue();
        assertThat(getSingleAnnotation(simplePojo.getFieldByName("sampleString").get(), XmlHintsAnnotation.class).get().allowsReferences(), is(false));

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
