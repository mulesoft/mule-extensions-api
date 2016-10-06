/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.metadata;

import org.mule.runtime.api.metadata.resolving.InputTypeResolver;
import org.mule.runtime.api.metadata.resolving.TypeKeysResolver;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

/**
 * Contract for models capable of providing a {@link MetadataResolverFactory}
 *
 * @since 1.0
 */
public interface MetadataEnrichableModel {

  /**
   * @return the {@link MetadataResolverFactory} required to instantiate the
   * {@link TypeKeysResolver}, {@link InputTypeResolver} and {@link OutputTypeResolver}
   * associated to this component's model.
   */
  MetadataResolverFactory getMetadataResolverFactory();

}
