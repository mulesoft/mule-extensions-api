/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;

import static java.util.Collections.unmodifiableList;
import org.mule.runtime.api.meta.model.ExtensionModel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Collects {@link Problem problems} found while validating an {@link ExtensionModel}.
 *
 * The purpose is to simply notify all the problems found together.
 *
 * @since 1.0
 */
public final class ProblemsReporter {

  private final ExtensionModel extensionModel;
  private final List<Problem> errors = new LinkedList<>();
  private final List<Problem> warnings = new LinkedList<>();

  /**
   * Creates a new instance
   * 
   * @param extensionModel the model which is being validated
   */
  public ProblemsReporter(ExtensionModel extensionModel) {
    this.extensionModel = extensionModel;
  }

  /**
   * Adds an error
   * 
   * @param problem the problem found
   * @return {@code this} reporter
   */
  public ProblemsReporter addError(Problem problem) {
    errors.add(problem);
    return this;
  }

  /***
   * Adds a warning
   * 
   * @param problem the warning found
   * @return {@code this} reporter
   */
  public ProblemsReporter addWarning(Problem problem) {
    warnings.add(problem);
    return this;
  }

  /**
   * @return Whether errors have been reported
   */
  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  /**
   * @return Whether warnings have been reported
   */
  public boolean hasWarnings() {
    return !warnings.isEmpty();
  }

  /**
   * @return The {@link ExtensionModel} that is being/was validated
   */
  public ExtensionModel getExtensionModel() {
    return extensionModel;
  }

  /**
   * @return immutable list of errors found
   */
  public List<Problem> getErrors() {
    return unmodifiableList(errors);
  }

  /**
   * @return immutable list of warnings found
   */
  public List<Problem> getWarnings() {
    return unmodifiableList(warnings);
  }

  /**
   * @return All the found warnings flattened as a String
   */
  public String getWarningsAsString() {
    return appendProblems(new StringBuilder(), warnings).toString();
  }

  /**
   * @return All found problems flattened as a String. Errors first, warnings later
   */
  @Override
  public String toString() {
    StringBuilder message = new StringBuilder();
    append(message, "ERRORS", errors);
    message.append('\n');
    append(message, "WARNINGS", warnings);

    return message.toString();
  }

  private void append(StringBuilder message, String problemType, Collection<Problem> problems) {
    message.append(problemType + ":");
    if (problems.isEmpty()) {
      message.append(" NONE");
    } else {
      message.append("\n");
      appendProblems(message, problems);
    }
  }

  private StringBuilder appendProblems(StringBuilder message, Collection<Problem> problems) {
    problems.forEach(p -> message.append("\t* ").append(p.getMessage()).append('\n'));
    return message;
  }
}
