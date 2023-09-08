/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
