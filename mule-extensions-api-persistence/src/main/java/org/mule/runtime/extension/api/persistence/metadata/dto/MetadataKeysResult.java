/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata.dto;

import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import org.mule.runtime.api.metadata.DefaultMetadataKey;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.Set;

/**
 * Data transfer object that carries data that represents a {@link MetadataResult} of a {@link DefaultMetadataKey}
 * and enables the ease of serialization an deserialization.
 *
 * @since 1.0
 */
public class MetadataKeysResult
{

    private final KeysResult result;
    private final Set<DefaultMetadataKey> keys;

    public MetadataKeysResult(MetadataResult<Set<DefaultMetadataKey>> metadataKeysResult)
    {
        this.result = new KeysResult(metadataKeysResult);
        this.keys = metadataKeysResult.get();
    }

    public MetadataResult<Set<DefaultMetadataKey>> toKeysMetadataResult()
    {
        if (!result.isSuccess())
        {
            Failure metadataFailure = result.getFailures().get(0);
            return failure(keys, metadataFailure.getMessage(), metadataFailure.getFailureCode(), metadataFailure.getReason());
        }

        return success(keys);
    }
}
