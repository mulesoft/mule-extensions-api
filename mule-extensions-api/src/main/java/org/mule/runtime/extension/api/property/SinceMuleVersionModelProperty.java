/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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

  public static final String NAME = "sinceMuleVersion";

  private final MuleVersion version;

  public SinceMuleVersionModelProperty(String version) {
    this.version = new MuleVersion(version);
  }

  /**
   *
   * @return a {@link MuleVersion} from which the enriched model should appear in the
   *         {@link org.mule.runtime.api.meta.model.ExtensionModel}
   */
  public MuleVersion getVersion() {
    return version;
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


