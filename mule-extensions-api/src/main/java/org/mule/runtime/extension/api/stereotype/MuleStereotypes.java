/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.stereotype;

import static java.util.Optional.ofNullable;

import java.util.Optional;


/**
 * {@link StereotypeDefinition} {@link Enum} which publish the available stereotypes that the Mule Runtime provides
 * to be extended from an extension.
 *
 * @since 1.0
 */
public enum MuleStereotypes implements StereotypeDefinition<MuleStereotypes> {

  ANY,

  SOURCE(ANY),

  PROCESSOR(ANY),

  ERROR_HANDLER,

  FLOW;

  private StereotypeDefinition<MuleStereotypes> parent;

  MuleStereotypes(StereotypeDefinition<MuleStereotypes> parent) {
    this.parent = parent;
  }

  MuleStereotypes() {}

  @Override
  public Optional<StereotypeDefinition<?>> getParent() {
    return ofNullable(parent);
  }
}
