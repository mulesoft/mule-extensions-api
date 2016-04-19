/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.manifest;

import static java.util.stream.Collectors.toList;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Allows serializing {@link Map} instances as a sequence of {@code &lt;property&gt;} elements.
 * <p>
 * This class is for internal use only. Users should not reference it.
 *
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "properties")
public final class XmlProperties
{

    @XmlElement(name = "property")
    private List<XmlProperty> properties;

    /**
     * Creates a new instance with empty {@link #properties}
     */
    XmlProperties()
    {
        properties = new LinkedList<>();
    }

    /**
     * Creates a new instance initialised with the given properties
     *
     * @param properties initial properties
     */
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
