/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.runtime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MutableContentMetadataTestCase extends AbstractContentMetadataTestCase
{

    @Override
    public void before()
    {
        super.before();
        modifiable = true;
        outputContentType = inputContentType;
    }

    @Override
    protected ContentMetadata createContentMetadata()
    {
        return new MutableContentMetadata(inputContentType);
    }

    @Test
    public void setOutputContentType()
    {
        contentMetadata.setOutputContentType(outputContentType);
        assertThat(contentMetadata.getOutputContentType(), is(sameInstance(outputContentType)));
    }
}
