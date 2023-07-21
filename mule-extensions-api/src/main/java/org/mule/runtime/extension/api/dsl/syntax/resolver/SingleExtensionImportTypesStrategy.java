/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
