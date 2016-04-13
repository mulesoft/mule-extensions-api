/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.manifest;

import org.mule.runtime.extension.api.manifest.DescriberManifest;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Internal representation of a {@link DescriberManifest} which contains
 * the necessary annotations and setters necessary to allow JAX-B
 * serialization/deserialization.
 *
 * For internal uses only. Should not be instantiated outside the context
 * of a {@link ExtensionManifestXmlSerializer}
 *
 * @since 1.0
 */
@XmlRootElement(name = "describer")
@XmlAccessorType(XmlAccessType.FIELD)
final class XmlDescriberManifest implements DescriberManifest
{

    @XmlElement
    private String id;

    @XmlElement
    @XmlJavaTypeAdapter(XmlPropertyAdapter.class)
    private Map<String, String> properties;

    @Override
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @Override
    public Map<String, String> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }
}
