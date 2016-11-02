/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.manifest;

import org.mule.runtime.extension.api.declaration.spi.Describer;
import org.mule.runtime.extension.api.manifest.ExtensionManifestBuilder.DescriberManifestBuilder;

import java.util.Map;

/**
 * Manifest that enunciates the main properties of a {@link Describer}
 * to be instantiated by the Mule Runtime.
 *
 * @see DescriberManifestBuilder
 * @since 1.0
 */
public interface DescriberManifest {

  /**
   * @return A unique ID which identifies a describer type.
   */
  String getId();

  /**
   * Providers properties which parameterize the {@link Describer}
   *
   * @return a {@link Map} of {@link String strings}. Could be empty but will never be {@code null}
   */
  Map<String, String> getProperties();
}
