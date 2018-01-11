/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.mule.runtime.api.component.ComponentIdentifier.buildFromStringRepresentation;
import static org.mule.runtime.api.meta.model.error.ErrorModelBuilder.newError;
import static org.mule.runtime.extension.internal.persistence.ErrorModelToIdentifierSerializer.serialize;
import static org.mule.runtime.extension.internal.persistence.ExtensionModelTypeAdapter.ERRORS;
import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.error.ErrorModelBuilder;
import org.mule.runtime.api.util.Pair;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Helper class for {@link ExtensionModelTypeAdapter} which encapsulates the logic of serializing and
 * deserializing {@link ErrorModel}
 *
 * @since 1.0
 */
class ErrorModelSerializerDelegate {

  private static final String MULE = "MULE";
  private static final String ERROR = "error";
  private static final String PARENT = "parent";
  private static final String HANDLEABLE = "handleable";
  private static final String EMPTY = "";
  private Map<String, ErrorModel> errorModelRespository;

  ErrorModelSerializerDelegate(Map<String, ErrorModel> errorModelRespository) {
    this.errorModelRespository = errorModelRespository;
  }

  /**
   * Serializes a {@link Set} of {@link ErrorModel}. This Serializer will only serialize the identifier of the
   * proper error model and the identifier of the parent.
   *
   * @param errorModels Errors to serialize
   * @param out         json writer where the serialized set will be written
   * @throws IOException if an error occurs trying to serialize the errors
   */
  void writeErrors(Set<ErrorModel> errorModels, JsonWriter out) throws IOException {
    Set<ErrorModel> models = flatenizeErrors(errorModels);
    out.name(ERRORS);
    out.beginArray();
    for (ErrorModel errorModel : models) {
      writeError(out, errorModel);
    }
    out.endArray();
  }

  private void writeError(JsonWriter out, ErrorModel errorModel) throws IOException {
    out.beginObject();
    out.name(ERROR).value(serialize(errorModel));
    if (errorModel.getParent().isPresent()) {
      out.name(PARENT).value(serialize(errorModel.getParent().get()));
    }
    out.name(HANDLEABLE).value(errorModel.isHandleable());
    out.endObject();
  }

  private Set<ErrorModel> flatenizeErrors(Set<ErrorModel> errorModels) {
    Set<ErrorModel> models = new LinkedHashSet<>();
    errorModels.forEach(model -> {
      models.add(model);
      Optional<ErrorModel> parent = model.getParent();
      while (parent.isPresent()) {
        ErrorModel parentModel = parent.get();
        models.add(parentModel);
        parent = parentModel.getParent();
      }
    });
    return models;
  }

  /**
   * Given a {@link JsonArray} representing a {@link Set} of {@link ErrorModel}, it will deserialize them.
   * Also contribute with the given {@link this#errorModelRespository}.
   *
   * @param errors The json array
   * @return The a {@link Map} with the Error Identifier as key and the represented {@link ErrorModel}
   */
  Map<String, ErrorModel> parseErrors(JsonArray errors) {
    Map<String, Pair<String, ErrorModelBuilder>> buildingErrors = new LinkedHashMap<>();

    errors.iterator().forEachRemaining(element -> {
      JsonObject error = element.getAsJsonObject();
      String anError = error.get(ERROR).getAsString();
      String parentError = EMPTY;
      if (error.has(PARENT)) {
        parentError = error.get(PARENT).getAsString();
      }
      ErrorModelBuilder errorModelBuilder = newError(buildFromStringRepresentation(anError));
      if (error.has(HANDLEABLE)) {
        errorModelBuilder.handleable(error.get(HANDLEABLE).getAsBoolean());
      }
      buildingErrors.put(anError, new Pair<>(parentError, errorModelBuilder));
    });

    buildingErrors.keySet().forEach(key -> buildError(key, buildingErrors, errorModelRespository));

    return errorModelRespository;
  }

  private ErrorModel buildError(String errorIdentifier, Map<String, Pair<String, ErrorModelBuilder>> buildingErrors,
                                Map<String, ErrorModel> builtErrorModels) {
    if (builtErrorModels.containsKey(errorIdentifier)) {
      return builtErrorModels.get(errorIdentifier);
    } else {
      Pair<String, ErrorModelBuilder> builderPair = buildingErrors.get(errorIdentifier);

      if (builderPair != null) {
        String parentError = builderPair.getFirst();
        ErrorModel errorModel;
        ErrorModelBuilder errorBuilder = builderPair.getSecond();

        if (!isEmpty(parentError)) {
          errorModel = errorBuilder.withParent(buildError(parentError, buildingErrors, builtErrorModels)).build();
        } else {
          errorModel = errorBuilder.build();
        }

        builtErrorModels.put(errorIdentifier, errorModel);

        return errorModel;
      } else {
        ComponentIdentifier identifier = buildFromStringRepresentation(errorIdentifier);
        return buildSimpleError(identifier, builtErrorModels);
      }
    }
  }

  private ErrorModel buildSimpleError(ComponentIdentifier identifier, Map<String, ErrorModel> builtErrors) {
    ErrorModel errorModel = newError(identifier).build();
    builtErrors.put(identifier.toString(), errorModel);
    return errorModel;
  }
}
