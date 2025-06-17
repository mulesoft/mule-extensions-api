/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader.parser.metadata;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;
import org.mule.runtime.extension.api.loader.parser.ExtensionModelParser;

/**
 * Parses the syntactic definition of input resolvers so that the semantics reflected in it can be extracted in a uniform way,
 * regardless of the actual syntax used by the extension developer.
 *
 * @see ExtensionModelParser
 * @since 1.10.0
 */
@NoImplement
public interface InputResolverModelParser {

  /**
   * @return the name of the parameter associated to the resolver parser by this parser
   */
  String getParameterName();

  /**
   * @return the instance of {@link InputTypeResolver}
   */
  InputTypeResolver<?> getInputResolver();

}
