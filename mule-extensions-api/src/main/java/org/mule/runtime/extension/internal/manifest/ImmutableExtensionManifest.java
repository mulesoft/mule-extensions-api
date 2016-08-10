/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.manifest;

import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang3.StringUtils.isBlank;

import org.mule.runtime.api.MuleVersion;
import org.mule.runtime.extension.api.manifest.DescriberManifest;
import org.mule.runtime.extension.api.manifest.ExtensionManifest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Immutable implementation of {@link ExtensionManifest}.
 * <p>
 * This class is for internal use only. Users should not reference it.
 *
 * @since 1.0
 */
public final class ImmutableExtensionManifest implements ExtensionManifest {

  private final String name;
  private final String description;
  private final String version;
  private final List<String> exportedPackages;
  private final List<String> exportedResources;
  private final DescriberManifest describerManifest;
  private final MuleVersion minMuleVersion;

  /**
   * Creates a new instance
   *
   * @param name              the extension's name
   * @param description       the extension's description
   * @param version           the extension's version
   * @param minMuleVersion    the extension's min Mule version
   * @param exportedPackages  the extension's exported package names
   * @param exportedResources the extension's exported resource paths
   * @param describerManifest the extension's {@link DescriberManifest}
   */
  public ImmutableExtensionManifest(String name, String description, String version, MuleVersion minMuleVersion,
                                    List<String> exportedPackages, List<String> exportedResources,
                                    DescriberManifest describerManifest) {
    checkNotBlank(name, "name");
    checkNotBlank(version, "version");

    this.name = name;
    this.description = description;
    this.version = version;
    this.minMuleVersion = minMuleVersion;
    this.describerManifest = describerManifest;
    this.exportedPackages = immutable(exportedPackages);
    this.exportedResources = immutable(exportedResources);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getVersion() {
    return version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MuleVersion getMinMuleVersion() {
    return minMuleVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DescriberManifest getDescriberManifest() {
    return describerManifest;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getExportedPackages() {
    return exportedPackages;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getExportedResources() {
    return exportedResources;
  }

  private void checkNotBlank(String value, String attributeName) {
    if (isBlank(value)) {
      throw new IllegalStateException("Manifest cannot have a blank " + attributeName);
    }
  }

  private <T> List<T> immutable(Collection<T> collection) {
    return unmodifiableList(new ArrayList<>(collection));
  }
}
