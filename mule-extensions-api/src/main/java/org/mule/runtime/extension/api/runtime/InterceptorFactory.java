/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime;

import org.mule.runtime.extension.api.runtime.operation.Interceptor;

/**
 * Creates new instances of {@link Interceptor}.
 * <p/>
 * Implementations are to be reusable and thread-safe
 *
 * @since 1.0
 * @deprecated since 1.3.0. Do not use.
 */
@FunctionalInterface
@Deprecated
public interface InterceptorFactory {

  /**
   * Creates a new {@link Interceptor}
   *
   * @return a new {@link Interceptor}
   */
  Interceptor createInterceptor();
}
