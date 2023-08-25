/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.loader;

import org.mule.runtime.api.meta.NamedObject;

/**
 * A problem found while validating a model or a component of it
 *
 * @since 1.0
 */
public final class Problem {

  private final NamedObject component;
  private final String message;

  /**
   * Creates a new instance
   *
   * @param component the model in which the problem was found
   * @param message   Problem's description
   */
  public Problem(NamedObject component, String message) {
    this.component = component;
    this.message = message;
  }

  /**
   * @return The model in which the problem was found
   */
  public NamedObject getComponent() {
    return component;
  }

  /**
   * @return The problem's description
   */
  public String getMessage() {
    return message;
  }
}
