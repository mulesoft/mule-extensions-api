/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.declaration.type;

import static java.util.Arrays.stream;
import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.extension.api.ExtensionConstants.DEFAULT_BYTES_STREAMING_MAX_BUFFER_SIZE;
import static org.mule.runtime.extension.api.ExtensionConstants.DEFAULT_BYTE_STREAMING_BUFFER_DATA_UNIT;
import static org.mule.runtime.extension.api.ExtensionConstants.DEFAULT_BYTE_STREAMING_BUFFER_INCREMENT_SIZE;
import static org.mule.runtime.extension.api.ExtensionConstants.DEFAULT_BYTE_STREAMING_BUFFER_SIZE;
import static org.mule.runtime.extension.api.ExtensionConstants.DEFAULT_OBJECT_STREAMING_BUFFER_INCREMENT_SIZE;
import static org.mule.runtime.extension.api.ExtensionConstants.DEFAULT_OBJECT_STREAMING_BUFFER_SIZE;
import static org.mule.runtime.extension.api.ExtensionConstants.DEFAULT_OBJECT_STREAMING_MAX_BUFFER_SIZE;
import static org.mule.runtime.internal.dsl.DslConstants.EE_NAMESPACE;
import static org.mule.runtime.internal.dsl.DslConstants.EE_PREFIX;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.util.DataUnit;
import org.mule.runtime.extension.api.declaration.type.annotation.InfrastructureTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.QNameTypeAnnotation;

import javax.xml.namespace.QName;

/**
 * Creates instances of {@link MetadataType} which represent a streaming strategy type
 *
 * @since 1.0
 */
public final class StreamingStrategyTypeBuilder extends InfrastructureTypeBuilder {

  private static final String MAX_BUFFER_SIZE = "maxBufferSize";
  private static final String BUFFER_SIZE_INCREMENT = "bufferSizeIncrement";
  private static final String INITIAL_BUFFER_SIZE = "initialBufferSize";

  public static final String REPEATABLE_FILE_STORE_BYTES_STREAM_ALIAS = "repeatable-file-store-stream";
  public static final String REPEATABLE_IN_MEMORY_BYTES_STREAM_ALIAS = "repeatable-in-memory-stream";
  public static final String NON_REPEATABLE_BYTE_STREAM_ALIAS = "non-repeatable-stream";

  public static final String REPEATABLE_IN_MEMORY_OBJECTS_STREAM_ALIAS = "repeatable-in-memory-iterable";
  public static final String REPEATABLE_FILE_STORE_OBJECTS_STREAM_ALIAS = "repeatable-file-store-iterable";
  public static final String NON_REPEATABLE_OBJECTS_STREAM_ALIAS = "non-repeatable-iterable";

  /**
   * @return a {@link MetadataType} representation of a byte streaming strategy
   */
  public MetadataType getByteStreamingStrategyType() {
    BaseTypeBuilder typeBuilder = create(JAVA);

    return create(JAVA).unionType()
        .of(getInMemoryByteStrategy(typeBuilder))
        .of(getFileStoreByteStrategy(typeBuilder))
        .of(getNoStreamingByteStrategy(typeBuilder))
        .id("ByteStreamingStrategy")
        .with(new InfrastructureTypeAnnotation())
        .build();
  }

  /**
   * @return a {@link MetadataType} representation of an object streaming strategy
   */
  public MetadataType getObjectStreamingStrategyType() {
    BaseTypeBuilder typeBuilder = create(JAVA);

    return create(JAVA).unionType()
        .of(getInMemoryObjectStrategy(typeBuilder))
        .of(getFileStoreObjectStrategy(typeBuilder))
        .of(getNoStreamingObjectStrategy(typeBuilder))
        .id("ObjectStreamingStrategy")
        .with(new InfrastructureTypeAnnotation())
        .build();
  }

  private MetadataType getFileStoreByteStrategy(BaseTypeBuilder typeBuilder) {
    ObjectTypeBuilder streamingType = typeBuilder.objectType()
        .id(REPEATABLE_FILE_STORE_BYTES_STREAM_ALIAS)
        .with(new QNameTypeAnnotation(new QName(EE_NAMESPACE, REPEATABLE_FILE_STORE_BYTES_STREAM_ALIAS, EE_PREFIX)))
        .with(new InfrastructureTypeAnnotation());

    addIntField(streamingType, typeBuilder, "inMemorySize",
                "Defines the maximum memory that the stream should use to keep data in memory. If more than that is consumed then "
                    +
                    "it will start to buffer the content on disk.",
                DEFAULT_BYTE_STREAMING_BUFFER_SIZE);

    addDataUnitField(typeBuilder, streamingType, "The unit in which maxInMemorySize is expressed");

    return streamingType.build();
  }

  private MetadataType getFileStoreObjectStrategy(BaseTypeBuilder typeBuilder) {
    ObjectTypeBuilder streamingType = typeBuilder.objectType()
        .id(REPEATABLE_FILE_STORE_OBJECTS_STREAM_ALIAS)
        .with(new QNameTypeAnnotation(new QName(EE_NAMESPACE, REPEATABLE_FILE_STORE_OBJECTS_STREAM_ALIAS, EE_PREFIX)))
        .with(new InfrastructureTypeAnnotation());

    addIntField(streamingType, typeBuilder, "inMemoryObjects",
                "This is the maximum amount of instances that will be kept in memory. If more than that is required, then " +
                    "it will start to buffer the content on disk.",
                DEFAULT_OBJECT_STREAMING_BUFFER_SIZE);

    addDataUnitField(typeBuilder, streamingType, "The unit in which maxInMemorySize is expressed");

    return streamingType.build();
  }

