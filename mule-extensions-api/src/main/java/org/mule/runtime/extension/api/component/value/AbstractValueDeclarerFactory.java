/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.component.value;

import static java.lang.String.format;
import static java.util.ServiceLoader.load;

import org.mule.api.annotation.NoExtend;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ADD JDOC
 */
@NoExtend
public abstract class AbstractValueDeclarerFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractValueDeclarerFactory.class);

  static {
    try {
      final AbstractValueDeclarerFactory factory = load(AbstractValueDeclarerFactory.class).iterator().next();
      LOGGER.info(format("Loaded BindingContextBuilderFactory implementation '%s' from classloader '%s'",
                         factory.getClass().getName(), factory.getClass().getClassLoader().toString()));

      DEFAULT_FACTORY = factory;
    } catch (Throwable t) {
      LOGGER.error("Error loading BindingContextBuilderFactory implementation.", t);
      throw t;
    }
  }

  private static final AbstractValueDeclarerFactory DEFAULT_FACTORY;

  public static AbstractValueDeclarerFactory getDefaultValueDeclarerFactory() {
    return DEFAULT_FACTORY;
  }

  public abstract ValueDeclarer create(ParameterModel parameterModel);

}
