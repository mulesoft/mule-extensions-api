/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.manifest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.extension.api.introspection.declaration.tck.TestHttpConnectorDeclarer.EXTENSION_DESCRIPTION;
import static org.mule.runtime.extension.api.introspection.declaration.tck.TestHttpConnectorDeclarer.EXTENSION_NAME;
import static org.mule.runtime.extension.api.introspection.declaration.tck.TestHttpConnectorDeclarer.VERSION;
import org.mule.runtime.extension.api.introspection.declaration.tck.TestHttpConnectorDeclarer;

import org.junit.Before;
import org.junit.Test;

public class ExtensionManifestBuilderTestCase
{

    private static final String DESCRIBER_ID = "annotations";
    private static final String DESCRIBER_PROPERTY = "type";
    private static final String DESCRIBER_PROPERTY_VALUE = TestHttpConnectorDeclarer.class.getName();

    private ExtensionManifestBuilder builder;

    @Before
    public void before()
    {
        builder = new ExtensionManifestBuilder();
        builder.setName(EXTENSION_NAME)
                .setDescription(EXTENSION_DESCRIPTION)
                .setVersion(VERSION)
                .withDescriber()
                .setId(DESCRIBER_ID)
                .addProperty(DESCRIBER_PROPERTY, DESCRIBER_PROPERTY_VALUE);

    }

    @Test
    public void build()
    {
        ExtensionManifest manifest = builder.build();
        assertThat(manifest.getName(), is(EXTENSION_NAME));
        assertThat(manifest.getDescription(), is(EXTENSION_DESCRIPTION));
        assertThat(manifest.getVersion(), is(VERSION));

        DescriberManifest describerManifest = manifest.getDescriberManifest();
        assertThat(describerManifest.getId(), is(DESCRIBER_ID));
        assertThat(describerManifest.getProperties().size(), is(1));
        assertThat(describerManifest.getProperties().get(DESCRIBER_PROPERTY), is(DESCRIBER_PROPERTY_VALUE));
    }

    @Test(expected = IllegalStateException.class)
    public void blankName()
    {
        builder.setName(null);
        builder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void blankVersion()
    {
        builder.setVersion(null);
        builder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void blankDescriberId()
    {
        builder.withDescriber().setId(null);
        builder.build();
    }
}
