/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.metadata;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.extension.internal.metadata.DefaultComponentMetadataConfigurerFactory;

/**
 * Factory of {@link ComponentMetadataConfigurer}.
 *
 * @since 1.8
 */
@NoImplement
public abstract class ComponentMetadataConfigurerFactory {

  /**
   * @return an implementation of {@link ComponentMetadataConfigurer}.
   */
  public abstract ComponentMetadataConfigurer create();

  public static ComponentMetadataConfigurerFactory getDefault() {
    return new DefaultComponentMetadataConfigurerFactory();
  }

}
