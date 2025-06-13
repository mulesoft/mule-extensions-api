/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader.parser;

/**
 * Parses the syntactical definition of a media type.
 *
 * @since 1.10.0
 */
public interface MediaTypeParser {

  String getMimeType();

  boolean isStrict();
}
