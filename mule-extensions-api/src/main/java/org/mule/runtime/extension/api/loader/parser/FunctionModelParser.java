/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader.parser;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.function.FunctionModel;
import org.mule.runtime.extension.api.runtime.function.FunctionExecutor;
import org.mule.runtime.extension.api.runtime.function.FunctionExecutorFactory;

import java.util.List;
import java.util.Optional;

/**
 * Parses the syntactic definition of a {@link FunctionModel} so that the semantics reflected in it can be extracted in a uniform
 * way, regardless of the actual syntax used by the extension developer.
 *
 * @see ExtensionModelParser
 * @since 1.10.0
 */
@NoImplement
public interface FunctionModelParser extends SemanticTermsParser, AdditionalPropertiesModelParser {

  /**
   * @return the function's name
   */
  String getName();

  /**
   * @return the function's description
   */
  String getDescription();

  /**
   * @return an {@link OutputModelParser} describing the function's output value
   */
  OutputModelParser getOutputType();

  /**
   * Returns a list with a {@link ParameterGroupModelParser} per each parameter group defined in the function. Each group is
   * listed in the same order as defined in the syntax.
   *
   * @return a list with the config's {@link ParameterGroupModelParser}
   */
  List<ParameterGroupModelParser> getParameterGroupModelParsers();

  /**
   * @return the {@link FunctionExecutorFactory} used to create the {@link FunctionExecutor} which brings the function to life
   */
  Optional<FunctionExecutorFactory> getFunctionExecutorFactory();

  /**
   * @return whether this function should be ignored and excluded from the resulting {@link ExtensionModel}. If the function is
   *         ignored there is no guarantee for it to be valid, no other parser method should be called if that is the case.
   */
  boolean isIgnored();

  /**
   * @return the function's {@link DeprecationModel} if one was defined
   */
  Optional<DeprecationModel> getDeprecationModel();

  /**
   * @return a {@link MinMuleVersionParser} that contains the minimum mule version this component can run on and the reason why
   *         that version was assigned.
   */
  Optional<MinMuleVersionParser> getResolvedMinMuleVersion();

}
