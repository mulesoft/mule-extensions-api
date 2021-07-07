/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.syntax.resolver;

import static java.lang.String.format;

import static org.mule.metadata.api.utils.MetadataTypeUtils.getTypeId;
import static org.mule.runtime.internal.dsl.DslConstants.CORE_NAMESPACE;
import static org.mule.runtime.internal.dsl.DslConstants.CORE_PREFIX;
import static org.mule.runtime.internal.dsl.DslConstants.CORE_SCHEMA_LOCATION;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.XmlDslModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default {@link ImportTypesStrategy} implementation that fails if an extension could not be located in the current context.
 *
 * @since 1.0
 */
public class DefaultImportTypesStrategy implements ImportTypesStrategy {

  private static final XmlDslModel MULE_XML_MODEL = XmlDslModel.builder()
      .setPrefix(CORE_PREFIX)
      .setNamespace(CORE_NAMESPACE)
      .setXsdFileName(CORE_PREFIX + ".xsd")
      .setSchemaLocation(CORE_SCHEMA_LOCATION)
      .build();

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
    extensionModel.getImportedTypes().stream()
        .map(ImportedTypeModel::getImportedType)
        .forEach(importedType -> getTypeId(importedType)
            .ifPresent(typeId -> {
              Optional<ExtensionModel> extension = context.getExtensionForType(typeId);

              if (!extension.isPresent() && !isProvidedByMule(typeId)) {
                throw new IllegalArgumentException(format("Couldn't find type '%s' definition because its declaring "
                    + "extension wasn't present in the current context",
                                                          typeId));
              }

              types.put(importedType, extension.map(ExtensionModel::getXmlDslModel).orElse(MULE_XML_MODEL));
            }));

    return types;
  }

  @Override
  public Map<MetadataType, XmlDslModel> getImportedTypes() {
    return importedTypes;
  }

  private boolean isProvidedByMule(String typeId) {
    return typeId.startsWith("org.mule.runtime") || typeId.startsWith("com.mulesoft.mule.runtime.");
  }
}
