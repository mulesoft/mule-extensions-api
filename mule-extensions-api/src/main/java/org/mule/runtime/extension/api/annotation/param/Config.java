/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.runtime.source.Source;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated member should be injected with the currently active configuration instance.
 * <p>
 * This annotation can either be applied to an argument of an operation method {@link Operations} or to a field of a class which
 * extends the {@link Source} class. It is not to be used on configurations
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.annotation.param.Config} instead.
 */
@Target(value = {PARAMETER, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface Config {

}
