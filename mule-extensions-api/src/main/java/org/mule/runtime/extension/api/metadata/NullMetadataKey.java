/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.metadata;

import static java.util.Collections.emptySet;

import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataProperty;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.Optional;
import java.util.Set;

/**
 * Null {@link MetadataKey} implementation that represents the absence of a key
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
public final class NullMetadataKey implements MetadataKey {

  public static final String ID = "";

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId() {
    return ID;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDisplayName() {
    return "";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<MetadataKey> getChilds() {
    return emptySet();
  }

  @Override
  public String getPartName() {
    return "";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends MetadataProperty> Optional<T> getMetadataProperty(Class<T> propertyType) {
    return Optional.empty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<MetadataProperty> getProperties() {
    return emptySet();
  }
}
