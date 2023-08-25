/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.sdk.api.annotation.source.SourceClusterSupport;

/**
 * Private {@link ModelProperty} which describes the cluster support of a given {@link SourceModel}
 *
 * @since 1.4
 */
public class SourceClusterSupportModelProperty implements ModelProperty {

  private static final String NAME = "clusterSupport";

  private SourceClusterSupport sourceClusterSupport;

  public SourceClusterSupportModelProperty(SourceClusterSupport clusterSupport) {
    this.sourceClusterSupport = clusterSupport;
  }

  public SourceClusterSupport getSourceClusterSupport() {
    return sourceClusterSupport;
  }

  @Override
  public String getName() {
    return "clusterSupport";
  }

  @Override
  public boolean isPublic() {
    return false;
  }
}
