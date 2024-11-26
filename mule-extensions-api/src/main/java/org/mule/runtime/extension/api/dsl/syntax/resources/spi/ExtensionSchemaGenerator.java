/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.syntax.resources.spi;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DslSyntaxResolver;

/**
 * Enables the generation of XML Schema Definitions for extensions.
 *
 * @since 1.0
 */
@NoImplement
public interface ExtensionSchemaGenerator {

  /**
   * Creates the XSD for an extension based on it's {@code extensionModel}.
   *
   * @param extensionModel the {@link ExtensionModel} used to generate the schema.
   * @param context        dsl context for all extensions of the deployment
   * @return an String containing the XML Schema Definition for the given extension.
   * @deprecated Since 1.9 use {@link #generate(ExtensionModel, DslResolvingContext, DslSyntaxResolver)} instead.
   */
  @Deprecated
  String generate(ExtensionModel extensionModel, DslResolvingContext context);

  /**
   * Creates the XSD for an extension based on it's {@code extensionModel}.
   *
   * @param extensionModel the {@link ExtensionModel} used to generate the schema.
   * @param context        dsl context for all extensions of the deployment
   * @param dsl            the dsl syntax for the {@code extensionModel}
   * @return an String containing the XML Schema Definition for the given extension.
   * @since 1.9
   */
  String generate(ExtensionModel extensionModel, DslResolvingContext context, DslSyntaxResolver dsl);
}
