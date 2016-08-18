/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.api.resolver;

import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.xml.dsl.api.DslElementSyntax;

import java.util.Optional;

/**
 * Context used by the {@link DslSyntaxResolver} to discover the
 * {@link ExtensionModel}s required to generate the {@link DslElementSyntax}
 * of a given component.
 *
 * @since 1.0
 */
public interface DslResolvingContext {

  /**
   * Returns an {@link Optional} {@link ExtensionModel} which
   * name equals the provided {@code name}.
   *
   * @param name the name of the extensions you want.
   * @return an {@link Optional}. It will be empty if the {@link ExtensionModel} is not
   * found in the context.
   */
  Optional<ExtensionModel> getExtension(String name);
}
