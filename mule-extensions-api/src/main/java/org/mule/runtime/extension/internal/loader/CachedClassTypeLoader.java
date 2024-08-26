/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader;

import static org.mule.metadata.java.api.utils.ClassUtils.getInnerClassName;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataFormat;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.utils.MetadataTypeUtils;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class CachedClassTypeLoader implements ClassTypeLoader {

  private final ClassTypeLoader classTypeLoader;

  private final Map<String, Optional<MetadataType>> typeIdentifierMetadataTypeMap = new WeakHashMap<>();
  private final Map<Type, MetadataType> typeMetadataTypeMap = new WeakHashMap<>();

  public CachedClassTypeLoader(ClassTypeLoader classTypeLoader) {
    requireNonNull(classTypeLoader, "classTypeLoader cannot be null");

    this.classTypeLoader = classTypeLoader;
  }

  @Override
  public Optional<MetadataType> load(String typeIdentifier) {
    return typeIdentifierMetadataTypeMap.computeIfAbsent(typeIdentifier,
                                                         this::doLoad);
  }

  private Optional<MetadataType> doLoad(String typeIdentifier) {
    if (void.class.getName().equals(typeIdentifier) || Void.class.getName().equals(typeIdentifier)) {
      return of(BaseTypeBuilder.create(MetadataFormat.JAVA).voidType()
          .build());
    }

    Class<?> clazz;
    try {
      clazz = getClassLoader().loadClass(typeIdentifier);
    } catch (ClassNotFoundException e) {
      try {
        clazz =
            getClassLoader().loadClass(getInnerClassName(typeIdentifier));
      } catch (ClassNotFoundException innerClassNotFound) {
        return empty();
      }
    }

    return of(load(clazz));
  }

  @Override
  public MetadataType load(Type type) {
    return typeMetadataTypeMap.computeIfAbsent(type, k -> {
      final MetadataType metadataType = classTypeLoader.load(k);

      MetadataTypeUtils.getTypeId(metadataType)
          .ifPresent(t -> typeIdentifierMetadataTypeMap.put(t, of(metadataType)));

      return metadataType;
    });
  }

  @Override
  public ClassLoader getClassLoader() {
    return classTypeLoader.getClassLoader();
  }

}
