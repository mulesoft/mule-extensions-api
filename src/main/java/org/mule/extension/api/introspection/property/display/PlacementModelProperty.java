/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.property.display;

import org.mule.extension.api.introspection.ParameterModel;

/**
 * Represents a {@link ParameterModel} capability to be located in the extension configuration window.
 *
 * @since 1.0
 */
public interface PlacementModelProperty
{
    /**
     * A unique key to identify this model property from others
     */
    String KEY = "PLACEMENT.MODEL.PROPERTY";

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
