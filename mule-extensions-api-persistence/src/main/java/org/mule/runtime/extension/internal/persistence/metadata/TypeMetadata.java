/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence.metadata;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;

/**
 * DTO that represents a {@link TypeMetadataDescriptor} into a serializable format.
 *
 * @since 1.0
 */
public final class TypeMetadata {

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

  TypeMetadataDescriptor toDescriptor() {
    return TypeMetadataDescriptor.builder().withType(type).dynamic(isDynamic).build();
  }

}
