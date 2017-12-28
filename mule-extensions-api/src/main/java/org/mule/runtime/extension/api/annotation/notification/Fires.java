/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.notification;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.notification.NotificationActionDefinition;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declares an array of {@link Class classes} of an {@link NotificationActionProvider}, to communicate and declare
 * which {@link NotificationActionDefinition}s a component could fire.
 * <p>
 * This annotation can be applied to an Operation method, an Operation container class, an {@link Extension} class or a source.
 * The annotation value to consider is the one of in the deepest level,
 * eg: If the annotation is used in an operation method and in an extension class, the one to use is the one in the
 * operation.
 *
 * @since 1.1
 * @see NotificationActionProvider
 * @see NotificationActionDefinition
 */
@Target({METHOD, TYPE})
@Retention(RUNTIME)
@Documented
public @interface Fires {

  /**
   * @return the {@link NotificationActionProvider} to use
   */
  Class<? extends NotificationActionProvider>[] value();

}
