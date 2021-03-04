/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.stereotype;

import static java.util.Optional.empty;

import java.util.Optional;

/**
 * A widely held but fixed and oversimplified image or idea of the owning model. Examples would be {@code validator},
 * {@code outbound}, etc.
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.stereotype.StereotypeDefinition} instead.
 */
@Deprecated
public interface StereotypeDefinition {

  /**
   * @return The type of the current Stereotype definition
   */
  String getName();

  /**
   * The stereotype's namespace. This method is optional and the Runtime will automatically default to the extension's namespace.
   * Keep in mind that no extension is allowed to declare a stereotype which namespace doesn't match its own. However, the
   * extension stereotypes CAN have a parent which belongs to a different namespace.
   *
   * @return The stereotype's namespace.
   */
  default String getNamespace() {
    return "";
  }

  /**
   * @return The {@link Optional} parent of the current Stereotype definition
   */
  default Optional<StereotypeDefinition> getParent() {
    return empty();
  }

}
