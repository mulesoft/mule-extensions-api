/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.extension.api.ExtensionConstants.ERROR_MAPPING_DESCRIPTION;
import static org.mule.runtime.extension.api.error.ErrorConstants.ERROR_TYPE_DEFINITION;
import static org.mule.runtime.extension.api.error.ErrorConstants.ERROR_TYPE_MATCHER;

import org.mule.metadata.api.builder.ArrayTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.declaration.type.annotation.InfrastructureTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeDslAnnotation;

/**
 * Creates instances of {@link MetadataType} which represent an error mapping.
 *
 * @since 1.4
 */
public class ErrorMappingsTypeBuilder extends InfrastructureTypeBuilder {

  public MetadataType buildErrorMappingsType() {
    final ObjectTypeBuilder errorMappingType = create(JAVA).objectType().id("errorMapping");
    // 'source' is not required, not setting it means ANY
    errorMappingType.addField()
        .key("source")
        .value(ERROR_TYPE_MATCHER);
    errorMappingType.addField()
        .key("target")
        .required()
        .value(ERROR_TYPE_DEFINITION);

    final ArrayTypeBuilder type = create(JAVA).arrayType()
        .of(errorMappingType)
        .with(new InfrastructureTypeAnnotation())
        .description(ERROR_MAPPING_DESCRIPTION)
        .with(new TypeDslAnnotation(true, false, null, null));

    return type.build();
  }
}
