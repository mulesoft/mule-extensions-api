/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.property;

import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * {@link ModelProperty} to indicate the SDK used for developing the associated extension.
 *
 * Note that some extensions may not have this property, meaning they were not developed by any of the main SDK flavors. An
 * example of this could be extensions programmatically declared.
 *
 * @since 1.6
 */
public class SdkFlavorModelProperty implements ModelProperty {

  /** The main SDK flavors. */
  public enum SdkFlavor {
    SDK_FLAVOR_JAVA, SDK_FLAVOR_XML, SDK_FLAVOR_MULE, SDK_FLAVOR_MULE_IN_APP
  }

  private final SdkFlavor sdkFlavor;

  public SdkFlavorModelProperty(SdkFlavor sdkFlavor) {
    this.sdkFlavor = sdkFlavor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "sdkFlavor";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPublic() {
    return false;
  }

  /**
   * @return The {@link SdkFlavor} used for developing the associated extension.
   */
  public SdkFlavor getFlavor() {
    return sdkFlavor;
  }
}
