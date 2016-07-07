/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata.dto;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import org.mule.runtime.api.metadata.DefaultMetadataKey;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.List;
import java.util.Set;

/**
 * DTO that represents a {@link MetadataResult} into a serializable format.
 *
 * @since 1.0
 */
class KeysResult
{

    private final List<Failure> failures;
    private final boolean isSuccess;

    KeysResult(MetadataResult<Set<DefaultMetadataKey>> result)
    {
        this.isSuccess = result.isSuccess();
        this.failures = result.isSuccess() ? emptyList() : singletonList(new Failure(result.getFailure().get(), "KEYS"));
    }

    boolean isSuccess()
    {
        return isSuccess;
    }

    List<Failure> getFailures()
    {
        return failures;
    }

}
