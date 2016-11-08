/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.xml.dsl.api.resolver;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.XmlDslModel;

import java.util.Map;

/**
 * Parameterize how the {@link DslSyntaxResolver} would behave when dealing with imported types from other extensions.
 * 
 * @since 1.0
 */
public interface ImportTypesStrategy {

  /**
   *
   * @return {@link Map} with imported types mapping
   */
  Map<MetadataType, XmlDslModel> getImportedTypes();
}
