/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.extension.api.introspection.property;

import org.mule.extension.api.introspection.ParameterModel;

/**
 * Represents the {@link ParameterModel} properties related to its UI.
 *
 * @since 1.0
 */
public interface DisplayModelProperty
{

    String KEY = "DISPLAY.MODEL.PROPERTY";

    /**
     * The name that will be used for displaying
     */
    String getDisplayName();

    /**
     * Returns whether the field should be masked in the UI or not
     */
    boolean isPassword();

    /**
     * Returns whether the field should use a multi line string editor in the UI or not
     */
    boolean isText();

    /**
     * The order of the parameter within its group.
     */
    int getOrder();

    /**
     * The group element name where the parameter is going to be located.
     */
    String getGroupName();

    /**
     * The tab element name where parameter and its group it's going to be located.
     */
    String getTabName();
}

