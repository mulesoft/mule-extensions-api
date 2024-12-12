/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.mimetype;

import static org.mule.runtime.api.metadata.MediaType.BINARY;
import static org.mule.runtime.api.metadata.MediaType.HTML;
import static org.mule.runtime.api.metadata.MediaType.JSON;
import static org.mule.runtime.api.metadata.MediaType.TEXT;
import static org.mule.runtime.api.metadata.MediaType.XML;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.mule.runtime.api.metadata.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.junit.Test;

import io.qameta.allure.Description;

public class FileExtensionContentTypeResolutionTestCase {

  private static final MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
  public static final MediaType DEFAULT_CONTENT_TYPE = BINARY;

  /**
   * Ensures that some of the defined mimeTypes are properly mapped. This test does not cover all the available mimeType mappings
   * as that will require to parse the mime.types file.
   * <p>
   * This is also testing that the mime types config can be read from another module
   * ({@code org.mule.runtime.extensions.mimeTypes}).
   */
  @Test
  @Description(useJavaDoc = true)
  public void resolvesFileMimeType() throws Exception {
    Map<String, MediaType> mimeTypes = new HashMap<>();
    mimeTypes.put("txt", TEXT);
    mimeTypes.put("json", JSON);
    mimeTypes.put("xml", XML);
    mimeTypes.put("html", HTML);
    mimeTypes.put("csv", MediaType.create("text", "csv"));

    for (String extension : mimeTypes.keySet()) {
      doFileMimeTypeTest(extension, mimeTypes.get(extension));
    }
  }

  @Test
  public void resolvesDefaultMimeType() throws Exception {
    doFileMimeTypeTest("xxxxxx", DEFAULT_CONTENT_TYPE);
  }

  private void doFileMimeTypeTest(String fileExtension, MediaType expectedMimeType) throws IOException {
    String filename = "test." + fileExtension;
    String mimeType = mimetypesFileTypeMap.getContentType(filename);

    assertThat(mimeType, equalTo(expectedMimeType.toString()));
  }
}
