/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.util;

import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;

/**
 * Utilities for handling a extension's namespace
 * <p>
 * This class <b>IS NOT</b> part of the API. To be used by the Mule Runtime only
 *
 * @since 1.5.0
 */
public class ExtensionNamespaceUtils {

  /**
   * @param extensionModel an {@link ExtensionModel}
   * @return the extension's namespace for a given {@link ExtensionModel}
   */
  public static String getExtensionsNamespace(ExtensionModel extensionModel) {
    return getExtensionsNamespace(extensionModel.getXmlDslModel());
  }

  /**
   * @param extensionDeclaration a {@link ExtensionDeclaration}
   * @return the extension's namespace for a given {@link ExtensionDeclaration}
   */
  public static String getExtensionsNamespace(ExtensionDeclaration extensionDeclaration) {
    return getExtensionsNamespace(extensionDeclaration.getXmlDslModel());
  }

  /**
   * @param dslModel a {@link XmlDslModel}
   * @return the extension's namespace for a given {@link ExtensionDeclaration}
   */
  public static String getExtensionsNamespace(XmlDslModel dslModel) {
    return dslModel.getPrefix().toUpperCase();
  }
}
