/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence.metadata.dto;

import org.mule.runtime.api.metadata.DefaultMetadataKey;
import org.mule.runtime.api.metadata.MetadataKeysContainer;
import org.mule.runtime.api.metadata.MetadataKeysContainerBuilder;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyMap;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;

/**
 * Data transfer object that carries data that represents a {@link MetadataResult} of a {@link DefaultMetadataKey} and enables the
 * ease of serialization an deserialization.
 *
 * @since 1.0
 */
public class MetadataKeysResult {

  private final static String KEYS = "KEYS";

  private final Failure failure;
  private final Map<String, Set<DefaultMetadataKey>> keys;

  @SuppressWarnings("unchecked")
  public MetadataKeysResult(MetadataResult<MetadataKeysContainer> result) {
    this.failure = result.getFailure().isPresent() ? new Failure(result.getFailure().get(), KEYS) : null;
    this.keys = result.get() != null ? (Map) result.get().getKeysByCategory() : emptyMap();
  }

  public MetadataResult<MetadataKeysContainer> toKeysMetadataResult() {
    MetadataKeysContainerBuilder builder = MetadataKeysContainerBuilder.getInstance().addAll((Map) keys);

    return failure != null ? failure(builder.build(), failure.getMessage(), failure.getFailureCode(),
                                     failure.getReason())
        : success(builder.build());
  }
}
