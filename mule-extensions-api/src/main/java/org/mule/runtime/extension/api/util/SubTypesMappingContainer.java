/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.ImmutableList.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.mule.metadata.internal.utils.MetadataTypeUtils.getTypeId;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.toSubTypesMap;
import org.mule.metadata.api.annotation.TypeIdAnnotation;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.SubTypesModel;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Immutable container for type mapping, storing the relation of a given type and its declared subtypes
 *
 * @since 1.0
 */
public class SubTypesMappingContainer {

  private final Map<MetadataType, Set<MetadataType>> subTypesMapping;
  private final Map<String, Set<MetadataType>> subTypesById;

  public SubTypesMappingContainer(Collection<SubTypesModel> subTypes) {
    subTypesMapping = toSubTypesMap(subTypes);
    this.subTypesById = subTypesMapping.entrySet().stream()
        .filter(e -> getTypeId(e.getKey()).isPresent())
        .collect(toMap(e -> getTypeId(e.getKey()).get(), Map.Entry::getValue, (k, v) -> k, LinkedHashMap::new));
  }

  /**
   * Returns a {@link List} with all the declared {@link MetadataType} subtypes
   * for the indicated {@link MetadataType} {@code type}.
   * <p>
   * Lookup will be performed first by {@link TypeIdAnnotation typeId},
   * defaulting to {@link MetadataType type} comparison if no {@link TypeIdAnnotation typeId} was found
   *
   * @param type the {@link MetadataType} for which to retrieve its declared subTypes
   * @return a {@link List} with all the declared subtypes for the indicated {@link MetadataType}
   */
  public Collection<MetadataType> getSubTypes(MetadataType type) {
    Collection<MetadataType> subTypes = getTypeId(type).map(subTypesById::get).orElse(subTypesMapping.get(type));
    return subTypes != null ? copyOf(subTypes) : of();
  }

  /**
   * Returns a {@link List} with all the declared {@link MetadataType} that are considered super
   * types from the given {@link MetadataType} {@code type}.
   * <p>
   * The lookup will be performed by looking recursively all the mappings that contains the given
   * {@code type} as subtype and storing the base type and again looking the super type of the
   * found base type.
   *
   * @param type {@link MetadataType} to look for their super types
   * @return a {@link List} with all the declared supertypes for the indicated {@link
   * MetadataType}
   */
  public List<MetadataType> getSuperTypes(MetadataType type) {

    final ImmutableList.Builder<MetadataType> builder = ImmutableList.builder();

    subTypesMapping.entrySet().stream()
        .filter(entry -> entry.getValue().contains(type))
        .forEach(entry -> {
          builder.add(entry.getKey());
          builder.addAll(getSuperTypes(entry.getKey()));
        });

    return builder.build();
  }

  /**
   * Returns a {@link List} with all the declared {@link MetadataType} subtypes
   * for the indicated {@link MetadataType} {@code type}.
   * <p>
   * * Type comparison will be performed first by {@link TypeIdAnnotation typeId} in the context of subTypes mapping.
   * If a {@link TypeIdAnnotation typeId} is available for the given {@code type},
   * the lookup will be performed by {@link TypeIdAnnotation#getValue()} disregarding {@link MetadataType} equality in its
   * full extent, which includes type generics and interfaces implementations, and
   * defaulting to {@link MetadataType#equals} comparison if no {@link TypeIdAnnotation typeId} was found
   *
   * @param type the {@link MetadataType} for which to retrieve its declared subTypes
   * @return <tt>true</tt> if this map contains a mapping for the specified key {@link MetadataType type}
   */
  public boolean containsBaseType(MetadataType type) {
    return getTypeId(type).map(subTypesById::get).orElse(subTypesMapping.get(type)) != null;
  }

  /**
   * @return a {@link List} with all the types which extend another type, in no particular order
   */
  public List<MetadataType> getAllSubTypes() {
    return subTypesMapping.values().stream().flatMap(Collection::stream).collect(toList());
  }

  /**
   * @return a {@link List} with all the types which are extended by another type
   */
  public List<MetadataType> getAllBaseTypes() {
    return ImmutableList.copyOf(subTypesMapping.keySet());
  }

}
