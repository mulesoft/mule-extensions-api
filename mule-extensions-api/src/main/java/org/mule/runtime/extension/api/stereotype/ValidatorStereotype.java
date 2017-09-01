/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.stereotype;

import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.PROCESSOR_DEFINITION;

import java.util.Optional;

/**
 * {@link StereotypeDefinition} for any {@code validator} component. 
 * A validator is also a {@link ProcessorStereotype processor}
 *
 * @since 1.0
 */
public class ValidatorStereotype implements MuleStereotypeDefinition {

  @Override
  public String getName() {
    return "VALIDATOR";
  }

  @Override
  public Optional<StereotypeDefinition> getParent() {
    return Optional.of(PROCESSOR_DEFINITION);
  }

}
