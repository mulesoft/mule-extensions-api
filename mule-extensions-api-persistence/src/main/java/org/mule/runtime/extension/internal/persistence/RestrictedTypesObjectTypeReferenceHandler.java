/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import static org.mule.metadata.api.utils.MetadataTypeUtils.getTypeId;
import org.mule.metadata.api.annotation.TypeIdAnnotation;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.persistence.DefaultObjectTypeReferenceHandler;
import org.mule.metadata.persistence.ObjectTypeReferenceHandler;
import org.mule.metadata.persistence.SerializationContext;
import org.mule.runtime.api.meta.model.ExtensionModel;

import com.google.gson.stream.JsonWriter;

import java.util.Optional;
import java.util.Set;

/**
 * Implementation of {@link ObjectTypeReferenceHandler} which only uses references for the {@link ObjectType} present in the
 * {@link ExtensionModel} catalogue types obtained through {@link ExtensionModel#getTypes()} and {@link ExtensionModel#getImportedTypes()}.
 *
 * @since 1.0
 */
public final class RestrictedTypesObjectTypeReferenceHandler implements ObjectTypeReferenceHandler {

  private final ObjectTypeReferenceHandler delegateReferenceHandler;
  private final Set<String> allowReferenceTypes;

  /**
   * Creates a new instance
   * 
   * @param serializationContext a {@link SerializationContext} to keep track of the references
   * @param allowReferenceTypes {@link TypeIdAnnotation#getValue()} of the {@link ObjectType} that can use references
   */
  public RestrictedTypesObjectTypeReferenceHandler(SerializationContext serializationContext, Set<String> allowReferenceTypes) {
    this.delegateReferenceHandler = new DefaultObjectTypeReferenceHandler(serializationContext);
    this.allowReferenceTypes = allowReferenceTypes;
  }

  /**
   * See {@link DefaultObjectTypeReferenceHandler#readReference(String)}
   */
  @Override
  public Optional<TypeBuilder> readReference(String typeReference) {
    return delegateReferenceHandler.readReference(typeReference);
  }

  /**
   * If the {@code type} allows references, it is resolved through
   * {@link DefaultObjectTypeReferenceHandler#writeReference(ObjectType, JsonWriter)}. Returns {@link Optional#empty()} otherwise.
   */
  @Override
  public Optional<String> writeReference(ObjectType type, JsonWriter writer) {
    return getTypeId(type).filter(allowReferenceTypes::contains)
        .flatMap(s -> delegateReferenceHandler.writeReference(type, writer));
  }

}