  private MetadataType getInMemoryByteStrategy(BaseTypeBuilder typeBuilder) {
    ObjectTypeBuilder streamingType = typeBuilder.objectType()
        .id(REPEATABLE_IN_MEMORY_BYTES_STREAM_ALIAS)
        .with(new InfrastructureTypeAnnotation());

    addIntField(streamingType, typeBuilder, INITIAL_BUFFER_SIZE,
                "This is the amount of memory that will be allocated in order to consume the stream and provide random "
                    + "access to it. If the stream contains more data than can be fit into this buffer, then it will be expanded "
                    + "by according to the bufferSizeIncrement attribute, with an upper limit of maxInMemorySize.",
                DEFAULT_BYTE_STREAMING_BUFFER_SIZE);

    addIntField(streamingType, typeBuilder, BUFFER_SIZE_INCREMENT,
                "This is by how much will be buffer size by expanded if it exceeds its initial size. Setting a value of zero or "
                    + "lower will mean that the buffer should not expand, meaning that a STREAM_MAXIMUM_SIZE_EXCEEDED error will be raised "
                    + "when the buffer gets full.",
                DEFAULT_BYTE_STREAMING_BUFFER_INCREMENT_SIZE);

    addIntField(streamingType, typeBuilder, MAX_BUFFER_SIZE,
                "This is the maximum amount of memory that will be used. If more than that is used then a STREAM_MAXIMUM_SIZE_EXCEEDED error will be raised. "
                    + "A value lower or equal to zero means no limit.",
                DEFAULT_BYTES_STREAMING_MAX_BUFFER_SIZE);


    addDataUnitField(typeBuilder, streamingType, "The unit in which all these attributes are expressed");

    return streamingType.build();
  }

  private MetadataType getInMemoryObjectStrategy(BaseTypeBuilder typeBuilder) {
    ObjectTypeBuilder streamingType = typeBuilder.objectType()
        .id(REPEATABLE_IN_MEMORY_OBJECTS_STREAM_ALIAS)
        .with(new InfrastructureTypeAnnotation());

    addIntField(streamingType, typeBuilder, INITIAL_BUFFER_SIZE,
                "This is the amount of instances that will be initially be allowed to be kept in memory in order to "
                    + "consume the stream and provide random access to it. If the stream contains more data than can fit "
                    + "into this buffer, then it will be expanded according to the bufferSizeIncrement attribute, with an upper "
                    + "limit of maxInMemorySize. Default value is 100 instances.",
                DEFAULT_OBJECT_STREAMING_BUFFER_SIZE);

    addIntField(streamingType, typeBuilder, "bufferSizeIncrement",
                "This is by how much will the buffer size by expanded if it exceeds its initial size. Setting a value of zero or "
                    + "lower will mean that the buffer should not expand, meaning that a STREAM_MAXIMUM_SIZE_EXCEEDED error will be raised "
                    + "when the buffer gets full. Default value is 100 instances.",
                DEFAULT_OBJECT_STREAMING_BUFFER_INCREMENT_SIZE);

    addIntField(streamingType, typeBuilder, MAX_BUFFER_SIZE,
                "This is the maximum amount of memory that will be used. If more than that is used then a STREAM_MAXIMUM_SIZE_EXCEEDED error will be raised. "
                    + "A value lower or equal to zero means no limit.",
                DEFAULT_OBJECT_STREAMING_MAX_BUFFER_SIZE);

    return streamingType.build();
  }

  private void addDataUnitField(BaseTypeBuilder typeBuilder, ObjectTypeBuilder streamingType, String description) {
    addEnumField(streamingType, typeBuilder, "bufferUnit", description,
                 DEFAULT_BYTE_STREAMING_BUFFER_DATA_UNIT.name(),
                 stream(DataUnit.values()).map(unit -> unit.name()).toArray(String[]::new));
  }

  private MetadataType getNoStreamingByteStrategy(BaseTypeBuilder typeBuilder) {
    return typeBuilder.objectType()
        .id(NON_REPEATABLE_BYTE_STREAM_ALIAS)
        .with(new InfrastructureTypeAnnotation())
        .description("This configuration allows the input stream to be read only once. It will not allow to seek randomly " +
            "which will limit the transformations that DW can perform on this stream. " +
            "Use this option for use cases which just require moving data around from one system to another to get optimum performance.")
        .build();
  }

  private MetadataType getNoStreamingObjectStrategy(BaseTypeBuilder typeBuilder) {
    return typeBuilder.objectType()
        .id(NON_REPEATABLE_OBJECTS_STREAM_ALIAS)
        .with(new InfrastructureTypeAnnotation())
        .description("This configuration allows the stream to be read only once. It will not allow to seek randomly " +
            "which will limit the transformations that DW can perform on this stream. " +
            "Use this option for use cases which just require moving data around from one system to another to get optimum performance.")
        .build();
  }
}
