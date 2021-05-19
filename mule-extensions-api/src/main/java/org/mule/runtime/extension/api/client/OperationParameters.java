/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client;

import org.mule.api.annotation.NoImplement;

/**
 * A simple interface for parameters that aims to be used to execute an extension operation using the {@link ExtensionsClient}.
 *
 * @since 1.0
 */
@NoImplement
public interface OperationParameters extends org.mule.sdk.api.client.OperationParameters {

}
