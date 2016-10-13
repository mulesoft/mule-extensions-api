/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.manifest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.api.meta.model.tck.TestHttpConnectorDeclarer.EXTENSION_DESCRIPTION;
import static org.mule.runtime.api.meta.model.tck.TestHttpConnectorDeclarer.EXTENSION_NAME;
import static org.mule.runtime.api.meta.model.tck.TestHttpConnectorDeclarer.MIN_MULE_VERSION;
import static org.mule.runtime.api.meta.model.tck.TestHttpConnectorDeclarer.VERSION;
import static org.mule.runtime.extension.tck.manifet.ExtensionManifestTestUtils.DESCRIBER_ID;
import static org.mule.runtime.extension.tck.manifet.ExtensionManifestTestUtils.DESCRIBER_PROPERTY;
import static org.mule.runtime.extension.tck.manifet.ExtensionManifestTestUtils.DESCRIBER_PROPERTY_VALUE;
import static org.mule.runtime.extension.tck.manifet.ExtensionManifestTestUtils.EXPORTED_PACKAGES;
import static org.mule.runtime.extension.tck.manifet.ExtensionManifestTestUtils.EXPORTED_RESOURCES;
import static org.mule.runtime.extension.tck.manifet.ExtensionManifestTestUtils.assertStringList;
import org.mule.runtime.extension.tck.manifet.ExtensionManifestTestUtils;

import org.junit.Before;
import org.junit.Test;

public class ExtensionManifestBuilderTestCase {

  private ExtensionManifestBuilder builder;

  @Before
  public void before() {
    builder = ExtensionManifestTestUtils.getTestBuilder();
  }

  @Test
  public void build() {
    ExtensionManifest manifest = builder.build();
    assertThat(manifest.getName(), is(EXTENSION_NAME));
    assertThat(manifest.getDescription(), is(EXTENSION_DESCRIPTION));
    assertThat(manifest.getVersion(), is(VERSION));
    assertThat(manifest.getMinMuleVersion(), is(MIN_MULE_VERSION));
    assertStringList(manifest.getExportedPackages(), EXPORTED_PACKAGES);
    assertStringList(manifest.getExportedResources(), EXPORTED_RESOURCES);

    DescriberManifest describerManifest = manifest.getDescriberManifest();
    assertThat(describerManifest.getId(), is(DESCRIBER_ID));
    assertThat(describerManifest.getProperties().size(), is(1));
    assertThat(describerManifest.getProperties().get(DESCRIBER_PROPERTY), is(DESCRIBER_PROPERTY_VALUE));
  }

  @Test(expected = IllegalStateException.class)
  public void blankName() {
    builder.setName(null);
    builder.build();
  }

  @Test(expected = IllegalStateException.class)
  public void blankVersion() {
    builder.setVersion(null);
    builder.build();
  }

  @Test(expected = IllegalStateException.class)
  public void blankDescriberId() {
    builder.withDescriber().setId(null);
    builder.build();
  }
}
