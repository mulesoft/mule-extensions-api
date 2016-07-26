/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.property;


import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.metadata.api.model.MetadataType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * An immutable model property which specifies the relations of a given
 * {@link MetadataType} and its declared subTypes, which are concrete implementations
 * of the base {@link Class}
 *
 * @since 1.0
 */
public final class SubTypesModelProperty implements ModelProperty
{

    private final Map<MetadataType, List<MetadataType>> typesMap;

    /**
     * Creates a new instance containing all the baseType-subTypes declarations
     * based on the {@link Class} references of each type
     *
     * @param mapping concrete implementations of the {@code baseType}
     */
    public SubTypesModelProperty(Map<MetadataType, List<MetadataType>> mapping)
    {
        this.typesMap = mapping;
    }

    /**
     * @return a {@link Map} containing the {@link List} of {@link MetadataType} subTypes for
     * the key base type
     */
    public Map<MetadataType, List<MetadataType>> getSubTypesMapping()
    {
        return Collections.unmodifiableMap(typesMap);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code subTypesMapping}
     */
    @Override
    public String getName()
    {
        return "subTypesMapping";
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code false}
     */
    @Override
    public boolean isExternalizable()
    {
        return true;
    }
}
