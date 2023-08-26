/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.stereotype;

/**
 * {@link StereotypeDefinition} for any generic config global element.
 *
 * @since 1.0
 */
public final class AppConfigStereotype extends MuleStereotypeDefinition {

  AppConfigStereotype() {}

  @Override
  public String getName() {
    return "APP_CONFIG";
  }

}
