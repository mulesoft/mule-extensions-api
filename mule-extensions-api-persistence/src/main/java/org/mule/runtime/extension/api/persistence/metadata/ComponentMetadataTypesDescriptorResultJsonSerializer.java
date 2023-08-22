/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata;

import org.mule.runtime.api.metadata.descriptor.ComponentMetadataTypesDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.runtime.extension.internal.persistence.ComponentMetadataTypesDescriptorResult;

import com.google.gson.reflect.TypeToken;

/**
 * Serializer that can convert a {@link MetadataResult} of a {@link ComponentMetadataTypesDescriptor} type into a readable and
 * processable JSON representation and from a JSON {@link String} to an {@link MetadataResult} instance
 *
 * @since 1.4
 */
public class ComponentMetadataTypesDescriptorResultJsonSerializer
    extends AbstractMetadataResultJsonSerializer<ComponentMetadataTypesDescriptor> {

  public ComponentMetadataTypesDescriptorResultJsonSerializer() {
    super(false);
  }

  public ComponentMetadataTypesDescriptorResultJsonSerializer(boolean prettyPrint) {
    this(prettyPrint, false);
  }

  public ComponentMetadataTypesDescriptorResultJsonSerializer(boolean prettyPrint, boolean reduced) {
    super(prettyPrint, reduced);
  }

  @Override
  public String serialize(MetadataResult<ComponentMetadataTypesDescriptor> metadataResult) {
    return gson.toJson((new ComponentMetadataTypesDescriptorResult(metadataResult)));
  }

  @Override
  public MetadataResult<ComponentMetadataTypesDescriptor> deserialize(String metadataResult) {
    ComponentMetadataTypesDescriptorResult result =
        gson.fromJson(metadataResult, new TypeToken<ComponentMetadataTypesDescriptorResult>() {}.getType());
    return result.toDescriptor();
  }
}
