/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ExternalDependencyModel;

/**
 * Utility operations to handle {@link ExternalDependencyModel} checks
 *
 * @since 1.0
 */
public final class ExtensionDependencyUtils {

  private ExtensionDependencyUtils() {}

  /**
   * Validates that a given {@link ExternalDependencyModel} matches the given {@code groupId}, {@code artifactId} and
   * {@code version}. This means that the {@link ExternalDependencyModel} has the same {@code groupId}, {@code artifactId} and the
   * {@code version} is in the range determined by {@link ExternalDependencyModel#getMinVersion()} and
   * {@link ExternalDependencyModel#getMaxVersion()} ()}
   */
  public static boolean validate(ExternalDependencyModel dependencyModel, String groupId, String artifactId, String version) {

    if (!dependencyModel.getGroupId().equals(groupId)) {
      return false;
    } else if (!dependencyModel.getArtifactId().equals(artifactId)) {
      return false;
    }

    boolean result = true;
    MuleVersion dependencyVersion = new MuleVersion(version);

    if (dependencyModel.getMinVersion().isPresent()) {
      result &= dependencyVersion.atLeast(dependencyModel.getMinVersion().get());
    }

    if (dependencyModel.getMaxVersion().isPresent()) {
      MuleVersion maxVersion = dependencyModel.getMaxVersion().get();
      result &= maxVersion.newerThan(dependencyVersion) || maxVersion.equals(dependencyVersion);
    }

    return result;
  }
}
