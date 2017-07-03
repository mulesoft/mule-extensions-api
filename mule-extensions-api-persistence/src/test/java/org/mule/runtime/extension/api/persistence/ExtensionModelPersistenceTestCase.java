/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mule.metadata.java.api.utils.JavaTypeUtils.getType;
import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.api.dsl.model.ComplexFieldsType;
import org.mule.runtime.extension.api.dsl.model.ExtensibleType;
import org.mule.runtime.extension.api.model.ImmutableExtensionModel;
import org.mule.runtime.extension.api.model.connection.ImmutableConnectionProviderModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class ExtensionModelPersistenceTestCase extends BasePersistenceTestCase {

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

    assertComplexParameter(complexParameter);
  }

  @Test
  public void messageSourceCorrectlyDeserialized() {
    SourceModel sourceModel = deserializedExtensionModel.getSourceModel(SOURCE_NAME).get();
    assertThat(sourceModel.getAllParameterModels().size(), is(3));

    ParameterModel complexParameter = sourceModel.getAllParameterModels().stream()
        .filter(p -> p.getName().equals(COMPLEX_PARAMETER_NAME))
        .findFirst().get();

    assertComplexParameter(complexParameter);
    assertThat(sourceModel.getSuccessCallback().isPresent(), is(true));
    assertThat(sourceModel.getErrorCallback().isPresent(), is(false));
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
    final String serializedList = extensionModelJsonSerializer.serializeList(extensionModelList);
    JSONAssert.assertEquals(getResourceAsString(LIST_OF_SERIALIZED_EXTENSION_MODEL_JSON), serializedList, true);
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

  private void assertComplexParameter(ParameterModel complexParameter) {
    MetadataType type = complexParameter.getType();
    assertThat(type, is(instanceOf(ObjectType.class)));
    assertThat(getType(type), equalTo(ComplexFieldsType.class));
  }
}
