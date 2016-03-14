/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.property;

/**
 * Immutable implementation of {@link DisplayModelProperty}
 *
 * @since 1.0
 */
public class ImmutableDisplayModelProperty implements DisplayModelProperty
{
    private final String displayName;
    private final boolean isPassword;
    private final boolean isText;
    private final int order;
    private final String groupName;
    private final String tabName;

    public ImmutableDisplayModelProperty(String displayName,
                                         boolean isPassword,
                                         boolean isText,
                                         int order,
                                         String groupName,
                                         String tabName)
    {
        this.displayName = displayName;
        this.isPassword = isPassword;
        this.isText = isText;
        this.order = order;
        this.groupName = groupName;
        this.tabName = tabName;
    }

    @Override
    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public boolean isPassword()
    {
        return isPassword;
    }

    @Override
    public boolean isText()
    {
        return isText;
    }

    @Override
    public int getOrder()
    {
        return order;
    }

    @Override
    public String getGroupName()
    {
        return groupName;
    }

    @Override
    public String getTabName()
    {
        return tabName;
    }
}
