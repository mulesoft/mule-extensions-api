/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader;

import static java.util.Objects.requireNonNull;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

class CachedClassTypeLoader implements ClassTypeLoader {

  private final ClassTypeLoader classTypeLoader;

  private final Map<Type, MetadataType> typeMetadataTypeMap = new WeakHashMap<>();
  private final Map<String, Optional<MetadataType>> typeIdentifierMetadataTypeMap = new WeakHashMap<>();

  public CachedClassTypeLoader(ClassTypeLoader classTypeLoader) {
    requireNonNull(classTypeLoader, "classTypeLoader cannot be null");

    this.classTypeLoader = classTypeLoader;
  }

  @Override
  public MetadataType load(Type type) {
    return typeMetadataTypeMap.computeIfAbsent(type, k -> classTypeLoader.load(type));
  }

  @Override
  public ClassLoader getClassLoader() {
    return classTypeLoader.getClassLoader();
  }

  @Override
  public Optional<MetadataType> load(String typeIdentifier) {
    return typeIdentifierMetadataTypeMap.computeIfAbsent(typeIdentifier, k -> classTypeLoader.load(k));
  }

}

