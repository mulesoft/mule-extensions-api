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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mule.runtime.api.meta.Category.COMMUNITY;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.meta.model.ExecutionType.CPU_LITE;
import static org.mule.runtime.api.meta.model.connection.ConnectionManagementType.NONE;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.api.meta.model.tck.TestWebServiceConsumerDeclarer.EXTERNAL_LIBRARY_MODEL;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataFormat;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ExternalLibraryModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.error.ErrorModelBuilder;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.declaration.type.DefaultExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.dsl.model.ComplexFieldsType;
import org.mule.runtime.extension.api.model.ImmutableExtensionModel;
import org.mule.runtime.extension.api.model.ImmutableOutputModel;
import org.mule.runtime.extension.api.model.connection.ImmutableConnectionProviderModel;
import org.mule.runtime.extension.api.model.operation.ImmutableOperationModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableExclusiveParametersModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableParameterGroupModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableParameterModel;
import org.mule.runtime.extension.api.model.source.ImmutableSourceCallbackModel;
import org.mule.runtime.extension.api.model.source.ImmutableSourceModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;

abstract class BasePersistenceTestCase {

  protected static final String SERIALIZED_EXTENSION_MODEL_JSON = "extension/serialized-extension-model.json";
  protected static final String LIST_OF_SERIALIZED_EXTENSION_MODEL_JSON = "extension/list-of-serialized-extension-model.json";
  protected static final ErrorModel PARENT_ERROR_MODEL =
      ErrorModelBuilder.newError("PARENT_ERROR_MODEL", "ERROR_NAMESPACE").build();
  protected static final ErrorModel ERROR_MODEL =
      ErrorModelBuilder.newError("SOME_ERROR", "ERROR_NAMESPACE").withParent(PARENT_ERROR_MODEL).build();

  protected final DisplayModel defaultDisplayModel = DisplayModel.builder().build();
  protected final ClassTypeLoader typeLoader = new DefaultExtensionsTypeLoaderFactory().createTypeLoader();
  protected final BaseTypeBuilder typeBuilder = BaseTypeBuilder.create(MetadataFormat.JAVA);
  protected final MetadataType stringType = typeLoader.load(String.class);
  protected final String GET_CAR_OPERATION_NAME = "getCar";
  protected final String CAR_NAME_PARAMETER_NAME = "carName";
  protected final String MODEL_PROPERTIES_NODE = "modelProperties";
  protected final String OPERATIONS_NODE = "operations";
  protected final String COMPLEX_PARAMETER_NAME = "complex";
  protected final String OBJECT_MAP_NAME = "map";

  protected final String SOURCE_NAME = "Source";
  protected final ParameterDslConfiguration defaultParameterDsl = ParameterDslConfiguration.getDefaultInstance();
  protected final LayoutModel defaultLayoutModel = LayoutModel.builder().build();

  protected final NonExternalizableModelProperty nonExternalizableModelProperty = new NonExternalizableModelProperty();
  protected final ExternalizableModelProperty externalizableModelProperty = new ExternalizableModelProperty();
  protected final Set<ModelProperty> modelProperties =
      new HashSet<>(asList(nonExternalizableModelProperty, externalizableModelProperty));

  protected ExtensionModel deserializedExtensionModel;
  protected ExtensionModel originalExtensionModel;
  protected OperationModel getCarOperation;
  protected SourceModel sourceModel;
  protected JsonElement serializedExtensionModel;
  protected JsonObject operationModelProperties;
  protected List<ExtensionModel> extensionModelList;
  protected ExtensionModelJsonSerializer extensionModelJsonSerializer;
  protected ObjectType exportedType;

