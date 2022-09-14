/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.declaration.type;

import static org.mule.metadata.api.model.MetadataFormat.JAVA;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.extension.api.runtime.config.ConfigurationProvider;

/**
 * Some common {@link MetadataType}s.
 */
public class MetadataTypeConstants {

  /**
   * {@link MetadataType} representing a {@link ConfigurationProvider}, suitable for config-ref parameters.
   */
  public static final MetadataType CONFIG_TYPE = BaseTypeBuilder.create(JAVA)
      .objectType()
      .id(ConfigurationProvider.class.getName())
      .with(new ClassInformationAnnotation(ConfigurationProvider.class))
      .build();

  private MetadataTypeConstants() {}
}


