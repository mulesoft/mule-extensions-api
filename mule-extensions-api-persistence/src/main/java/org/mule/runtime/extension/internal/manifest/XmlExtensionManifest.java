/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.manifest;

import org.mule.runtime.extension.api.manifest.DescriberManifest;
import org.mule.runtime.extension.api.manifest.ExtensionManifest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Internal representation of a {@link XmlExtensionManifest} which contains
 * the necessary annotations and setters necessary to allow JAX-B
 * serialization/deserialization.
 * <p>
 * This class is for internal use only. Users should not reference it.
 *
 * @since 1.0
 */
@XmlRootElement(name = "extension-manifest")
@XmlAccessorType(XmlAccessType.FIELD)
public final class XmlExtensionManifest implements ExtensionManifest
{

    @XmlElement
    private String name;

    @XmlElement
    private String description;

    @XmlElement
    private String version;

    @XmlElementWrapper(name = "exportedPackages")
    @XmlElement(name = "package")
    private List<String> exportedPackages = new ArrayList<>();

    @XmlElementWrapper(name = "exportedResources")
    @XmlElement(name = "resource")
    private List<String> exportedResources = new ArrayList<>();

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

    @Override
    public List<String> getExportedPackages()
    {
        return exportedPackages;
    }

    public void setExportedPackages(List<String> exportedPackages)
    {
        this.exportedPackages = exportedPackages;
    }

    @Override
    public List<String> getExportedResources()
    {
        return exportedResources;
    }

    public void setExportedResources(List<String> exportedResources)
    {
        this.exportedResources = exportedResources;
    }
}
