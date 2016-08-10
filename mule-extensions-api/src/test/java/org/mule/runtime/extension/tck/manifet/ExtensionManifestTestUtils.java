/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.tck.manifet;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.EXTENSION_DESCRIPTION;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.EXTENSION_NAME;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.MIN_MULE_VERSION;
import static org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer.VERSION;

import org.mule.runtime.extension.api.manifest.ExtensionManifest;
import org.mule.runtime.extension.api.manifest.ExtensionManifestBuilder;
import org.mule.runtime.extension.tck.introspection.TestHttpConnectorDeclarer;

import java.util.Arrays;
import java.util.List;

/**
 * Utilities to test functionality related to {@link ExtensionManifest}
 *
 * @since 1.0
 */
public final class ExtensionManifestTestUtils {

  public static final String DESCRIBER_ID = "annotations";
  public static final List<String> EXPORTED_PACKAGES = Arrays.asList("org.mule.foo", "org.mule.bar");
  public static final List<String> EXPORTED_RESOURCES = Arrays.asList("/META-INF/foo", "bar");
  public static final String DESCRIBER_PROPERTY = "type";
  public static final String DESCRIBER_PROPERTY_VALUE = TestHttpConnectorDeclarer.class.getName();

  private ExtensionManifestTestUtils() {}

  /**
   * Creates a new {@link ExtensionManifestBuilder} initialised
   * with a known test state
   *
   * @return a {@link ExtensionManifestBuilder}
   */
  public static ExtensionManifestBuilder getTestBuilder() {
    ExtensionManifestBuilder builder = new ExtensionManifestBuilder();
    builder.setName(EXTENSION_NAME)
        .setDescription(EXTENSION_DESCRIPTION)
        .addExportedPackages(EXPORTED_PACKAGES)
        .addExportedResources(EXPORTED_RESOURCES)
        .setVersion(VERSION)
        .setMinMuleVersion(MIN_MULE_VERSION)
        .withDescriber()
        .setId(DESCRIBER_ID)
        .addProperty(DESCRIBER_PROPERTY, DESCRIBER_PROPERTY_VALUE);

    return builder;
  }

  /**
   * Validates that the two lists have the same size and that
   * all of the items in the {@code expected} {@link List} are
   * present in the {@code actual} one
   *
   * @param expected a reference {@link List}
   * @param actual   an actual {@link List} to test
   */
  public static void assertStringList(List<String> expected, List<String> actual) {
    assertThat(actual, hasSize(expected.size()));
    assertThat(actual, hasItems(expected.toArray(new String[expected.size()])));
  }
}
