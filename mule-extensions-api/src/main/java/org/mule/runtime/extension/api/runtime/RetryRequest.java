/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime;

/**
 * Requests the runtime to retry the execution of a failing operation.
 * <p/>
 * The runtime does not guarantee that the petition will be granted nor provides
 * any feedback on the decision taken. There's also no guarantee on whether the request
 * is evaluated right away or if it's deferred to a later moment.
 *
 * @since 1.0
 */
public interface RetryRequest
{

    /**
     * Request the retry of a failing operation
     */
    void request();

}
