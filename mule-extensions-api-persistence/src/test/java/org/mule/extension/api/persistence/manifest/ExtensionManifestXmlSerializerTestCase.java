/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.persistence.manifest;

import static org.custommonkey.xmlunit.XMLUnit.compareXML;
import static org.custommonkey.xmlunit.XMLUnit.setIgnoreAttributeOrder;
import static org.custommonkey.xmlunit.XMLUnit.setIgnoreComments;
import static org.custommonkey.xmlunit.XMLUnit.setIgnoreWhitespace;
import static org.custommonkey.xmlunit.XMLUnit.setNormalizeWhitespace;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mule.runtime.extension.tck.manifet.ExtensionManifestTestUtils.assertStringList;
import static org.mule.runtime.extension.tck.manifet.ExtensionManifestTestUtils.getTestBuilder;
import org.mule.runtime.extension.api.manifest.DescriberManifest;
import org.mule.runtime.extension.api.manifest.ExtensionManifest;
import org.mule.runtime.extension.api.persistence.manifest.ExtensionManifestXmlSerializer;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.junit.Before;
import org.junit.Test;

public class ExtensionManifestXmlSerializerTestCase
{

    private ExtensionManifest manifest;
    private ExtensionManifestXmlSerializer serializer = new ExtensionManifestXmlSerializer();

    @Before
    public void before()
    {
        manifest = getTestBuilder().build();
    }

    @Test
    public void serialize() throws Exception
    {
        String actual = serializer.serialize(manifest);
        InputStream expected = getClass().getResourceAsStream("/manifest/test-extension-manifest.xml");
        assertThat("Could not obtain expected file", expected, is(notNullValue()));

        setNormalizeWhitespace(true);
        setIgnoreWhitespace(true);
        setIgnoreComments(true);
        setIgnoreAttributeOrder(true);

        Diff diff = compareXML(IOUtils.toString(expected), actual);
        if (!(diff.similar() && diff.identical()))
        {
            System.out.println(actual);
            DetailedDiff detDiff = new DetailedDiff(diff);
            @SuppressWarnings("rawtypes")
            List differences = detDiff.getAllDifferences();
            StringBuilder diffLines = new StringBuilder();
            for (Object object : differences)
            {
                Difference difference = (Difference) object;
                diffLines.append(difference.toString() + '\n');
            }

            fail(String.format("serialized content did not match. Expected:\n%s\nObtained:\n%s", expected, actual));
        }
    }

    @Test
    public void deserialize() throws Exception
    {
        ExtensionManifest deserialized = serializer.deserialize(serializer.serialize(manifest));
        assertThat(deserialized.getName(), equalTo(manifest.getName()));
        assertThat(deserialized.getDescription(), equalTo(manifest.getDescription()));
        assertThat(deserialized.getVersion(), equalTo(manifest.getVersion()));
        assertStringList(manifest.getExportedPackages(), deserialized.getExportedPackages());
        assertStringList(manifest.getExportedResources(), deserialized.getExportedResources());

        DescriberManifest deserializedDescriber = deserialized.getDescriberManifest();
        DescriberManifest manifestDescriber = manifest.getDescriberManifest();

        assertThat(deserializedDescriber.getId(), equalTo(manifestDescriber.getId()));
        assertThat(deserializedDescriber.getProperties().size(), equalTo(manifestDescriber.getProperties().size()));

        deserializedDescriber.getProperties().entrySet().forEach(entry -> assertThat(manifestDescriber.getProperties().get(entry.getKey()), equalTo(entry.getValue())));
    }
}
