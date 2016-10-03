/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.property.MetadataKeyIdModelProperty;
import org.mule.runtime.extension.api.introspection.property.MetadataKeyPartModelProperty;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Map;

/**
 * Holds constants for the JSON serialization and desarialization of {@link ExtensionModel}s
 *
 * @since 1.0
 */
final class JsonSerializationConstants {

  static final String METADATA_KEY_ID_MODEL_PROPERTY = "metadataKeyId";
  static final String METADATA_KEY_PART_MODEL_PROPERTY = "metadataKeyPart";

  private JsonSerializationConstants() {}

  private static BiMap<String, Class<? extends ModelProperty>> getMapping() {
    final HashBiMap<String, Class<? extends ModelProperty>> stringClassHashMap = HashBiMap.create();

    stringClassHashMap.put(METADATA_KEY_ID_MODEL_PROPERTY, MetadataKeyIdModelProperty.class);
    stringClassHashMap.put(METADATA_KEY_PART_MODEL_PROPERTY, MetadataKeyPartModelProperty.class);

    return stringClassHashMap;
  }

  /**
   * @return A {@link Map} that links a friendly name with a {@link ModelProperty} class.
   * This is helpful when deserializing a {@link ModelProperty} to be able to create the correct instance type.
   */
  static Map<String, Class<? extends ModelProperty>> getNameClassMapping() {
    return getMapping();
  }

  /**
   * @return A {@link Map} that links a {@link ModelProperty} class with their friendly name.
   * This is helpful when serializing a {@link ModelProperty} to be able to tag it with a more friendly name that
   * the full qualifier name of the class.
   */
  static Map<Class<? extends ModelProperty>, String> getClassNameMapping() {
    return getMapping().inverse();
  }

}
