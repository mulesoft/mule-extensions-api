/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.loader;

import org.mule.api.annotation.NoImplement;

import java.util.Set;

/**
 * Contract intended for the discovery of the available {@link ExtensionModelLoader}.
 * <p>
 * This contract was originally design with SPI discovery in mind, but could also be used in different contexts.
 *
 * @since 1.5.0
 */
@NoImplement
public interface ExtensionModelLoaderProvider {

  /**
   * @return a {@link Set} of available {@link ExtensionModelLoader} instances.
   */
  Set<ExtensionModelLoader> getExtensionModelLoaders();
}
