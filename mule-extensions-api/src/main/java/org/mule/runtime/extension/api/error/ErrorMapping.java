/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.error;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

/**
 * Determines that an error thrown by an operation should be mapped to another.
 *
 * @since 1.4
 */
public final class ErrorMapping {

  @Parameter
  @Optional(defaultValue = "ANY")
  @Alias("sourceType")
  private final String source;
  @Parameter
  @Alias("targetType")
  private final String target;

  public ErrorMapping(String source, String target) {
    this.source = source;
    this.target = target;
  }

  /**
   * @return the type of the error to be mapped from
   */
  public String getSource() {
    return source;
  }

  /**
   * @return the type of the error to be mapped to
   */
  public String getTarget() {
    return target;
  }

  @Override
  public String toString() {
    return "ErrorMapping: " + source + " -> " + target;
  }
}
