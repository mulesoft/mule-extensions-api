/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.property;

import org.mule.runtime.extension.api.annotation.param.display.Placement;

/**
 * Builder for {@link LayoutModelProperty}
 *
 * @since 1.0
 */
public class LayoutModelPropertyBuilder
{

    private boolean isPassword;
    private boolean isText;
    private int order;
    private String groupName;
    private String tabName;

    public static LayoutModelPropertyBuilder create()
    {
        return new LayoutModelPropertyBuilder();
    }

    public static LayoutModelPropertyBuilder create(LayoutModelProperty layoutProperty)
    {
        return new LayoutModelPropertyBuilder()
                .groupName(layoutProperty.getGroupName())
                .order(layoutProperty.getOrder())
                .tabName(layoutProperty.getTabName())
                .withPassword(layoutProperty.isPassword())
                .withText(layoutProperty.isText());
    }

    private LayoutModelPropertyBuilder()
    {
        this.isPassword = false;
        this.isText = false;
        this.order = Placement.DEFAULT_ORDER;
        this.groupName = "";
        this.tabName = "";
    }

    public LayoutModelPropertyBuilder withPassword(boolean isPassword)
    {
        this.isPassword = isPassword;
        return this;
    }

    public LayoutModelPropertyBuilder withText(boolean isText)
    {
        this.isText = isText;
        return this;
    }

    public LayoutModelPropertyBuilder tabName(String tabName)
    {
        this.tabName = tabName;
        return this;
    }

    public LayoutModelPropertyBuilder groupName(String groupName)
    {
        this.groupName = groupName;
        return this;
    }

    public LayoutModelPropertyBuilder order(int order)
    {
        this.order = order;
        return this;
    }

    public LayoutModelProperty build()
    {
        return new LayoutModelProperty(
                isPassword,
                isText,
                order,
                groupName,
                tabName);
    }
}
