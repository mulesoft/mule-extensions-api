/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence.metadata.dto;

import org.mule.runtime.api.metadata.descriptor.ImmutableTypeMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.OutputMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;

/**
 * DTO that represents a {@link OutputMetadataDescriptor} into a serializable format.
 *
 * @since 1.0
 */
public class EntityMetadata {

  private final static String ENTITIES = "ENTITIES";

  private final List<Failure> failures;
  private final TypeMetadata entity;

  public EntityMetadata(MetadataResult<ImmutableTypeMetadataDescriptor> result) {
    entity = result.get() != null ? new TypeMetadata(result.get().getType(), false) : null;
    failures = result.isSuccess() ? emptyList()
        : singletonList(new Failure(result.getFailure().get(), ENTITIES));
  }

  TypeMetadata getEntity() {
    return entity;
  }

  public MetadataResult<ImmutableTypeMetadataDescriptor> toEntityMetadataResult() {

    if (!failures.isEmpty()) {
      Failure metadataFailure = failures.get(0);
      return failure(new ImmutableTypeMetadataDescriptor(entity != null ? entity.getType() : null), metadataFailure.getMessage(),
                     metadataFailure.getFailureCode(),
                     metadataFailure.getReason());
    }

    return success(new ImmutableTypeMetadataDescriptor(entity.getType()));
  }
}
