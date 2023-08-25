/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
