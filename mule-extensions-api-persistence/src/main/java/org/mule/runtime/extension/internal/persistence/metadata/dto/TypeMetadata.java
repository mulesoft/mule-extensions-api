/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence.metadata.dto;

import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.descriptor.ImmutableTypeMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.List;

/**
 * DTO that represents a {@link TypeMetadataDescriptor} into a serializable format.
 *
 * @since 1.0
 */
public class TypeMetadata implements Descriptable<TypeMetadataDescriptor> {

  protected final MetadataType type;
  protected final boolean isDynamic;

  public TypeMetadata(MetadataType type, boolean isDynamic) {
    this.isDynamic = isDynamic;
    this.type = type;
  }

  public boolean isDynamic() {
    return isDynamic;
  }

  public MetadataType getType() {
    return type;
  }

  @Override
  public MetadataResult<TypeMetadataDescriptor> toDescriptorResult(List<Failure> metadataFailure) {
    ImmutableTypeMetadataDescriptor descriptor = new ImmutableTypeMetadataDescriptor(type);
    if (metadataFailure.size() == 1) {
      return failure(descriptor,
                     metadataFailure.get(0).getMessage(),
                     metadataFailure.get(0).getFailureCode(),
                     metadataFailure.get(0).getReason());
    } else {
      return success(descriptor);
    }
  }

}
