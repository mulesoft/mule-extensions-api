/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.property;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.extension.internal.ExtensionDevelopmentFramework;

/**
 * {@link ModelProperty} to indicate the {@link ExtensionDevelopmentFramework} used for developing the associated extension.
 * <p>
 * Note that some extensions may not have this property, meaning they were not developed by any of the main development
 * frameworks. An example of this could be extensions programmatically declared.
 *
 * @since 1.5
 */
public class DevelopmentFrameworkModelProperty implements ModelProperty {

  private final ExtensionDevelopmentFramework extensionDevelopmentFramework;

  public DevelopmentFrameworkModelProperty(ExtensionDevelopmentFramework extensionDevelopmentFramework) {
    this.extensionDevelopmentFramework = extensionDevelopmentFramework;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "extensionDevelopmentFramework";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPublic() {
    return false;
  }

  /**
   * @return The {@link ExtensionDevelopmentFramework} used for developing the associated extension.
   */
  public ExtensionDevelopmentFramework getDevelopmentFramework() {
    return extensionDevelopmentFramework;
  }
}
