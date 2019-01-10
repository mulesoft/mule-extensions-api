/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ModelProperty;

/**
 * {@link ModelProperty} to be used on any {@link org.mule.runtime.api.meta.model.EnrichableModel} that has been added to an
 * {@link org.mule.runtime.api.meta.model.ExtensionModel} in a certain {@link org.mule.runtime.api.meta.MuleVersion} and it is not
 * desired to add it to versions of the extension model generated for older runtimes. If this {@link ModelProperty} is not
 * present, the model must not be filtered for any runtime version.
 *
 * @since 1.2.0
 */
public class SinceMuleVersionModelProperty implements ModelProperty {

  public static final String NAME = "Since MuleVersion";

  private final MuleVersion sinceMuleVersion;

  public SinceMuleVersionModelProperty(String sinceMuleVersion) {
    this.sinceMuleVersion = new MuleVersion(sinceMuleVersion);
  }

  /**
   *
   * @return a {@link MuleVersion} from which the enriched model should appear in the
   *         {@link org.mule.runtime.api.meta.model.ExtensionModel}
   */
  public MuleVersion getSinceMuleVersion() {
    return sinceMuleVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPublic() {
    return true;
  }
}


