/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.property.display;

/**
 * Immutable implementation of {@link PlacementModelProperty}
 *
 * @since 1.0
 */
public final class ImmutablePlacementModelProperty implements PlacementModelProperty
{

    private final int order;
    private final String group;
    private final String tab;

    public ImmutablePlacementModelProperty(int order, String group, String tab)
    {
        this.order = order;
        this.group = group;
        this.tab = tab;
    }

    public int getOrder()
    {
        return order;
    }

    @Override
    public String getGroupName()
    {
        return group;
    }

    @Override
    public String getTabName()
    {
        return tab;
    }
}
