/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader.delegate;

/**
 * {@link ModelLoaderDelegate} factory
 *
 * @since 4.1
 */
@FunctionalInterface
public interface ModelLoaderDelegateFactory {

  /**
   * Returns a new {@link ModelLoaderDelegate} instance bound to the given {@code version} and {@code loaderId}.
   *
   * @param version  the extension's version
   * @param loaderId the ID of the loader which will be using this delegate.
   * @return a new {@link ModelLoaderDelegate}
   */
  ModelLoaderDelegate getLoader(String version, String loaderId);

}
