/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.extension.api.soap.SoapTransportProvider;
import org.mule.runtime.extension.api.soap.message.MessageDispatcher;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks an Extension so a {@link SoapTransportProvider} that creates a {@link MessageDispatcher} that dispatches the soap
 * messages using a provided HTTP Extension Configuration is added to the transports that the extension can use.
 * <p>
 * The {@link SoapTransportProvider} that this annotation adds to the extension will coexist with the ones defined in the
 * {@link SoapTransportProviders} annotation.
 *
 * @since 1.0
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface HttpConfigTransportProvider {
}
