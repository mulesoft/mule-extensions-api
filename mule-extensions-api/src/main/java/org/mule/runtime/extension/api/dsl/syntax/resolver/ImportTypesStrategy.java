/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.dsl.syntax.resolver;

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
