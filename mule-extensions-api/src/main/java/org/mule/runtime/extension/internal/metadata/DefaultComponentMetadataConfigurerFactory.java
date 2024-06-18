/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.metadata;

import static java.util.ServiceLoader.load;

import static org.mule.runtime.api.util.classloader.MuleImplementationLoaderUtils.getMuleImplementationsLoader;
import static org.mule.runtime.api.i18n.I18nMessageFactory.createStaticMessage;

import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.runtime.api.util.LazyValue;
import org.mule.runtime.extension.api.metadata.ComponentMetadataConfigurer;
import org.mule.runtime.extension.api.metadata.ComponentMetadataConfigurerFactory;
import org.mule.runtime.extension.api.metadata.ComponentMetadataConfigurerFactoryDelegate;

import java.util.Iterator;

public class DefaultComponentMetadataConfigurerFactory extends ComponentMetadataConfigurerFactory {

  private static final LazyValue<ComponentMetadataConfigurerFactoryDelegate> DELEGATE =
      new LazyValue(() -> {
        Iterator<ComponentMetadataConfigurerFactoryDelegate> iter =
            load(ComponentMetadataConfigurerFactoryDelegate.class,
                 getMuleImplementationsLoader()).iterator();
        if (!iter.hasNext()) {
          throw new MuleRuntimeException(createStaticMessage("There is no implementation available for %s.",
                                                             ComponentMetadataConfigurerFactoryDelegate.class.getName()));
        }
        return iter.next();
      });

  @Override
  public ComponentMetadataConfigurer create() {
    ComponentMetadataConfigurerFactoryDelegate delegate = DELEGATE.get();
    return delegate.create();
  }
}
