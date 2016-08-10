/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection;

import org.mule.runtime.extension.api.runtime.operation.Interceptor;
import org.mule.runtime.extension.api.runtime.InterceptorFactory;

import java.util.List;

/**
 * Contract for models capable of providing a {@link List} of {@link InterceptorFactory}
 *
 * @see Interceptable
 * @since 1.0
 */
public interface InterceptableModel {

  /**
   * Returns a {@link List} which items are {@link InterceptorFactory} instances
   * that are to be used to provision the {@link Interceptor interceptors} for the
   * configurations created from this model.
   * <p>
   * The order of the factories in the list will be the same as the order of the resulting
   * {@link Interceptor interceptors}. However, just like it's explained in the
   * {@link Interceptable} interface, the order is not guaranteed to be respected although
   * it should be expressed anyway.
   *
   * @return an immutable {@link List}. Can be empty but must never be {@code null}
   */
  List<InterceptorFactory> getInterceptorFactories();
}
