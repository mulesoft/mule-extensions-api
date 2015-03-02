/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.spi;

import org.mule.extension.introspection.Describer;
import org.mule.extension.introspection.DescribingContext;
import org.mule.extension.introspection.Extension;
import org.mule.extension.introspection.declaration.Construct;
import org.mule.extension.introspection.declaration.Declaration;

/**
 * A post processor that allows doing extra task before a
 * {@link Describer} finishes
 * describing a {@link Construct}
 * <p/>
 * This allows to customize the discovery process by given a hooking point
 * for manipulating the {@link Declaration} before
 * it actually generates the final {@link Extension} instance.
 * <p/>
 * Instances are to be discovered by standard SPI mechanism
 *
 * @since 1.0
 */
public interface DescriberPostProcessor
{

    /**
     * Optionally applies extra logic into any of the {@code context}'s
     * attributes
     *
     * @param context the current {@link DescribingContext}
     */
    void postProcess(DescribingContext context);

}
