/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import static java.nio.file.Files.createTempFile;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mule.runtime.api.meta.Category.COMMUNITY;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.meta.model.ExecutionType.CPU_LITE;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.json.JsonTypeLoader;
import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.declaration.type.DefaultExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.model.ImmutableExtensionModel;
import org.mule.runtime.extension.api.model.ImmutableOutputModel;
import org.mule.runtime.extension.api.model.operation.ImmutableOperationModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableExclusiveParametersModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableParameterGroupModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableParameterModel;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class LoadAndSerializeXmlExtensionModelsTestCase {

  public static final String SCHEMAS_GET_JOB_JSON = "schemas/greenhouse-get-job.json";
  public static final String SERIALIZED_EXTENSION_MODEL_STRING_JSON = "serializedExtensionModelString.json";
  public static final String XML_BASED_EXT_MODEL_JSON = "extension/xml-based-ext-model.json";
  protected final String LOADED_PARAMETER_NAME = "loaded";
  protected final String GET_CAR_OPERATION_NAME = "getCar";
  protected final DisplayModel defaultDisplayModel = DisplayModel.builder().build();
  protected final LayoutModel defaultLayoutModel = LayoutModel.builder().build();
  protected final ParameterDslConfiguration defaultParameterDsl = ParameterDslConfiguration.getDefaultInstance();
  protected final ClassTypeLoader typeLoader = new DefaultExtensionsTypeLoaderFactory().createTypeLoader();
  protected final MetadataType stringType = typeLoader.load(String.class);

  protected ExtensionModel originalExtensionModel;
  protected JsonElement serializedExtensionModel;
  protected ExtensionModelJsonSerializer extensionModelJsonSerializer;

  @Before
  public void setup() throws IOException {
    String schema = IOUtils.toString(Thread.currentThread().getContextClassLoader()
        .getResourceAsStream(SCHEMAS_GET_JOB_JSON));
    final MetadataType jsonLoadedType = new JsonTypeLoader(schema).load("").get();
    final ImmutableParameterModel loadedParameter =
        new ImmutableParameterModel(LOADED_PARAMETER_NAME, "loaded type from json to serialize",
                                    jsonLoadedType,
                                    false, true, false, SUPPORTED, null, BEHAVIOUR, defaultParameterDsl,
                                    defaultDisplayModel, defaultLayoutModel, null, emptyList(), emptySet());

    final ImmutableOutputModel outputModel = new ImmutableOutputModel("Message.Payload", stringType, true, emptySet());
    final ImmutableOutputModel outputAttributesModel =
        new ImmutableOutputModel("Message.Attributes", stringType, false, emptySet());

    ImmutableOperationModel getCarOperation = new ImmutableOperationModel(GET_CAR_OPERATION_NAME, "Obtains a car",
                                                                          asParameterGroup(loadedParameter),
                                                                          outputModel,
                                                                          outputAttributesModel,
                                                                          true, CPU_LITE, false, false, false,
                                                                          defaultDisplayModel,
                                                                          emptySet(), emptySet(), emptySet());

    originalExtensionModel =
        new ImmutableExtensionModel("DummyExtension", "Test extension", "4.0.0", "MuleSoft",
                                    COMMUNITY, new MuleVersion("4.0"), emptyList(),
                                    singletonList(getCarOperation),
                                    emptyList(), emptyList(), defaultDisplayModel, XmlDslModel.builder().build(),
                                    emptySet(), emptySet(), emptySet(), emptySet(), emptySet(), emptySet(), emptySet());

    extensionModelJsonSerializer = new ExtensionModelJsonSerializer(true);

  }

  @Test
  public void serializeDeserializeExternalModel() throws IOException {
    String external =
        IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream(XML_BASED_EXT_MODEL_JSON));
    ExtensionModel externalModel = extensionModelJsonSerializer.deserialize(external);
    String serializedResult = extensionModelJsonSerializer.serialize(externalModel);

    assertThat(external, is(equalTo(serializedResult)));
  }

  @Test
  public void serializeDeserializeMock() throws IOException {

    final String serializedExtensionModelString = extensionModelJsonSerializer.serialize(originalExtensionModel);

    FileUtils.writeStringToFile(createTempFile(SERIALIZED_EXTENSION_MODEL_STRING_JSON, ".tmp").toFile(),
                                serializedExtensionModelString);

    serializedExtensionModel = new JsonParser().parse(serializedExtensionModelString);
    ExtensionModel deserializedExtensionModel = extensionModelJsonSerializer.deserialize(serializedExtensionModelString);

    assertThat(originalExtensionModel, is(equalTo(deserializedExtensionModel)));
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
