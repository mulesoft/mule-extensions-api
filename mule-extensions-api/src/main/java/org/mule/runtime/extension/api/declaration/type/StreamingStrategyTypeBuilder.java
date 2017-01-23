/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static java.util.Arrays.stream;
import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.util.ByteUnit;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeAliasAnnotation;

import java.util.Map;

/**
 * Creates instances of {@link MetadataType} which represent a streaming strategy type
 *
 * @since 1.0
 */
public final class StreamingStrategyTypeBuilder extends InfrastructureTypeBuilder {

  /**
   * @return a {@link MetadataType} representation of a retry policy
   */
  public MetadataType buildStreamingStrategyType() {
    BaseTypeBuilder typeBuilder = create(JAVA);

    return create(JAVA).unionType()
        .of(getFileStoreStrategy(typeBuilder))
        .of(getInMemoryStrategy(typeBuilder))
        .of(getNoStreamingStrategy(typeBuilder))
        .id(Map.class.getName())
        .with(new TypeAliasAnnotation("ReconnectionStrategy"))
        .build();
  }

  private MetadataType getFileStoreStrategy(BaseTypeBuilder typeBuilder) {
    return createBufferedStreamingType("repeatable-file-store-stream",
                                       "Defines the maximum memory that the stream should use to keep data in memory. If more than that is consumed then "
                                           +
                                           "it will start to buffer the content on disk.",
                                       typeBuilder);
  }

  private MetadataType getInMemoryStrategy(BaseTypeBuilder typeBuilder) {
    return createBufferedStreamingType("repeatable-in-memory-stream",
                                       "This is the maximum amount of memory that will be used. If more than that is used then a STREAM_MAXIMUM_SIZE_EXCEEDED error will be raised",
                                       typeBuilder);
  }

  private MetadataType getNoStreamingStrategy(BaseTypeBuilder typeBuilder) {
    return typeBuilder.objectType()
        .id(Map.class.getName())
        .with(new TypeAliasAnnotation("in-memory-stream"))
        .description("This configuration allows the input stream to be read only once. It will not allow to seek randomly " +
            "which will limit the transformations that DW can perform on this stream. " +
            "Use this option for use cases which just require moving data around from one system to another to get optimum performance.")
        .build();
  }

  private MetadataType createBufferedStreamingType(String name, String description, BaseTypeBuilder typeBuilder) {
    ObjectTypeBuilder streamingType = typeBuilder.objectType()
        .id(Map.class.getName())
        .with(new TypeAliasAnnotation(name));

    addIntField(streamingType, typeBuilder, "maxInMemorySize", description, 256);//TODO: Use actual default

    addEnumField(streamingType, typeBuilder, "sizeUnit", "The unit in which maxInMemorySize is expressed",
                 ByteUnit.KB.name(), /// TODO: use actual default
                 stream(ByteUnit.values()).map(unit -> unit.name()).toArray(String[]::new));

    return streamingType.build();
  }

}
