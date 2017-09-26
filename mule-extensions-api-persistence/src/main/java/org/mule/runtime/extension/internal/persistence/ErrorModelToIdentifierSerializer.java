/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import static java.util.Collections.emptyMap;
import static org.mule.runtime.api.component.ComponentIdentifier.buildFromStringRepresentation;
import static org.mule.runtime.api.meta.model.error.ErrorModelBuilder.newError;

import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.api.meta.model.error.ErrorModel;

import java.util.Map;

/**
 * JSON Serializer for {@link ErrorModel} instances as error identifiers.
 *
 * @since 1.0
 */
public class ErrorModelToIdentifierSerializer {

  /**
   * Deserializes an error model identifier into an {@link ErrorModel}. Uses an Error Model Repository to be able
   * to rebuild the entire error model hierarchy, if it exist.
   *
   * @param errorIdentifier      Error following the following structure {@code nameSpace:errorType}, example:
   *                             {@code MULE:CONNECTIVITY}
   * @param errorModelRepository Repository of already built {@link ErrorModel error models}
   * @return An {@link ErrorModel} representing the given errorIdentifier
   */
  public static ErrorModel deserialize(String errorIdentifier, Map<String, ErrorModel> errorModelRepository) {
    boolean exists = errorModelRepository.containsKey(errorIdentifier);
    if (exists) {
      return errorModelRepository.get(errorIdentifier);
    } else {
      return createErrorModel(errorIdentifier);
    }
  }

  /**
   * Deserializes an error model identifier into an {@link ErrorModel}.
   *
   * @param errorIdentifier Error following the following structure {@code nameSpace:errorType}, example:
   *                        {@code MULE:CONNECTIVITY}
   * @return An {@link ErrorModel} representing the given errorIdentifier
   */
  public static ErrorModel deserialize(String errorIdentifier) {
    return deserialize(errorIdentifier, emptyMap());
  }

  /**
   * Serializes an {@link ErrorModel} to the following structure: {@code nameSpace:errorType}
   *
   * @param errorModel Error to serialize
   * @return Serialized error
   */
  public static String serialize(ErrorModel errorModel) {
    return errorModel.getNamespace() + ":" + errorModel.getType();
  }

  private static ErrorModel createErrorModel(String errorIdentifier) {
    ComponentIdentifier componentIdentifier = buildFromStringRepresentation(errorIdentifier);
    return newError(componentIdentifier).build();
  }
}
