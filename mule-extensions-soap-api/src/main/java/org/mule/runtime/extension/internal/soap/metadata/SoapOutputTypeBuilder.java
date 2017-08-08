/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.soap.metadata;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.NullType;

/**
 * Helper class that builds the output types retrieved by Soap Connect extensions and WSC.
 *
 * @since 1.0
 */
public class SoapOutputTypeBuilder {

  public final static String BODY_FIELD = "body";
  public final static String HEADERS_FIELD = "headers";
  public final static String ATTACHMENTS_FIELD = "attachments";

  public static MetadataType buildOutputType(MetadataType body, MetadataType headers,
                                             MetadataType attachments, BaseTypeBuilder builder) {

    if (isNullType(body) && isNullType(attachments) && isNullType(headers)) {
      return builder.nullType().build();
    }

    ObjectTypeBuilder object = builder.objectType();
    if (!isNullType(body)) {
      object.addField().key(BODY_FIELD).value(body);
    }
    if (!isNullType(headers)) {
      object.addField().key(HEADERS_FIELD).value(headers);
    }
    if (!isNullType(attachments)) {
      object.addField().key(ATTACHMENTS_FIELD).value().arrayType().of(builder.binaryType());
    }
    return object.build();
  }

  private static boolean isNullType(MetadataType type) {
    return type instanceof NullType;
  }
}
