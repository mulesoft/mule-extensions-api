/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;

import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;

/**
 * Extension point for customizable components which declare an extension.
 *
 * Whenever a client of the Extensions API wants to provide its own logic for declaring an extension, this interface should be use
 * to provide such logic
 *
 * @since 1.0
 */
@FunctionalInterface
public interface ExtensionLoadingDelegate {

  /**
   * Uses the given arguments to declare the extension
   * 
   * @param extensionDeclarer the {@link ExtensionDeclarer} in which the extension is declared
   * @param context           the loading context with all the additional information available
   */
  void accept(ExtensionDeclarer extensionDeclarer, ExtensionLoadingContext context);
}
