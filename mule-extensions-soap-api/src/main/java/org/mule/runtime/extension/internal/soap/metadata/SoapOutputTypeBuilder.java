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
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.runtime.api.util.Reference;

/**
 * Helper class that builds the output types retrieved by Soap Connect extensions and WSC.
 *
 * @since 1.0
 */
public class SoapOutputTypeBuilder {

  public final static String BODY_FIELD = "body";
  public final static String HEADERS_FIELD = "headers";
  public final static String ATTACHMENTS_FIELD = "attachments";

  public static MetadataType buildOutputType(MetadataType body, MetadataType attachments, BaseTypeBuilder builder) {
    Reference<MetadataType> result = new Reference<>();
    attachments.accept(new MetadataTypeVisitor() {

      @Override
      protected void defaultVisit(MetadataType metadataType) {
        ObjectTypeBuilder object = builder.objectType();
        object.addField().key(BODY_FIELD).value(body);
        object.addField().key(ATTACHMENTS_FIELD).value().arrayType().of(builder.anyType());
        result.set(object.build());
      }

      @Override
      public void visitNull(NullType nullType) {
        result.set(body);
      }
    });
    return result.get();
  }

  public static MetadataType buildOutputAttributesType(MetadataType soapHeaders, BaseTypeBuilder builder) {
    ObjectTypeBuilder attributes = builder.objectType();
    attributes.addField().key(HEADERS_FIELD).value(soapHeaders);
    ObjectTypeBuilder protocolHeaders = attributes.addField().key("protocolHeaders").value().objectType();
    protocolHeaders.openWith().stringType();
    return attributes.build();
  }

}
