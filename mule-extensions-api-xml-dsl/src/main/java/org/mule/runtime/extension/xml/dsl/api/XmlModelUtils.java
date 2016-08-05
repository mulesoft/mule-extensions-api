/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.api;

import static org.mule.metadata.utils.MetadataTypeUtils.getSingleAnnotation;
import static org.mule.runtime.extension.api.util.NameUtils.defaultNamespace;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.xml.dsl.api.property.XmlHintsModelProperty;
import org.mule.runtime.extension.xml.dsl.api.property.XmlModelProperty;

import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

/**
 * Utils class for parsing and generation of Xml related values of an {@link ExtensionModel extension}
 *
 * @since 1.0
 */
public final class XmlModelUtils
{

    private static final String XSD_EXTENSION = ".xsd";
    private static final String CURRENT_VERSION = "current";
    private static final String DEFAULT_SCHEMA_LOCATION_MASK = "http://www.mulesoft.org/schema/mule/%s";

    public static XmlModelProperty createXmlModelProperty(Xml xml, String extensionName, String extensionVersion)
    {

        String namespace = calculateValue(xml, () -> xml.namespace(), () -> defaultNamespace(extensionName));
        String namespaceLocation = calculateValue(xml, () -> xml.namespaceLocation(), () -> buildDefaultLocation(namespace));
        String xsdFileName = buildDefaultXsdFileName(namespace);
        String schemaLocation = buildDefaultSchemaLocation(namespaceLocation, xsdFileName);

        return new XmlModelProperty(extensionVersion, namespace, namespaceLocation, xsdFileName, schemaLocation);
    }

    /**
     * Optionally returns a {@link XmlHintsModelProperty} associated to the given {@code parameter}.
     * <p>
     * If the {@code parameter} doesn't contain the property itself, then it checks if the
     * {@link ParameterModel#getType()} contains a {@link XmlHintsAnnotation} and if
     * so, it adapts that annotation into a model property
     *
     * @param parameter a {@link ParameterModel}
     * @return an {@link Optional} {@link XmlHintsModelProperty}
     */
    public static Optional<XmlHintsModelProperty> getHintsModelProperty(ParameterModel parameter)
    {
        Optional<XmlHintsModelProperty> property = parameter.getModelProperty(XmlHintsModelProperty.class);
        if (!property.isPresent())
        {
            property = getSingleAnnotation(parameter.getType(), XmlHintsAnnotation.class)
                    .map(annotation -> new XmlHintsModelProperty(annotation.allowsInlineDefinition(), annotation.allowsReferences()));
        }

        return property;
    }

    private static String calculateValue(Xml xml, Supplier<String> value, Supplier<String> fallback)
    {
        if (xml != null)
        {
            String result = value.get();
            if (StringUtils.isNotBlank(result))
            {
                return result;
            }
        }
        return fallback.get();
    }

    private static String buildDefaultLocation(String namespace)
    {
        return String.format(DEFAULT_SCHEMA_LOCATION_MASK, namespace);
    }

    private static String buildDefaultXsdFileName(String namespace)
    {
        return String.format("mule-%s%s", namespace, XSD_EXTENSION);
    }

    private static String buildDefaultSchemaLocation(String namespaceLocation, String xsdFileName)
    {
        return String.format("%s/%s/%s", namespaceLocation, CURRENT_VERSION, xsdFileName);
    }

}
