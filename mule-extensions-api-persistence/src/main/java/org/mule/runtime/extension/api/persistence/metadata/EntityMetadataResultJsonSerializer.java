/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata;

import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import org.mule.runtime.api.metadata.descriptor.ImmutableTypeMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Serializer that can convert a {@link MetadataResult} of {@link TypeMetadataDescriptor} type into a readable and processable
 * JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
 *
 * @since 1.0
 */
public class EntityMetadataResultJsonSerializer extends AbstractMetadataResultJsonSerializer {

  public EntityMetadataResultJsonSerializer(boolean prettyPrint) {
    super(prettyPrint);
  }

  @Override
  public String serialize(MetadataResult result) {
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

    private final static String ENTITIES = "ENTITIES";

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
      TypeMetadataDescriptor result = new ImmutableTypeMetadataDescriptor(entity != null ? entity.getType() : null, true);
      return failures.isEmpty() ? success(result) : failure(result, failures);
    }
  }

}
