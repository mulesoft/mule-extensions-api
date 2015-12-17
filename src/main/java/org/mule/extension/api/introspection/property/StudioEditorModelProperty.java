/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.property;

/**
 * Created by pablocabrera on 11/18/15.
 */
public interface StudioEditorModelProperty
{

    /**
     * A unique key to identify this model property from others
     */
    String KEY = "STUDIO.EDITOR.MODEL.PROPERTY";

    /**
     * The version of the module. Defaults to editors.xml.
     */
    String getFileName();
}
