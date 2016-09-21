/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata;

import com.google.gson.reflect.TypeToken;
import org.mule.runtime.api.metadata.descriptor.ImmutableTypeMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;

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
    return gson.toJson(new EntityMetadata(result));
  }

  @Override
  public MetadataResult<TypeMetadataDescriptor> deserialize(String result) {
    EntityMetadata metadata = gson.fromJson(result, new TypeToken<EntityMetadata>() {}.getType());
    return metadata.toEntityMetadataResult();
  }

  /**
   * DTO that represents a {@link MetadataResult} of {@link TypeMetadataDescriptor} for serializing the metadata result of an
   * entity provided by DSQL.
   *
   * @since 1.0
   */
  private class EntityMetadata {

    private final static String ENTITIES = "ENTITIES";

    private final Failure failure;
    private final TypeMetadata entity;

    public EntityMetadata(MetadataResult<TypeMetadataDescriptor> result) {
      entity = result.get() != null ? new TypeMetadata(result.get().getType(), false) : null;
      failure = result.getFailure().isPresent() ? new Failure(result.getFailure().get(), ENTITIES) : null;
    }

    TypeMetadata getEntity() {
      return entity;
    }

    public MetadataResult<TypeMetadataDescriptor> toEntityMetadataResult() {

      return failure != null
          ? failure(new ImmutableTypeMetadataDescriptor(entity != null ? entity.getType() : null), failure.getMessage(),
                    failure.getFailureCode(),
                    failure.getReason())
          : success(new ImmutableTypeMetadataDescriptor(entity.getType()));
    }
  }

}
