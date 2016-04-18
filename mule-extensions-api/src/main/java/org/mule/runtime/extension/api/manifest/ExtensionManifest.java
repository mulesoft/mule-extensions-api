/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.manifest;

import org.mule.runtime.extension.api.introspection.Described;
import org.mule.runtime.extension.api.introspection.ExtensionModel;

/**
 * A manifest which enunciates the main properties of a {@link ExtensionModel}
 * which is yet to be instantiated.
 *
 * @see ExtensionManifestBuilder
 * @since 1.0
 */
public interface ExtensionManifest extends Described
{

    /**
     * @return the extension's version
     */
    String getVersion();

    /**
     * @return a {@link DescriberManifest}
     */
    DescriberManifest getDescriberManifest();
}
