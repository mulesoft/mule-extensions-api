/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.resources;

import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.api.resources.spi.GeneratedResourceFactory;

import java.util.List;

/**
 * A component capable of dynamically generating resources to support
 * an {@link ExtensionModel}.
 * <p>
 * Although extensions resolve their functionality mainly on runtime,
 * some configuration resources such as XML schemas, service registration files,
 * spring bundles, or whatever resource the runtime requires need to be generated
 * in compile or run time.
 * <p>
 * To determine which resources need to be generated, a standard SPI
 * mechanism will be used to obtain instances of {@link GeneratedResourceFactory}.
 * Those factories will be used to create the resources needed by each extension.
 *
 * @since 1.0
 */
public interface ResourcesGenerator {

  /**
   * Generates resources for the given {@code extension} by propagating
   * the given {@code extensionModel} through all the discovered
   * {@link GeneratedResourceFactory} instances.
   * <p>
   * The generated resources are written to disk and returned for
   * further processing.
   *
   * @param extensionModel a {@link ExtensionModel}
   * @link a {@link List} with the generated resources. Might be empty but will never be {@code null}
   */
  List<GeneratedResource> generateFor(ExtensionModel extensionModel);
}
