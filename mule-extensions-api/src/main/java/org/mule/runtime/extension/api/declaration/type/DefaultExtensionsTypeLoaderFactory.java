/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static java.lang.Thread.currentThread;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.java.api.JavaTypeLoader;

/**
 * Default implementation of {@link ExtensionsTypeLoaderFactory}
 *
 * @since 1.0
 */
public class DefaultExtensionsTypeLoaderFactory implements ExtensionsTypeLoaderFactory {

  /**
   * Delegates into {@link #createTypeLoader(ClassLoader)} using the current context {@link ClassLoader}
   *
   * @return a new {@link JavaTypeLoader}
   */
  @Override
  public ClassTypeLoader createTypeLoader() {
    return createTypeLoader(currentThread().getContextClassLoader());
  }

  /**
   * Creates a new {@link JavaTypeLoader} using the given {@code classLoader}
   *
   * @param classLoader the {@link ClassLoader} that the created loader should use to access java typesl
   * @return a new {@link JavaTypeLoader}
   */
  @Override
  public ClassTypeLoader createTypeLoader(ClassLoader classLoader) {
    return new JavaTypeLoader(classLoader, new ExtensionsTypeHandlerManagerFactory());
  }
}
