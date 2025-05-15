/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * Indicates the name of the config to use when the operation/source with this property does not expliciely define the config to
 * use.
 *
 * @since 1.10
 */
public class ImplicitConfigNameModelProperty implements ModelProperty {

  private static final long serialVersionUID = 1L;

  private final String implicitConfigName;

  public ImplicitConfigNameModelProperty(String implicitConfigName) {
    this.implicitConfigName = implicitConfigName;
  }

  @Override
  public String getName() {
    return "ImplicitConfigName";
  }

  @Override
  public boolean isPublic() {
    return false;
  }

  public String getImplicitConfigName() {
    return implicitConfigName;
  }
}
