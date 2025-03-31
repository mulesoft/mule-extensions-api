/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.resources;

import org.mule.runtime.api.meta.model.ExtensionModel;

/**
 * A resource that is generated in the context of an {@link ExtensionModel}'s build process
 *
 * @since 1.0
 */
public final class GeneratedResource {

  private final boolean availableInArtifact;
  private final String path;
  private final byte[] content;

  /**
   * Creates a new instance
   *
   * @param path    the resource's path
   * @param content the resource's content
   */
  public GeneratedResource(String path, byte[] content) {
    this(false, path, content);
  }

  /**
   * Creates a new instance
   *
   * @param availableInArtifact whether the resource will be part of the target extension artifact.
   * @param path                the resource's path
   * @param content             the resource's content
   */
  public GeneratedResource(boolean availableInArtifact, String path, byte[] content) {
    this.availableInArtifact = availableInArtifact;
    this.path = path;
    this.content = content;
  }

  /**
   * Resources that are not available in the target extension artifact may be used during the build process of the extension as
   * temporary data.
   * 
   * @return whether the resource will be part of the target extension artifact.
   */
  public boolean isAvailableInArtifact() {
    return availableInArtifact;
  }

  /**
   * The path in which the resource will be found after being generated. Also serves as the resource's primary key
   *
   * @return a {@link java.lang.String}. Cannot empty nor {@code null}
   */
  public String getPath() {
    return path;
  }

  /**
   * The resource's content
   *
   * @return a non {@code null} {@link byte[]}
   */
  public byte[] getContent() {
    return content;
  }
}
