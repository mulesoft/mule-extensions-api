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
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mule.metadata.java.api.utils.JavaTypeUtils.getType;
import static org.mule.runtime.api.meta.Category.COMMUNITY;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.meta.model.connection.ConnectionManagementType.NONE;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataFormat;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ElementDslModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeAliasAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.api.model.ImmutableExtensionModel;
import org.mule.runtime.extension.api.model.ImmutableOutputModel;
import org.mule.runtime.extension.api.model.connection.ImmutableConnectionProviderModel;
import org.mule.runtime.extension.api.model.error.ErrorModelBuilder;
import org.mule.runtime.extension.api.model.operation.ImmutableOperationModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableExclusiveParametersModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableParameterGroupModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableParameterModel;
import org.mule.runtime.extension.api.persistence.model.ComplexFieldsType;
import org.mule.runtime.extension.api.persistence.model.ExtensibleType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class ExtensionModelPersistenceTestCase extends BasePersistenceTestCase {

  private static final String SERIALIZED_EXTENSION_MODEL_JSON = "extension/serialized-extension-model.json";
  private static final String LIST_OF_SERIALIZED_EXTENSION_MODEL_JSON = "extension/list-of-serialized-extension-model.json";
  private static final ErrorModel PARENT_ERROR_MODEL =
      ErrorModelBuilder.newError("PARENT_ERROR_MODEL", "ERROR_NAMESPACE").build();
  private static final ErrorModel ERROR_MODEL =
      ErrorModelBuilder.newError("SOME_ERROR", "ERROR_NAMESPACE").withParent(PARENT_ERROR_MODEL).build();

  private final BaseTypeBuilder typeBuilder = BaseTypeBuilder.create(MetadataFormat.JAVA);
  private final NonExternalizableModelProperty nonExternalizableModelProperty = new NonExternalizableModelProperty();
  private final ExternalizableModelProperty externalizableModelProperty = new ExternalizableModelProperty();
  private final Set<ModelProperty> modelProperties =
      new HashSet<>(asList(nonExternalizableModelProperty, externalizableModelProperty));

  private final MetadataType stringType = typeLoader.load(String.class);
  private final String GET_CAR_OPERATION_NAME = "getCar";
  private final String CAR_NAME_PARAMETER_NAME = "carName";
  private final String MODEL_PROPERTIES_NODE = "modelProperties";
  private final String OPERATIONS_NODE = "operations";
  private final String COMPLEX_PARAMETER_NAME = "complex";
  private final String OBJECT_MAP_NAME = "map";

  private ExtensionModel deserializedExtensionModel;
  private ImmutableExtensionModel originalExtensionModel;
  private ImmutableOperationModel getCarOperation;
  private JsonElement serializedExtensionModel;
  private JsonObject operationModelProperties;
  private List<ExtensionModel> extensionModelList;
  private ExtensionModelJsonSerializer extensionModelJsonSerializer;
  private ObjectType exportedType;
  private ElementDslModel defaultParameterDsl = ElementDslModel.getDefaultInstance();
  private DisplayModel defaultDisplayModel = DisplayModel.builder().build();
  private LayoutModel defaultLayoutModel = LayoutModel.builder().build();

  @Before
  public void setUp() {
    final ImmutableParameterModel carNameParameter =
        new ImmutableParameterModel(CAR_NAME_PARAMETER_NAME, "Name of the car", stringType, false, false, SUPPORTED, "",
                                    BEHAVIOUR, defaultParameterDsl, defaultDisplayModel, defaultLayoutModel, emptySet());
    final ImmutableParameterModel usernameParameter =
        new ImmutableParameterModel("username", "Username", stringType, true, true, SUPPORTED, "",
                                    BEHAVIOUR, defaultParameterDsl, defaultDisplayModel, defaultLayoutModel, emptySet());
    final ImmutableParameterModel passwordParameter =
        new ImmutableParameterModel("password", "Password", stringType, false, true, SUPPORTED, "",
                                    BEHAVIOUR, defaultParameterDsl, defaultDisplayModel, defaultLayoutModel, emptySet());
    final ImmutableParameterModel complexParameter =
        new ImmutableParameterModel(COMPLEX_PARAMETER_NAME, "complex type to serialize",
                                    ExtensionsTypeLoaderFactory.getDefault()
                                        .createTypeLoader()
                                        .load(ComplexFieldsType.class),
                                    false, true, SUPPORTED, null, BEHAVIOUR, defaultParameterDsl,
                                    defaultDisplayModel, defaultLayoutModel, emptySet());

    exportedType = typeBuilder.objectType().id("test.package.ExportedClass")
        .with(new ClassInformationAnnotation(ExportedClass.class, emptyList()))
        .with(new TypeAliasAnnotation(ExportedClass.class.getSimpleName())).build();

    final ImmutableParameterModel objectMap =
        new ImmutableParameterModel(OBJECT_MAP_NAME, "object map",
                                    typeBuilder.dictionaryType()
                                        .id(HashMap.class.getName())
                                        .ofKey(ExtensionsTypeLoaderFactory.getDefault().createTypeLoader().load(String.class))
                                        .ofValue(exportedType)
                                        .build(),

                                    false, true, SUPPORTED, null, BEHAVIOUR, defaultParameterDsl,
                                    defaultDisplayModel, defaultLayoutModel, emptySet());

    getCarOperation =
        new ImmutableOperationModel(GET_CAR_OPERATION_NAME, "Obtains a car", asParameterGroup(carNameParameter, complexParameter),
                                    new ImmutableOutputModel("Message.Payload", stringType, true, emptySet()),
                                    new ImmutableOutputModel("Message.Attributes", stringType, false, emptySet()),
                                    defaultDisplayModel, modelProperties, singleton(ERROR_MODEL));
    final ImmutableConnectionProviderModel basicAuth =
        new ImmutableConnectionProviderModel("BasicAuth",
                                             "Basic Auth Config",
                                             asParameterGroup(usernameParameter, passwordParameter, objectMap),
                                             NONE,
                                             defaultDisplayModel,
                                             emptySet());


    originalExtensionModel =
        new ImmutableExtensionModel("DummyExtension", "Test extension", "4.0.0", "MuleSoft", COMMUNITY,
                                    new MuleVersion("4.0"), emptyList(), singletonList(getCarOperation),
                                    singletonList(basicAuth), emptyList(),
                                    defaultDisplayModel, XmlDslModel.builder().build(),
                                    emptySet(), singleton(exportedType), emptySet(), emptySet(), singleton(ERROR_MODEL));

    extensionModelJsonSerializer = new ExtensionModelJsonSerializer(true);
    final String serializedExtensionModelString =
        extensionModelJsonSerializer.serialize(originalExtensionModel);
    serializedExtensionModel = new JsonParser().parse(serializedExtensionModelString);
    deserializedExtensionModel = extensionModelJsonSerializer.deserialize(serializedExtensionModelString);
    operationModelProperties = serializedExtensionModel.getAsJsonObject().get(OPERATIONS_NODE).getAsJsonArray()
        .get(0).getAsJsonObject().get(MODEL_PROPERTIES_NODE).getAsJsonObject();
    extensionModelList = asList(deserializedExtensionModel, originalExtensionModel);
  }

  @Test
  public void nonExternalizableModelsAreNotSerialized() {
    assertThat(getCarOperation.getModelProperties().contains(nonExternalizableModelProperty), is(true));
    assertThat(getCarOperation.getModelProperties().size(), is(2));

    assertThat(operationModelProperties.has(NonExternalizableModelProperty.class.getName()), is(false));
    assertThat(operationModelProperties.entrySet().size(), is(1));
  }

  @Test
  public void operationTypeCorrectlyDeserialized() {
    OperationModel operation = deserializedExtensionModel.getOperationModel(GET_CAR_OPERATION_NAME).get();
    ParameterModel complexParameter = operation.getAllParameterModels().stream()
        .filter(p -> p.getName().equals(COMPLEX_PARAMETER_NAME))
        .findFirst().get();

    MetadataType type = complexParameter.getType();
    assertThat(type, is(instanceOf(ObjectType.class)));
    assertThat(getType(type), equalTo(ComplexFieldsType.class));
  }

  @Test
  public void runtimeModelsAreDeserializedIntoNonRuntimeModels() {
    assertThat(deserializedExtensionModel, instanceOf(ImmutableExtensionModel.class));
    assertThat(deserializedExtensionModel.getConnectionProviders().get(0), instanceOf(ImmutableConnectionProviderModel.class));
  }

  @Test
  public void validateJsonStructure() throws IOException {
    final JsonElement expectedSerializedExtensionModel =
        new JsonParser().parse(getResourceAsString(SERIALIZED_EXTENSION_MODEL_JSON));
    assertThat(serializedExtensionModel, is(expectedSerializedExtensionModel));
  }

  @Test
  public void validateJsonListStructure() throws IOException {
    final JsonParser jsonParser = new JsonParser();
    final JsonElement expectedSerializedExtensionModel =
        jsonParser.parse(getResourceAsString(LIST_OF_SERIALIZED_EXTENSION_MODEL_JSON));
    final String serializedList = extensionModelJsonSerializer.serializeList(extensionModelList);
    final JsonElement parse = jsonParser.parse(serializedList);

    assertThat(parse, is(expectedSerializedExtensionModel));
  }

  @Test
  public void validateCustomTypeAnnotations() throws IOException {
    MetadataType complexType = deserializedExtensionModel.getOperationModels().get(0).getAllParameterModels().get(1).getType();
    assertThat(complexType, instanceOf(ObjectType.class));
    assertThat(complexType.getAnnotation(TypeAliasAnnotation.class).isPresent(), is(true));
    assertThat(complexType.getAnnotation(TypeAliasAnnotation.class).get().getValue(), is(ComplexFieldsType.ALIAS));

    ArrayType extensibleTypeList = (ArrayType) ((ObjectType) complexType).getFieldByName("extensibleTypeList").get().getValue();
    assertThat(extensibleTypeList.getType().getAnnotation(ExtensibleTypeAnnotation.class).isPresent(), is(true));
    assertThat(extensibleTypeList.getType().getAnnotation(TypeAliasAnnotation.class).get().getValue(), is(ExtensibleType.ALIAS));

    ObjectType simplePojo = (ObjectType) ((ObjectType) complexType).getFieldByName("simplePojo").get().getValue();
    assertThat(simplePojo.getFieldByName("sampleString").get().getAnnotation(XmlHintsAnnotation.class).get().allowsReferences(),
               is(false));
  }

  @Test
  public void enrichedTypesAreSerializated() throws IOException {
    Set<String> typesSet = getExtensionTypeIds(serializedExtensionModel.getAsJsonObject());
    assertThat(typesSet, hasItem(containsString(ExportedClass.class.getSimpleName())));
    assertThat(typesSet, not(hasItem(containsString(Object.class.getSimpleName()))));
    assertThat(typesSet, not(hasItem(containsString(ComplexFieldsType.class.getSimpleName()))));
    final Set<ObjectType> types = deserializedExtensionModel.getTypes();
    assertThat(types, hasItem(exportedType));
  }

  @Test
  public void minMuleVersionIsSerialized() {
    assertThat(originalExtensionModel.getMinMuleVersion().toCompleteNumericVersion(),
               is(deserializedExtensionModel.getMinMuleVersion().toCompleteNumericVersion()));
  }

  private Set<String> getExtensionTypeIds(JsonObject jsonExtensionModel) {
    final JsonArray typesArray = jsonExtensionModel.getAsJsonArray("types");
    Set<String> typesSet = new HashSet<>();
    for (JsonElement next : typesArray) {
      typesSet.add(next.getAsJsonObject().getAsJsonObject("annotations").get("typeId").getAsString());
    }
    return typesSet;
  }

  private List<ParameterGroupModel> asParameterGroup(ParameterModel... parameters) {
    Set<String> exclusiveParamNames = Stream.of(parameters)
        .filter(p -> !p.isRequired())
        .map(ParameterModel::getName)
        .collect(toSet());

    return asList(new ImmutableParameterGroupModel(DEFAULT_GROUP_NAME,
                                                   "",
                                                   asList(parameters),
                                                   asList(new ImmutableExclusiveParametersModel(exclusiveParamNames, false)),
                                                   null,
                                                   null,
                                                   emptySet()));
  }

  private class NonExternalizableModelProperty implements ModelProperty {

    @Override
    public String getName() {
      return "NonExternalizableModelProperty";
    }

    @Override
    public boolean isExternalizable() {
      return false;
    }
  }

  private class ExternalizableModelProperty implements ModelProperty {

    @Override
    public String getName() {
      return "ExternalizableModelProperty";
    }

    @Override
    public boolean isExternalizable() {
      return true;
    }
  }

  private class ExportedClass {
  }
}
