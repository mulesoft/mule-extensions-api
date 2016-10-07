/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration;

import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;

/**
 * Used for propagating state across all the components
 * that may take part on the describing of an {@link ExtensionModel}
 *
 * @since 1.0
 */
public interface DescribingContext {

  /**
   * The {@link ExtensionDeclarer} in which
   * the extension is being described into
   *
   * @return a non {@code null} {@link ExtensionDeclarer}
   */
  ExtensionDeclarer getExtensionDeclarer();

  /**
   * Adds a custom parameter registered under {@code key}
   *
   * @param key   the key under which the {@code value} is to be registered
   * @param value the custom parameter value
   * @throws IllegalArgumentException if {@code key} or {@code value} are {@code null}
   */
  void addParameter(String key, Object value);

  /**
   * Obtains the custom parameter registered under {@code key}
   * and verifies that (if not null) is of type {@code expectedType}.
   * If the obtained value if not an instance of such type, an
   * {@link IllegalArgumentException} is thrown. If no value is
   * registered under that {@code key}, then it simply returns
   * {@code null}
   *
   * @param key          the key under which the wanted value is registered
   * @param expectedType the type of the expected value
   * @param <T>          generic type of the expected value
   * @return an instance of {@code expectedType} or {@code null}
   */
  <T> T getParameter(String key, Class<T> expectedType);
}
