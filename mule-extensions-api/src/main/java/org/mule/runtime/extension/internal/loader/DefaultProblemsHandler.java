/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

import org.slf4j.Logger;

/**
 * Default {@link ProblemsHandler}.
 *
 * @since 1.1
 */
public final class DefaultProblemsHandler implements ProblemsHandler {

  private ExtensionModel extensionModel;
  private final Logger LOGGER = getLogger(ExtensionModelFactory.class);

  DefaultProblemsHandler(ExtensionModel extensionModel) {
    this.extensionModel = extensionModel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleProblems(ProblemsReporter problemsReporter) {
    if (problemsReporter.hasErrors()) {
      throw new IllegalModelDefinitionException(format("Extension '%s' has definition errors:\n%s", extensionModel.getName(),
                                                       problemsReporter.toString()));
    } else if (problemsReporter.hasWarnings()) {
      if (LOGGER.isWarnEnabled()) {
        LOGGER.warn(format("Extension '%s' has definition warnings:\n%s", extensionModel.getName(),
                           problemsReporter.getWarningsAsString()));
      }
    }
  }
}
