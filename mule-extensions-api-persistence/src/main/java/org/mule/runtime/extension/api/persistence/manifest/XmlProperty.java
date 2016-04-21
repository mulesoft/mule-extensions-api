/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.manifest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
final class XmlProperty
{

    XmlProperty()
    {
    }

    XmlProperty(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    @XmlAttribute
    private String key;

    @XmlAttribute
    private String value;

    public String getKey()
    {
        return key;
    }

    public String getValue()
    {
        return value;
    }
}
