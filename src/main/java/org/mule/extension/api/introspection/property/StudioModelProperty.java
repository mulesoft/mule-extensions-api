/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.property;

/**
 * Represents an extension's capability to provide a custom UI description
 *
 * @since 1.0
 */
public interface StudioModelProperty
{

    /**
     * A unique key to identify this model property from others
     */
    String KEY = "STUDIO.EDITOR.MODEL.PROPERTY";

    /**
     * The name of the file used to describe the UI. Defaults to editors.xml.
     */
    String getEditorFileName();

    /**
     * To signal that an editor will be provided by the connector or studio
     */
    boolean isProvided();
}
