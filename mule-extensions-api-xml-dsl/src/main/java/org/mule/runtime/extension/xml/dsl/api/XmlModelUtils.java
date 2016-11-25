/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.api;

import static org.mule.runtime.extension.api.util.NameUtils.defaultNamespace;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.extension.api.declaration.type.annotation.XmlHintsAnnotation;

import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

/**
 * Utils class for parsing and generation of Xml related values of an {@link ExtensionModel extension}
 *
 * @since 1.0
 */
public final class XmlModelUtils {

  private static final String XSD_EXTENSION = ".xsd";
  private static final String CURRENT_VERSION = "current";
  private static final String DEFAULT_SCHEMA_LOCATION_MASK = "http://www.mulesoft.org/schema/mule/%s";


  public static XmlDslModel createXmlLanguageModel(Xml xml, String extensionName, String extensionVersion) {

    String namespace = calculateValue(xml, () -> xml.namespace(), () -> defaultNamespace(extensionName));
    String namespaceLocation = calculateValue(xml, () -> xml.namespaceLocation(), () -> buildDefaultLocation(namespace));
    String xsdFileName = buildDefaultXsdFileName(namespace);
    String schemaLocation = buildDefaultSchemaLocation(namespaceLocation, xsdFileName);

    return XmlDslModel.builder()
        .setSchemaVersion(extensionVersion)
        .setNamespace(namespace)
        .setNamespaceUri(namespaceLocation)
        .setSchemaLocation(schemaLocation)
        .setXsdFileName(xsdFileName)
        .build();
  }

  /**
   * @param metadataType a type
   * @return Whether the given {@code metadataType} can be defined as a top level element
   */
  public static boolean supportsTopLevelDeclaration(MetadataType metadataType) {
    if (metadataType instanceof ObjectType) {
      return metadataType.getAnnotation(XmlHintsAnnotation.class)
          .map(XmlHintsAnnotation::allowsTopLevelDefinition)
          .orElse(false);
    }

    return false;
  }

  private static String calculateValue(Xml xml, Supplier<String> value, Supplier<String> fallback) {
    if (xml != null) {
      String result = value.get();
      if (StringUtils.isNotBlank(result)) {
        return result;
      }
    }
    return fallback.get();
  }

  private static String buildDefaultLocation(String namespace) {
    return String.format(DEFAULT_SCHEMA_LOCATION_MASK, namespace);
  }

  private static String buildDefaultXsdFileName(String namespace) {
    return String.format("mule-%s%s", namespace, XSD_EXTENSION);
  }

  private static String buildDefaultSchemaLocation(String namespaceLocation, String xsdFileName) {
    return String.format("%s/%s/%s", namespaceLocation, CURRENT_VERSION, xsdFileName);
  }
}
