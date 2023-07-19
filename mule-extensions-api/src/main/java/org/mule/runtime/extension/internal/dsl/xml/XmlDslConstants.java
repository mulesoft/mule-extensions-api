/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.dsl.xml;

import static org.mule.runtime.internal.dsl.DslConstants.DEFAULT_NAMESPACE_URI_MASK;

import static java.lang.String.format;

/**
 * Constants related with XML based DSLs.
 *
 * @since 1.5
 */
public final class XmlDslConstants {

  public static final String MODULE_DSL_NAMESPACE_URI = format(DEFAULT_NAMESPACE_URI_MASK, "module");
  public static final String MODULE_DSL_NAMESPACE = "module";
  public static final String MODULE_ROOT_NODE_NAME = "module";

  public static final String MULE_SDK_EXTENSION_DSL_NAMESPACE_URI = format(DEFAULT_NAMESPACE_URI_MASK, "mule-extension");
  public static final String MULE_SDK_EXTENSION_DSL_NAMESPACE = "extension";
  public static final String MULE_SDK_EXTENSION_ROOT_NODE_NAME = "extension";

  private XmlDslConstants() {
    // Private constructor to prevent instantiation.
  }
}
