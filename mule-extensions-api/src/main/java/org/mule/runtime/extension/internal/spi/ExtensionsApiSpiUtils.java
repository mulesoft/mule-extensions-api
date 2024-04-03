/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.spi;

import static java.util.ServiceLoader.load;
import static java.util.stream.StreamSupport.stream;

import org.mule.runtime.extension.api.dsl.syntax.resources.spi.DslResourceFactory;
import org.mule.runtime.extension.api.dsl.syntax.resources.spi.ExtensionSchemaGenerator;
import org.mule.runtime.extension.api.loader.ExtensionModelLoaderProvider;
import org.mule.runtime.extension.api.resources.spi.GeneratedResourceFactory;

import java.util.stream.Stream;

/**
 * Provides utilities to lookup and load implementations of interfaces defined in {@code extensions-api} through SPI.
 * 
 * @since 1.5
 */
public final class ExtensionsApiSpiUtils {

  private ExtensionsApiSpiUtils() {
    // Nothing to do
  }

  public static Stream<ExtensionModelLoaderProvider> loadExtensionModelLoaderProviders() {
    return stream(((Iterable<ExtensionModelLoaderProvider>) () -> load(ExtensionModelLoaderProvider.class,
                                                                       ExtensionsApiSpiUtils.class.getClassLoader())
                                                                           .iterator())
                                                                               .spliterator(),
                  false);
  }

  public static Stream<ExtensionSchemaGenerator> loadExtensionSchemaGenerators() {
    return stream(((Iterable<ExtensionSchemaGenerator>) () -> load(ExtensionSchemaGenerator.class,
                                                                   ExtensionsApiSpiUtils.class.getClassLoader())
                                                                       .iterator())
                                                                           .spliterator(),
                  false);
  }

  public static Stream<DslResourceFactory> loadDslResourceFactories() {
    return stream(((Iterable<DslResourceFactory>) () -> load(DslResourceFactory.class,
                                                             ExtensionsApiSpiUtils.class.getClassLoader())
                                                                 .iterator())
                                                                     .spliterator(),
                  false);
  }

  public static Stream<GeneratedResourceFactory> loadGeneratedResourceFactories() {
    return stream(((Iterable<GeneratedResourceFactory>) () -> load(GeneratedResourceFactory.class,
                                                                   ExtensionsApiSpiUtils.class.getClassLoader())
                                                                       .iterator())
                                                                           .spliterator(),
                  false);
  }

}
