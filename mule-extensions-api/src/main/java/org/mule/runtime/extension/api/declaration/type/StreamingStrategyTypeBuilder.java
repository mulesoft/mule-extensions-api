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
import static org.mule.runtime.extension.api.ExtensionConstants.DEFAULT_STREAMING_BUFFER_DATA_UNIT;
import static org.mule.runtime.extension.api.ExtensionConstants.DEFAULT_STREAMING_BUFFER_SIZE;
import static org.mule.runtime.extension.api.ExtensionConstants.DEFAULT_STREAMING_MAX_BUFFER_SIZE;

import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.util.DataUnit;
import org.mule.runtime.extension.api.declaration.type.annotation.InfrastructureTypeAnnotation;

/**
 * Creates instances of {@link MetadataType} which represent a streaming strategy type
 *
 * @since 1.0
 */
public final class StreamingStrategyTypeBuilder extends InfrastructureTypeBuilder {

  public static final String REPEATABLE_FILE_STORE_STREAM_ALIAS = "repeatable-file-store-stream";
  public static final String REPEATABLE_IN_MEMORY_STREAM_ALIAS = "repeatable-in-memory-stream";
  public static final String IN_MEMORY_STREAM_ALIAS = "in-memory-stream";

  /**
   * @return a {@link MetadataType} representation of a retry policy
   */
  public MetadataType buildStreamingStrategyType() {
    BaseTypeBuilder typeBuilder = create(JAVA);

    return create(JAVA).unionType()
        .of(getInMemoryStrategy(typeBuilder))
        .of(getNoStreamingStrategy(typeBuilder))
        .of(getFileStoreStrategy(typeBuilder))
        .id(Object.class.getName())
        .with(new TypeAliasAnnotation("StreamingStrategy"))
        .with(new InfrastructureTypeAnnotation())
        .build();
  }

  private MetadataType getFileStoreStrategy(BaseTypeBuilder typeBuilder) {
    ObjectTypeBuilder streamingType = typeBuilder.objectType()
        .id(Object.class.getName())
        .with(new TypeAliasAnnotation(REPEATABLE_FILE_STORE_STREAM_ALIAS))
        .with(new InfrastructureTypeAnnotation());

    addIntField(streamingType, typeBuilder, "maxInMemorySize",
                "Defines the maximum memory that the stream should use to keep data in memory. If more than that is consumed then "
                    +
                    "it will start to buffer the content on disk.",
                DEFAULT_STREAMING_MAX_BUFFER_SIZE);

    addDataUnitField(typeBuilder, streamingType, "The unit in which maxInMemorySize is expressed");

    return streamingType.build();
  }

  private MetadataType getInMemoryStrategy(BaseTypeBuilder typeBuilder) {
    ObjectTypeBuilder streamingType = typeBuilder.objectType()
        .id(Object.class.getName())
        .with(new TypeAliasAnnotation(REPEATABLE_IN_MEMORY_STREAM_ALIAS))
        .with(new InfrastructureTypeAnnotation());

    addIntField(streamingType, typeBuilder, "initialBufferSize",
                "This is the amount of memory that will be allocated in order to consume the stream and provide random "
                    + "access to it. If the stream contains more data than can be fit into this buffer, then it will be expanded "
                    + "by according to the bufferSizeIncrement attribute, with an upper limit of maxInMemorySize.",
                DEFAULT_STREAMING_BUFFER_SIZE);

    addIntField(streamingType, typeBuilder, "bufferSizeIncrement",
                "This is by how much will be buffer size by expanded if it exceeds its initial size. Setting a value of zero or "
                    + "lower will mean that the buffer should not expand, meaning that a STREAM_MAXIMUM_SIZE_EXCEEDED error will be raised "
                    + "when the buffer gets full.",
                DEFAULT_STREAMING_BUFFER_SIZE);

    addIntField(streamingType, typeBuilder, "maxInMemorySize",
                "This is the maximum amount of memory that will be used. If more than that is used then a STREAM_MAXIMUM_SIZE_EXCEEDED error will be raised. "
                    + "A value lower or equal to zero means no limit.",
                0);


    addDataUnitField(typeBuilder, streamingType, "The unit in which all these attributes are expressed");

    return streamingType.build();
  }

  private void addDataUnitField(BaseTypeBuilder typeBuilder, ObjectTypeBuilder streamingType, String description) {
    addEnumField(streamingType, typeBuilder, "bufferUnit", description,
                 DEFAULT_STREAMING_BUFFER_DATA_UNIT.name(),
                 stream(DataUnit.values()).map(unit -> unit.name()).toArray(String[]::new));
  }

  private MetadataType getNoStreamingStrategy(BaseTypeBuilder typeBuilder) {
    return typeBuilder.objectType()
        .id(Object.class.getName())
        .with(new TypeAliasAnnotation(IN_MEMORY_STREAM_ALIAS))
        .with(new InfrastructureTypeAnnotation())
        .description("This configuration allows the input stream to be read only once. It will not allow to seek randomly " +
            "which will limit the transformations that DW can perform on this stream. " +
            "Use this option for use cases which just require moving data around from one system to another to get optimum performance.")
        .build();
  }
}
