/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.runtime;

import org.junit.Test;

public class FixedContentMetadataTestCase extends AbstractContentMetadataTestCase
{

    @Override
    protected ContentMetadata createContentMetadata()
    {
        return new FixedContextMetadata(inputContentType, outputContentType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setOutputContentType()
    {
        contentMetadata.setOutputContentType(inputContentType);
    }


}
