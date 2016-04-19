/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.manifest;

import static java.util.stream.Collectors.toMap;

import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * An {@link XmlAdapter} used to bridge between a {@link Map} of {@link String strings}
 * and a {@link XmlProperties} instance
 */
final class XmlPropertyAdapter extends XmlAdapter<XmlProperties, Map<String, String>>
{

    @Override
    public Map<String, String> unmarshal(XmlProperties properties) throws Exception
    {
        return properties.getProperties().stream().collect(toMap(XmlProperty::getKey, XmlProperty::getValue));
    }

    @Override
    public XmlProperties marshal(Map<String, String> properties) throws Exception
    {
        return new XmlProperties(properties);
    }
}
