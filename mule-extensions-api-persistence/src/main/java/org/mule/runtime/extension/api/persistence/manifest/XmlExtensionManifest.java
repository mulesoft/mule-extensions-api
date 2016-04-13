/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.manifest;

import org.mule.runtime.extension.api.manifest.DescriberManifest;
import org.mule.runtime.extension.api.manifest.ExtensionManifest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Internal representation of a {@link XmlExtensionManifest} which contains
 * the necessary annotations and setters necessary to allow JAX-B
 * serialization/deserialization.
 *
 * For internal uses only. Should not be instantiated outside the context
 * of a {@link ExtensionManifestXmlSerializer}
 *
 * @since 1.0
 */
@XmlRootElement(name = "extension-manifest")
@XmlAccessorType(XmlAccessType.FIELD)
final class XmlExtensionManifest implements ExtensionManifest
{

    @XmlElement
    private String name;

    @XmlElement
    private String description;

    @XmlElement
    private String version;

    @XmlElement(name = "describer")
    private XmlDescriberManifest describerManifest;

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public String getVersion()
    {
        return version;
    }

    @Override
    public DescriberManifest getDescriberManifest()
    {
        return describerManifest;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public void setDescriberManifest(XmlDescriberManifest describerManifest)
    {
        this.describerManifest = describerManifest;
    }
}
