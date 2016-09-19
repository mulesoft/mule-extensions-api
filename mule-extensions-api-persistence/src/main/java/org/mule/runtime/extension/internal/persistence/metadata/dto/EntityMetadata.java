/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence.metadata.dto;

import org.mule.runtime.api.metadata.descriptor.ImmutableTypeMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;

/**
 * DTO that represents a {@link TypeMetadataDescriptor} into a serializable format. Used for the serialization of the DSQL
 * entities.
 *
 * @since 1.0
 */
public class EntityMetadata {

  private final static String ENTITIES = "ENTITIES";

  private final Failure failure;
  private final TypeMetadata entity;

  public EntityMetadata(MetadataResult<ImmutableTypeMetadataDescriptor> result) {
    entity = result.get() != null ? new TypeMetadata(result.get().getType(), false) : null;
    failure = result.getFailure().isPresent() ? new Failure(result.getFailure().get(), ENTITIES) : null;
  }

  TypeMetadata getEntity() {
    return entity;
  }

  public MetadataResult<ImmutableTypeMetadataDescriptor> toEntityMetadataResult() {

    return failure != null
        ? failure(new ImmutableTypeMetadataDescriptor(entity != null ? entity.getType() : null), failure.getMessage(),
                  failure.getFailureCode(),
                  failure.getReason())
        : success(new ImmutableTypeMetadataDescriptor(entity.getType()));
  }
}
