/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.manifest;

import org.mule.runtime.extension.api.manifest.DescriberManifest;
import org.mule.runtime.extension.api.manifest.ExtensionManifest;
import org.mule.runtime.extension.api.manifest.ExtensionManifestBuilder;
import org.mule.runtime.extension.internal.manifest.XmlDescriberManifest;
import org.mule.runtime.extension.internal.manifest.XmlExtensionManifest;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Serializer capable of marshalling an {@link ExtensionManifest} instance
 * to {@code XML} format and back
 *
 * @since 1.0
 */
public final class ExtensionManifestXmlSerializer
{

    /**
     * Serializes the given {@code manifest} to XML
     *
     * @param manifest the manifest to be marshaled
     * @return the resulting {@code XML} in {@link String} format
     */
    public String serialize(ExtensionManifest manifest)
    {
        final XmlExtensionManifest xmlManifest = new XmlExtensionManifest();
        xmlManifest.setName(manifest.getName());
        xmlManifest.setDescription(manifest.getDescription());
        xmlManifest.setVersion(manifest.getVersion());
        xmlManifest.setExportedPackages(manifest.getExportedPackages());
        xmlManifest.setExportedResources(manifest.getExportedResources());
        xmlManifest.setDescriberManifest(asXml(manifest.getDescriberManifest()));

        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(XmlExtensionManifest.class);
            Marshaller marshaller = jaxbContext.createMarshaller();

            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            marshaller.marshal(xmlManifest, getXMLSerializer(out).asContentHandler());

            return out.toString();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserializes the given {@code xml} back into a {@link ExtensionManifest}
     *
     * @param xml a manifest {@code XML} in {@link String} format
     * @return an {@link ExtensionManifest} instance
     */
    public ExtensionManifest deserialize(String xml)
    {
        XmlExtensionManifest xmlManifest;
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(XmlExtensionManifest.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            xmlManifest = (XmlExtensionManifest) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        ExtensionManifestBuilder builder = new ExtensionManifestBuilder();
        builder.setName(xmlManifest.getName())
                .setDescription(xmlManifest.getDescription())
                .setVersion(xmlManifest.getVersion())
                .addExportedPackages(xmlManifest.getExportedPackages())
                .addExportedResources(xmlManifest.getExportedResources())
                .withDescriber()
                .setId(xmlManifest.getDescriberManifest().getId())
                .addProperties(xmlManifest.getDescriberManifest().getProperties());

        return builder.build();
    }

    private XMLSerializer getXMLSerializer(OutputStream out)
    {
        OutputFormat of = new OutputFormat();

        of.setCDataElements(new String[] {"^description"});
        of.setIndenting(true);

        XMLSerializer serializer = new XMLSerializer(of);
        serializer.setOutputByteStream(out);

        return serializer;
    }

    private XmlDescriberManifest asXml(DescriberManifest manifest)
    {
        XmlDescriberManifest xmlManifest = new XmlDescriberManifest();
        xmlManifest.setId(manifest.getId());
        xmlManifest.setProperties(manifest.getProperties());

        return xmlManifest;
    }
}
