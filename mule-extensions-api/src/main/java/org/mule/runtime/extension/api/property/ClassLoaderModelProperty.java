/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.EnrichableModel;
import org.mule.runtime.api.meta.model.ModelProperty;

import java.lang.ref.WeakReference;

/**
 * Links an {@link EnrichableModel} with a {@link ClassLoader}.
 *
 * @since 1.0
 */
public class ClassLoaderModelProperty implements ModelProperty {

  private final WeakReference<ClassLoader> classLoader;

  /**
   * Creates a new instance
   *
   * @param classLoader the {@link ClassLoader} that {@code this} instance references
   */
  public ClassLoaderModelProperty(ClassLoader classLoader) {
    this.classLoader = new WeakReference<>(classLoader);
  }

  /**
   * @return {@code classLoader}
   */
  @Override
  public String getName() {
    return "classLoader";
  }

  /**
   * @return {@code false}
   */
  @Override
  public boolean isPublic() {
    return false;
  }

  /**
   * @return The referenced {@link ClassLoader}
   */
  public ClassLoader getClassLoader() {
    return classLoader.get();
  }
}
