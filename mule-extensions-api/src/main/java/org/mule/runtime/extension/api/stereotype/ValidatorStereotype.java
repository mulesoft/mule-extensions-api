/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.stereotype;

import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.PROCESSOR_DEFINITION;

import java.util.Optional;

/**
 * {@link StereotypeDefinition} for any {@code validator} component. A validator is also a {@link ProcessorStereotype processor}
 *
 * @since 1.0
 */
public final class ValidatorStereotype extends MuleStereotypeDefinition {

  @Override
  public String getName() {
    return "VALIDATOR";
  }

  @Override
  public Optional<StereotypeDefinition> getParent() {
    return Optional.of(PROCESSOR_DEFINITION);
  }

}
