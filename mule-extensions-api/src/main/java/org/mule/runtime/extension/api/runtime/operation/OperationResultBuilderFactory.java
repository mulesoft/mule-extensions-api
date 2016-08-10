/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.operation;

import static java.lang.String.format;
import static java.util.ServiceLoader.load;
import org.mule.runtime.api.message.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating instances of {@link OperationResult.Builder}
 * <p>
 * {@link #getDefaultFactory()} method can be used to obtain a default implementation
 * provided by the runtime
 *
 * @since 1.0
 */
public abstract class OperationResultBuilderFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(OperationResultBuilderFactory.class);
  private static final OperationResultBuilderFactory DEFAULT_FACTORY;

  static {
    try {
      final OperationResultBuilderFactory factory = load(OperationResultBuilderFactory.class).iterator().next();
      LOGGER.info(format("Loaded %s implementation '%s' form classloader '%s'",
                         OperationResultBuilderFactory.class.getSimpleName(),
                         factory.getClass().getName(),
                         factory.getClass().getClassLoader().toString()));

      DEFAULT_FACTORY = factory;
    } catch (Exception e) {
      LOGGER.error(format("Error loading %s implementation.", OperationResultBuilderFactory.class.getSimpleName()), e);
      throw e;
    }
  }


  /**
   * The implementation of this abstract class is provided by the Mule Runtime, and loaded during
   * this class initialization.
   * <p>
   * If more than one implementation is found, the classLoading order of those implementations
   * will determine which one is used. Information about this will be logged to aid in the
   * troubleshooting of those cases.
   *
   * @return the implementation of this builder factory provided by the Mule Runtime.
   */
  public static final OperationResultBuilderFactory getDefaultFactory() {
    return DEFAULT_FACTORY;
  }

  /**
   * Creates a new {@link OperationResult.Builder}
   *
   * @param <Output>     the generic type of the output value
   * @param <A> the generic type of the message attributes
   * @return a new {@link OperationResult.Builder}
   */
  public abstract <Output, A extends Attributes> OperationResult.Builder<Output, A> create();

}
