/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.property;

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
