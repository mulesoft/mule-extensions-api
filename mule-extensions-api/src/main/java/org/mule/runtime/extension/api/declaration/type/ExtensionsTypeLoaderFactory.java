/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.declaration.type;

import org.mule.api.annotation.NoImplement;
import org.mule.metadata.api.ClassTypeLoader;

/**
 * Factory class for instances of {@link ClassTypeLoader} properly setup to introspect types following the rules of the Extensions
 * API.
 * <p>
 * The extensions runtime should not use instances of {@link ClassTypeLoader} which were not created through implementations of
 * this interface.
 *
 * @since 1.0
 */
@NoImplement
public interface ExtensionsTypeLoaderFactory {

  /**
   * @return the system default implementation of this class
   */
  static ExtensionsTypeLoaderFactory getDefault() {
    return new DefaultExtensionsTypeLoaderFactory();
  }

  /**
   * @return a new instance of {@link ClassTypeLoader}
   */
  ClassTypeLoader createTypeLoader();

  /**
   * @param classLoader the {@link ClassLoader} that the created loader should use to access java typesl
   * @return a new instance of {@link ClassTypeLoader}
   */
  ClassTypeLoader createTypeLoader(ClassLoader classLoader);
}