  @Before
  public void setUp() {
    final ImmutableParameterModel carNameParameter =
        new ImmutableParameterModel(CAR_NAME_PARAMETER_NAME, "Name of the car", stringType, true, false, SUPPORTED, "",
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
                                    typeBuilder.objectType()
                                        .id(HashMap.class.getName())
                                        .openWith(exportedType)
                                        .build(),

                                    false, true, SUPPORTED, null, BEHAVIOUR, defaultParameterDsl,
                                    defaultDisplayModel, defaultLayoutModel, emptySet());

    final ImmutableOutputModel outputModel = new ImmutableOutputModel("Message.Payload", stringType, true, emptySet());
    final ImmutableOutputModel outputAttributesModel =
        new ImmutableOutputModel("Message.Attributes", stringType, false, emptySet());

    getCarOperation =
        new ImmutableOperationModel(GET_CAR_OPERATION_NAME, "Obtains a car", asParameterGroup(carNameParameter, complexParameter),
                                    outputModel,
                                    outputAttributesModel,
                                    true, CPU_LITE, false, false, defaultDisplayModel,
                                    singleton(ERROR_MODEL), modelProperties);

    final ImmutableConnectionProviderModel basicAuth =
        new ImmutableConnectionProviderModel("BasicAuth",
                                             "Basic Auth Config",
                                             asParameterGroup(usernameParameter, passwordParameter, objectMap),
                                             NONE,
                                             externalLibrarySet(),
                                             defaultDisplayModel,
                                             emptySet());

    sourceModel = new ImmutableSourceModel(SOURCE_NAME, "A Message Source", true,
                                           asParameterGroup(carNameParameter),
                                           outputModel, outputAttributesModel,
                                           Optional
                                               .of(new ImmutableSourceCallbackModel("onSuccess", "",
                                                                                    asParameterGroup(
                                                                                                     complexParameter),
                                                                                    DisplayModel
                                                                                        .builder()
                                                                                        .build(),
                                                                                    emptySet())),
                                           Optional.empty(), false, false,
                                           DisplayModel.builder().build(),
                                           emptySet());


    originalExtensionModel =
        new ImmutableExtensionModel("DummyExtension", "Test extension", "4.0.0", "MuleSoft", COMMUNITY,
                                    new MuleVersion("4.0"), emptyList(), singletonList(getCarOperation),
                                    singletonList(basicAuth), singletonList(sourceModel),
                                    defaultDisplayModel, XmlDslModel.builder().build(),
                                    emptySet(), singleton(exportedType), emptySet(), emptySet(), singleton(ERROR_MODEL),
                                    externalLibrarySet(), emptySet());

    extensionModelJsonSerializer = new ExtensionModelJsonSerializer(true);
    final String serializedExtensionModelString =
        extensionModelJsonSerializer.serialize(originalExtensionModel);
    serializedExtensionModel = new JsonParser().parse(serializedExtensionModelString);
    deserializedExtensionModel = extensionModelJsonSerializer.deserialize(serializedExtensionModelString);
    operationModelProperties = serializedExtensionModel.getAsJsonObject().get(OPERATIONS_NODE).getAsJsonArray()
        .get(0).getAsJsonObject().get(MODEL_PROPERTIES_NODE).getAsJsonObject();
    extensionModelList = asList(deserializedExtensionModel, originalExtensionModel);
  }

  protected String getResourceAsString(String fileName) throws IOException {
    final InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
    return IOUtils.toString(resourceAsStream);
  }

  void assertSerializedJson(String serializedResult, String expectedFileName) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String resource = getResourceAsString(expectedFileName);
    final JsonParser jsonParser = new JsonParser();
    JsonElement expected = jsonParser.parse(resource);
    JsonElement result = jsonParser.parse(serializedResult);
    if (!result.equals(expected)) {
      System.out.println("Expected: \n " + gson.toJson(expected));
      System.out.println("\n\nBut Got: \n " + gson.toJson(result));
    }

    assertThat(result, is(expected));
  }

  public static class NonExternalizableModelProperty implements ModelProperty {

    @Override
    public String getName() {
      return "NonExternalizableModelProperty";
    }

    @Override
    public boolean isPublic() {
      return false;
    }
  }


  public static class ExternalizableModelProperty implements ModelProperty {

    @Override
    public String getName() {
      return "ExternalizableModelProperty";
    }

    @Override
    public boolean isPublic() {
      return true;
    }
  }

  public static class ExportedClass {
  }

  private Set<ExternalLibraryModel> externalLibrarySet() {
    Set<ExternalLibraryModel> externalLibraryModels = new HashSet<>();
    externalLibraryModels.add(EXTERNAL_LIBRARY_MODEL);

    return externalLibraryModels;
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
                                                   false,
                                                   null,
                                                   null,
                                                   emptySet()));
  }
}
