/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.descriptor.ImmutableTypeMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.Optional;

import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;

/**
 * DTO that represents a {@link TypeMetadataDescriptor} into a serializable format.
 *
 * @since 1.0
 */
class TypeMetadata {

  protected final MetadataType type;
  protected final boolean isDynamic;

  TypeMetadata(MetadataType type, boolean isDynamic) {
    this.isDynamic = isDynamic;
    this.type = type;
  }

  public boolean isDynamic() {
    return isDynamic;
  }

  public MetadataType getType() {
    return type;
  }

  MetadataResult<TypeMetadataDescriptor> toDescriptorResult(Optional<Failure> metadataFailure) {
    ImmutableTypeMetadataDescriptor descriptor = new ImmutableTypeMetadataDescriptor(type);
    if (metadataFailure.isPresent()) {
      return failure(descriptor,
                     metadataFailure.get().getMessage(),
                     metadataFailure.get().getFailureCode(),
                     metadataFailure.get().getReason());
    } else {
      return success(descriptor);
    }
  }

}
