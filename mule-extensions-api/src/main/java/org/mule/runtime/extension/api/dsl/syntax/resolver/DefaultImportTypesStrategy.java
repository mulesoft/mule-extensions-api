/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.syntax.resolver;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.XmlDslModel;

import java.util.Map;

/**
 * Default {@link ImportTypesStrategy} implementation that fails if an extension could not be located in the current context.
 * 
 * @since 1.0
 */
public class DefaultImportTypesStrategy implements ImportTypesStrategy {

  private final ExtensionModel extensionModel;
  private final DslResolvingContext context;

  public DefaultImportTypesStrategy(ExtensionModel extensionModel, DslResolvingContext context) {
    this.extensionModel = extensionModel;
    this.context = context;
  }

  @Override
  public Map<MetadataType, XmlDslModel> getImportedTypes() {
    return extensionModel.getImportedTypes().stream().collect(toMap(ImportedTypeModel::getImportedType, imported -> {
      ExtensionModel extensionModel = context.getExtension(imported.getOriginExtensionName())
          .orElseThrow(() -> new IllegalArgumentException(format(
                                                                 "The Extension [%s] is not present in the current context",
                                                                 imported.getOriginExtensionName())));
      return extensionModel.getXmlDslModel();
    }));
  }
}
