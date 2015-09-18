/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.api.extension.introspection.property;

/**
 * Represents an extension's capability to be mapped and usable
 * from a Mule XML config
 *
 * @since 1.0
 */
public interface XmlModelProperty
{

    /**
     * A unique key to identify this model property from others
     */
    String KEY = "XML.MODEL.PROPERTY";

    /**
     * The version of the module. Defaults to 1.0.
     */
    String getSchemaVersion();

    /**
     * Namespace of the module
     */
    String getNamespace();

    /**
     * Location URI for the schema
     */
    String getSchemaLocation();

}
