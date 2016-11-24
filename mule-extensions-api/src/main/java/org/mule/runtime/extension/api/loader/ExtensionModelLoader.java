/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;


import org.mule.runtime.api.deployment.meta.MulePluginLoaderDescriptor;
import org.mule.runtime.api.deployment.meta.MulePluginModel;
import org.mule.runtime.api.meta.model.ExtensionModel;

import java.util.Map;

/**
 * Loader for {@link ExtensionModel} for a Mule plugin artifact from a set of attributes read by the {@link MulePluginModel}.
 * <p/>
 * The parametrization of {@link #loadExtensionModel(ClassLoader, Map)} method should become from the {@link MulePluginLoaderDescriptor#getAttributes()}
 * method, where that descriptor comes from a {@link MulePluginModel#getExtensionModelLoaderDescriptor()}.
 *
 * @since 1.0
 */
public interface ExtensionModelLoader {

  /**
   * @return an identifier to be compared with a {@link MulePluginLoaderDescriptor#getId()). Non null neither blank.
   * <p/>
   * The ID must be unique among all {@link ExtensionModelLoader }.
   */
  String getId();

  /**
   * Creates an {@link ExtensionModel} from the {@code pluginClassLoader} using any attribute it needs from the {@code attributes}
   * parameter.
   * <p/>
   * Bear in mind that, is up to each implementation of this method to validate the {@code attributes} parameter has the needed
   * elements and that each of them is also of the expected Java class. This is by design, as the invokers of this method knows
   * how to parse descriptor files (see {@link MulePluginModel}) for a plugin in a generic way.
   *
   * @param pluginClassLoader context {@link ClassLoader} that holds all the needed classes and resources to properly generate
   *                          an {@link ExtensionModel}.
   * @param attributes a set of attributes to work with in each concrete implementation of {@link org.mule.runtime.api.deployment.loader.ExtensionModelLoader}, which will
   *                   be responsible of extracting the mandatory parameters (while casting, if needed).
   * @return an {@link ExtensionModel} that represents the plugin being described
   * @throws IllegalArgumentException if there are missing entries in {@code attributes} or the type of any of them does not apply
   * to the expected one.
   */
  ExtensionModel loadExtensionModel(ClassLoader pluginClassLoader, Map<String, Object> attributes);
}
