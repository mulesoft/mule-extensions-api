/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
