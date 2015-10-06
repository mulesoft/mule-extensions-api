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

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

public abstract class AbstractContentMetadataTestCase
{

    protected ContentType inputContentType;
    protected ContentType outputContentType;
    protected ContentMetadata contentMetadata;
    protected boolean modifiable = false;

    protected abstract ContentMetadata createContentMetadata();

    @Before
    public void before()
    {
        inputContentType = new ContentType(Charset.defaultCharset(), "input/input");
        outputContentType = new ContentType(Charset.defaultCharset(), "output/output");
        contentMetadata = createContentMetadata();
    }

    @Test
    public void initialState()
    {
        assertThat(contentMetadata.getInputContentType(), is(sameInstance(inputContentType)));
        assertThat(contentMetadata.getOutputContentType(), is(sameInstance(outputContentType)));
    }

    @Test
    public void isModifiable()
    {
        assertThat(contentMetadata.isOutputModifiable(), is(modifiable));
    }

}
