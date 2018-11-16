/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import static java.util.Collections.unmodifiableList;

import org.mule.runtime.api.meta.model.ModelProperty;

import java.util.List;

/**
 * {@link ModelProperty} to be used on Configurations or Connection Providers which indicates which parameters of these
 * impact on the metadata resolution.
 *
 * @since 1.2.0
 */
public class MetadataImpactModelProperty implements ModelProperty {

  public static final String NAME = "metadataImpact";

  private final List<String> requiredParameters;

  public MetadataImpactModelProperty(List<String> requiredParameters) {
    this.requiredParameters = requiredParameters;
  }

  /**
   * @return The list of required parameters for metadata resolution.
   */
  public List<String> getRequiredParameters() {
    return unmodifiableList(requiredParameters);
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
