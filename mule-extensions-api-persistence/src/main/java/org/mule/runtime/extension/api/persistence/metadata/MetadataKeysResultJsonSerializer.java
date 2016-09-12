/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata;

import org.mule.runtime.api.metadata.DefaultMetadataKey;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.resolving.ImmutableMetadataResult;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.runtime.extension.internal.persistence.metadata.dto.MetadataKeysResult;

import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Serializer that can convert a {@link MetadataResult} of a {@link List} of {@link MetadataKey} type into
 * a readable and processable JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
 *
 * @since 1.0
 */
public class MetadataKeysResultJsonSerializer extends AbstractMetadataResultJsonSerializer {

  public MetadataKeysResultJsonSerializer() {
    super(false);
  }

  public MetadataKeysResultJsonSerializer(boolean prettyPrint) {
    super(prettyPrint);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String serialize(MetadataResult metadataResult) {
    return gson.toJson(new MetadataKeysResult(metadataResult));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ImmutableMetadataResult<Map<String, Set<DefaultMetadataKey>>> deserialize(String metadataResult) {
    MetadataKeysResult result = gson.fromJson(metadataResult, new TypeToken<MetadataKeysResult>() {}.getType());
    return (ImmutableMetadataResult<Map<String, Set<DefaultMetadataKey>>>) result.toKeysMetadataResult();
  }
}
