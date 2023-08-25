/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.property;

import java.util.Objects;

/**
 * Class which describes an Extension Metadata Resolver.
 * <p/>
 * Gives information about the name of the resolver, as also if requires a connector or configuration to work.
 *
 * @since 1.2.0
 */
public class ResolverInformation {

  private String resolverName;
  private boolean requiresConnection;
  private boolean requiresConfiguration;

  ResolverInformation(String resolverName, boolean requiresConnection, boolean requiresConfiguration) {
    this.resolverName = resolverName;
    this.requiresConnection = requiresConnection;
    this.requiresConfiguration = requiresConfiguration;
  }

  /**
   * @return The name of the resolver
   */
  public String getResolverName() {
    return resolverName;
  }

  /**
   * @return boolean indicating if requires or not a connection to work
   */
  public boolean isRequiresConnection() {
    return requiresConnection;
  }

  /**
   * @return boolean indicating if requires or not a configuration to work
   */
  public boolean isRequiresConfiguration() {
    return requiresConfiguration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ResolverInformation that = (ResolverInformation) o;
    return requiresConnection == that.requiresConnection &&
        requiresConfiguration == that.requiresConfiguration &&
        Objects.equals(resolverName, that.resolverName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resolverName, requiresConnection, requiresConfiguration);
  }
}
