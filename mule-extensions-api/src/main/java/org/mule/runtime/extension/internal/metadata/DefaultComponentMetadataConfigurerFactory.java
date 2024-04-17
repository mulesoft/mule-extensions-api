/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.metadata;

import org.mule.runtime.api.util.LazyValue;
import org.mule.runtime.extension.api.metadata.ComponentMetadataConfigurer;
import org.mule.runtime.extension.api.metadata.ComponentMetadataConfigurerFactory;

import static java.util.ServiceLoader.load;

public class DefaultComponentMetadataConfigurerFactory extends ComponentMetadataConfigurerFactory {

  private static final LazyValue<ComponentMetadataConfigurerFactoryDelegate> DELEGATE =
      new LazyValue(() -> load(ComponentMetadataConfigurerFactoryDelegate.class,
                               ComponentMetadataConfigurerFactoryDelegate.class.getClassLoader()).iterator().next());

  @Override
  public ComponentMetadataConfigurer create() {
    ComponentMetadataConfigurerFactoryDelegate delegate = DELEGATE.get();
    if (delegate == null) {
      return null;
    }
    return delegate.create();
  }
}
