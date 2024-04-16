/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.metadata;

import static java.util.ServiceLoader.load;
import java.util.Iterator;

/**
 * Factory of {@link ComponentMetadataConfigurer}.
 *
 * @since 1.8
 */
public class ComponentMetadataConfigurerFactoryUtils {

  /**
   * @return an implementation of {@link ComponentMetadataConfigurer.ComponentMetadataConfigurerFactory}.
   */
  public ComponentMetadataConfigurer.ComponentMetadataConfigurerFactory create() {
    Iterator<ComponentMetadataConfigurer.ComponentMetadataConfigurerFactory> iter =
        load(ComponentMetadataConfigurer.ComponentMetadataConfigurerFactory.class,
             ComponentMetadataConfigurer.ComponentMetadataConfigurerFactory.class.getClassLoader()).iterator();
    return iter.hasNext() ? iter.next() : null;
  }

}
