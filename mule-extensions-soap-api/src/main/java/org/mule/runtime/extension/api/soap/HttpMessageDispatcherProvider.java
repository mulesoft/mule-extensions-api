/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap;

import org.mule.runtime.extension.api.soap.annotation.SoapMessageDispatcherProviders;
import org.mule.runtime.extension.api.soap.message.MessageDispatcher;

/**
 * A message dispatcher that sends messages over HTTP with a default configuration or using an HTTP requester configuration.
 * <p>
 * This interface aims to be added as one of the {@link SoapMessageDispatcherProviders} annotation values to explicitly mark
 * this dispatcher must be added to the extension available message dispatchers.
 * <p>
 * Also, when the extension does not provide any custom dispatchers, this dispatcher is added as the default one.
 *
 * @since 1.0
 */
public interface HttpMessageDispatcherProvider extends MessageDispatcherProvider<MessageDispatcher> {

}
