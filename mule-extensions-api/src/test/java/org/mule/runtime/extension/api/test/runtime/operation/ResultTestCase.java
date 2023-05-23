/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.runtime.operation;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.api.metadata.MediaType.APPLICATION_JAVA;
import static org.mule.runtime.api.metadata.MediaType.APPLICATION_JSON;

import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.extension.api.runtime.operation.Result;

import org.junit.Test;

public class ResultTestCase {

  @Test
  public void copy() {
    Object output = new Object();
    MediaType mediaType = APPLICATION_JAVA;
    Object attributes = new Object();
    MediaType attributesMediaType = APPLICATION_JSON;
    long length = 1L;
    Result<Object, Object> result = Result.builder()
        .output(output)
        .mediaType(mediaType)
        .attributes(attributes)
        .attributesMediaType(attributesMediaType)
        .length(length)
        .build();

    Result<Object, Object> resultCopy = result.copy().build();

    assertThat(resultCopy.getOutput(), is(output));
    assertThat(resultCopy.getMediaType().get(), is(mediaType));
    assertThat(resultCopy.getAttributes().get(), is(attributes));
    assertThat(resultCopy.getAttributesMediaType().get(), is(attributesMediaType));
    assertThat(resultCopy.getByteLength().getAsLong(), is(length));
  }

}
