/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.loader;

import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

/**
 * Handler for the {@link Problem problems} generated when loading an extension.
 *
 * @since 1.1
 */
public interface ProblemsHandler {

  /**
   * Receives a {@link ProblemsReporter} with the current status of errors and warnings and executes to be able to execute the
   * correspondent handling.
   *
   * @param problemsReporter reporter containing the extension loading errors and warnings.
   */
  void handleProblems(ProblemsReporter problemsReporter);

}
