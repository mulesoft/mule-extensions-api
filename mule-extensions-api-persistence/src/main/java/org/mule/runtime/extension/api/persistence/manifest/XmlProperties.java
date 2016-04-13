/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.manifest;

import static java.util.stream.Collectors.toList;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "properties")
final class XmlProperties
{

    @XmlElement(name = "property")
    private List<XmlProperty> properties;

    XmlProperties()
    {
        properties = new LinkedList<>();
    }

    XmlProperties(Map<String, String> properties)
    {
        this.properties = properties.entrySet().stream()
                .map(entry -> new XmlProperty(entry.getKey(), entry.getValue()))
                .collect(toList());
    }


    public List<XmlProperty> getProperties()
    {
        return properties;
    }
}
