/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.syntax.resolver;

import static java.lang.String.format;
import org.mule.metadata.api.annotation.TypeIdAnnotation;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.XmlDslModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Default {@link ImportTypesStrategy} implementation that fails if an extension could not be located in the current context.
 * 
 * @since 1.0
 */
public class DefaultImportTypesStrategy implements ImportTypesStrategy {

  private final ExtensionModel extensionModel;
  private final DslResolvingContext context;
  private final Map<MetadataType, XmlDslModel> importedTypes;

  public DefaultImportTypesStrategy(ExtensionModel extensionModel, DslResolvingContext context) {
    this.extensionModel = extensionModel;
    this.context = context;
    this.importedTypes = generateImportedTypes();
  }

  private Map<MetadataType, XmlDslModel> generateImportedTypes() {
    Map<MetadataType, XmlDslModel> types = new HashMap<>();
    extensionModel.getImportedTypes().stream().map(ImportedTypeModel::getImportedType).forEach(importedType -> {
      importedType.getAnnotation(TypeIdAnnotation.class).map(TypeIdAnnotation::getValue).ifPresent(typeId -> {
        ExtensionModel extensionModel = context.getExtensionForType(typeId)
            .orElseThrow(() -> new IllegalArgumentException(format("Couldn't load type '%s' because its declaring extension wasn't present in the current context",
                                                                   typeId)));
        types.put(importedType, extensionModel.getXmlDslModel());
      });
    });

    return types;
  }

  @Override
  public Map<MetadataType, XmlDslModel> getImportedTypes() {
    return importedTypes;
  }
}
