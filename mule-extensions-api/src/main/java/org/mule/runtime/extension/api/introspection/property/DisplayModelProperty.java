/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.api.introspection.property;

import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;

/**
 * Provides UI related properties for a {@link ParameterModel}
 *
 * @since 1.0
 */
public final class DisplayModelProperty implements ModelProperty
{

    private final String displayName;
    private final boolean password;
    private final boolean text;
    private final int order;
    private final String groupName;
    private final String tabName;

    public DisplayModelProperty(String displayName,
                                boolean password,
                                boolean text,
                                int order,
                                String groupName,
                                String tabName)
    {
        this.displayName = displayName;
        this.password = password;
        this.text = text;
        this.order = order;
        this.groupName = groupName;
        this.tabName = tabName;
    }


    /**
     * @return The name that will be used for displaying
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * @return Whether the field should be masked in the UI or not
     */
    public boolean isPassword()
    {
        return password;
    }

    /**
     * @return Whether the field should use a multi line string editor in the UI or not
     */
    public boolean isText()
    {
        return text;
    }

    /**
     * @return The order of the parameter within its group.
     */
    public int getOrder()
    {
        return order;
    }

    /**
     * @return The group element name where the parameter is going to be located.
     */
    public String getGroupName()
    {
        return groupName;
    }

    /**
     * @return The tab element name where parameter and its group it's going to be located.
     */
    public String getTabName()
    {
        return tabName;
    }

    /**
     * {@inheritDoc}
     * @return {@code Display properties}
     */
    @Override
    public String getName()
    {
        return "Display properties";
    }

    /**
     * {@inheritDoc}
     * @return {@code true}
     */
    @Override
    public boolean isExternalizable()
    {
        return true;
    }
}

