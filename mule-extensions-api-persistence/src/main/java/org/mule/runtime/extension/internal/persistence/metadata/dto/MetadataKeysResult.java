/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence.metadata.dto;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import org.mule.runtime.api.metadata.DefaultMetadataKey;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.List;
import java.util.Set;

/**
 * Data transfer object that carries data that represents a {@link MetadataResult} of a {@link DefaultMetadataKey}
 * and enables the ease of serialization an deserialization.
 *
 * @since 1.0
 */
public class MetadataKeysResult {

  private final static String KEYS = "KEYS";

  private final List<Failure> failures;
  private final Set<DefaultMetadataKey> keys;

  public MetadataKeysResult(MetadataResult<Set<DefaultMetadataKey>> result) {
    this.failures = result.isSuccess() ? emptyList()
        : singletonList(new Failure(result.getFailure().get(), KEYS));
    this.keys = result.get();
  }

  public MetadataResult<Set<DefaultMetadataKey>> toKeysMetadataResult() {
    if (!failures.isEmpty()) {
      Failure metadataFailure = failures.get(0);
      return failure(keys, metadataFailure.getMessage(), metadataFailure.getFailureCode(), metadataFailure.getReason());
    }

    return success(keys);
  }
}
