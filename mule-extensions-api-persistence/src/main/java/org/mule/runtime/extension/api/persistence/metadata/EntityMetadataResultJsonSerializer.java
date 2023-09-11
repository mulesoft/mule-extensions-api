/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata;

import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.runtime.extension.internal.persistence.metadata.TypeMetadata;

import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Serializer that can convert a {@link MetadataResult} of {@link TypeMetadataDescriptor} type into a readable and processable
 * JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
 *
 * @since 1.0
 */
public class EntityMetadataResultJsonSerializer extends AbstractMetadataResultJsonSerializer<TypeMetadataDescriptor> {

  public EntityMetadataResultJsonSerializer(boolean prettyPrint) {
    super(prettyPrint);
  }

  @Override
  public String serialize(MetadataResult<TypeMetadataDescriptor> result) {
    return gson.toJson(new EntityMetadataResult(result));
  }

  @Override
  public MetadataResult<TypeMetadataDescriptor> deserialize(String result) {
    EntityMetadataResult entityMetadataResult = gson.fromJson(result, new TypeToken<EntityMetadataResult>() {}.getType());
    return entityMetadataResult.toEntityMetadataResult();
  }

  /**
   * DTO that represents a {@link MetadataResult} of {@link TypeMetadataDescriptor} for serializing the metadata result of an
   * entity provided by DSQL.
   *
   * @since 1.0
   */
  private class EntityMetadataResult {

    private final TypeMetadata entity;
    private final List<MetadataFailure> failures;

    EntityMetadataResult(MetadataResult<TypeMetadataDescriptor> result) {
      entity = result.get() != null ? new TypeMetadata(result.get().getType(), false) : null;
      failures = result.getFailures();
    }

    TypeMetadata getEntity() {
      return entity;
    }

    MetadataResult<TypeMetadataDescriptor> toEntityMetadataResult() {
      if (entity == null) {
        return failure(failures);
      }

      TypeMetadataDescriptor result = TypeMetadataDescriptor.builder().withType(entity.getType()).build();
      return failures.isEmpty() ? success(result) : failure(result, failures);
    }
  }

}
