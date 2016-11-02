/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.runtime.extension.api.declaration.type.DefaultExtensionsTypeLoaderFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

abstract class BasePersistenceTestCase {

  protected ClassTypeLoader typeLoader = new DefaultExtensionsTypeLoaderFactory().createTypeLoader();

  protected String getResourceAsString(String fileName) throws IOException {
    final InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
    return IOUtils.toString(resourceAsStream);
  }

  protected void assertSerializedJson(String serializedResult, String expectedFileName) throws IOException {
    String resource = getResourceAsString(expectedFileName);
    final JsonParser jsonParser = new JsonParser();
    final JsonElement expected = jsonParser.parse(resource);
    final JsonElement result = jsonParser.parse(serializedResult);

    assertThat(result, is(expected));
  }
}
