/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
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

  private final String path;
  private final byte[] content;

  /**
   * Creates a new instance
   *
   * @param path    the resource's path
   * @param content the resource's content
   */
  public GeneratedResource(String path, byte[] content) {
    this.path = path;
    this.content = content;
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
