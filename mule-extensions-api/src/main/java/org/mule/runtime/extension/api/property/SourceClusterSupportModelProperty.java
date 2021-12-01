/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
