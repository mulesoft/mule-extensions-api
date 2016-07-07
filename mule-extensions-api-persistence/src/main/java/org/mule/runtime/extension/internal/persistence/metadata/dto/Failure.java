/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence.metadata.dto;

import org.mule.runtime.api.metadata.resolving.FailureCode;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;

/**
 * DTO that represents a {@link MetadataFailure} into a serializable format.
 *
 * @since 1.0
 */
class Failure
{

    private final String failureCode;
    private final String message;
    private final String reason;
    private final String failureComponent;

    Failure(MetadataFailure failure, String failureComponent)
    {
        this.failureComponent = failureComponent;
        this.reason = failure.getReason();
        this.message = failure.getMessage();
        this.failureCode = failure.getFailureCode().getName();
    }

    FailureCode getFailureCode()
    {
        return new FailureCode(failureCode);
    }

    String getMessage()
    {
        return message;
    }

    String getReason()
    {
        return reason;
    }

    String getFailureComponent()
    {
        return failureComponent;
    }
}
