/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.stereotype;

/**
 * {@link StereotypeDefinition} for any generic config global element.
 *
 * @since 4.5
 */
public final class ExtraAppConfigStereotype extends MuleStereotypeDefinition {

  ExtraAppConfigStereotype() {}

  @Override
  public String getName() {
    return "EXTRA_APP_CONFIG";
  }

}
