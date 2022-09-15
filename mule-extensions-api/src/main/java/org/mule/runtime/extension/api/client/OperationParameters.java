/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client;

import org.mule.api.annotation.NoImplement;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.function.Consumer;

/**
 * A simple interface for parameters that aims to be used to execute an extension operation using the {@link ExtensionsClient}.
 *
 * @since 1.0
 * @deprecated since 1.5.0. Use {@link ExtensionsClient#executeAsync(String, String, Consumer)} instead
 */
@MinMuleVersion("4.1")
@NoImplement
@Deprecated
public interface OperationParameters extends org.mule.sdk.api.client.OperationParameters {

}
