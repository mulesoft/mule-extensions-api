/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.syntax.resolver;

import static java.util.Collections.emptyMap;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.XmlDslModel;

import java.util.Map;

/**
 * {@link ImportTypesStrategy} implementation that permits the {@link DslSyntaxResolver} to work without any imported type. Useful
 * for situations in which the imported types are not relevant to the context in which the {@link DslSyntaxResolver} is being
 * used.
 *
 * @since 1.0
 */
public class SingleExtensionImportTypesStrategy implements ImportTypesStrategy {

  @Override
  public Map<MetadataType, XmlDslModel> getImportedTypes() {
    return emptyMap();
  }
}
