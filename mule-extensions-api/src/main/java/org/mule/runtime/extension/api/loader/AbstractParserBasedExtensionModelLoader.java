/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;

import static org.mule.runtime.api.util.classloader.MuleImplementationLoaderUtils.getMuleImplementationsLoader;
import static org.mule.runtime.extension.api.ExtensionConstants.VERSION_PROPERTY_NAME;

import static java.util.ServiceLoader.load;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

import org.mule.runtime.extension.api.loader.delegate.ModelLoaderDelegate;
import org.mule.runtime.extension.api.loader.delegate.ModelLoaderDelegateFactory;
import org.mule.runtime.extension.api.loader.parser.ExtensionModelParserFactory;

/**
 * Base implementation for an {@link ExtensionModelLoader}
 *
 * @since 1.10.0
 */
public abstract class AbstractParserBasedExtensionModelLoader extends ExtensionModelLoader {

  /**
   * @param context the loading context
   * @return the {@link ExtensionModelParserFactory} to be used
   */
  protected abstract ExtensionModelParserFactory getExtensionModelParserFactory(ExtensionLoadingContext context);

  /**
   * {@inheritDoc}
   */
  @Override
  protected final void declareExtension(ExtensionLoadingContext context) {
    String version =
        context.<String>getParameter(VERSION_PROPERTY_NAME)
            .orElseThrow(() -> new IllegalArgumentException("version not specified"));

    ExtensionModelParserFactory parserFactory = getExtensionModelParserFactory(context);
    getModelLoaderDelegate(context, version).declare(parserFactory, context);
  }

  protected ModelLoaderDelegate getModelLoaderDelegate(ExtensionLoadingContext context, String version) {
    return stream(load(ModelLoaderDelegateFactory.class, getMuleImplementationsLoader()).spliterator(), false)
        .collect(collectingAndThen(toList(), list -> {
          if (list.size() > 1) {
            throw new IllegalStateException("Found more than one implementation for "
                + ModelLoaderDelegateFactory.class.getName());
          } else if (list.isEmpty()) {
            throw new IllegalStateException("No implementation found for " + ModelLoaderDelegateFactory.class.getName());
          }

          return list.get(0);
        }))
        .getLoader(version, getId());
  }
}
