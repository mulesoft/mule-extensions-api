/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.property;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;

/**
 * A {@link ModelProperty} meant to be applied on {@link ParameterModel} instances which represent an infrastructure parameter
 *
 * @since 1.0
 */
public class InfrastructureParameterModelProperty implements ModelProperty {

  public static final String NAME = "infrastructureParameter";

  public InfrastructureParameterModelProperty(int sequence) {
    this.sequence = sequence;
  }

  private final int sequence;

  /**
   * @return {@code infrastructureParameter}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * @return {@code true}
   */
  @Override
  public boolean isPublic() {
    return true;
  }

  /**
   * @return a sequence indicating the parameters order
   */
  public int getSequence() {
    return sequence;
  }
}
