/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata;

import org.mule.runtime.api.metadata.descriptor.ImmutableTypeMetadataDescriptor;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.ImmutableMetadataResult;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.runtime.extension.internal.persistence.metadata.dto.EntityMetadata;

import com.google.gson.reflect.TypeToken;

/**
 * Serializer that can convert a {@link MetadataResult} of {@link TypeMetadataDescriptor} type into a readable and processable
 * JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
 *
 * @since 1.0
 */
public class EntityMetadataResultJsonSerializer extends AbstractMetadataResultJsonSerializer {

  // TODO: MULE-10583 Review MetadataService serialization API
  public EntityMetadataResultJsonSerializer(boolean prettyPrint) {
    super(prettyPrint);
  }

  @Override
  public String serialize(MetadataResult result) {
    return gson.toJson(new EntityMetadata(result));
  }

  @Override
  public ImmutableMetadataResult<ImmutableTypeMetadataDescriptor> deserialize(String result) {
    EntityMetadata metadata = gson.fromJson(result, new TypeToken<EntityMetadata>() {}.getType());
    return (ImmutableMetadataResult<ImmutableTypeMetadataDescriptor>) metadata.toEntityMetadataResult();
  }
}
