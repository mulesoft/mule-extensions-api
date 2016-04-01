/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A specialization of {@link AbstractComplexModel} which also implements
 * {@link HasConnectionProviderModels}, {@link HasSourceModels} and
 * {@link HasOperationModels}
 *
 * @since 1.0
 */
abstract class AbstractComplexModel extends AbstractImmutableModel implements HasConnectionProviderModels, HasSourceModels, HasOperationModels
{

    private final Map<String, OperationModel> operations;
    private final Map<String, ConnectionProviderModel> connectionProviders;
    private final Map<String, SourceModel> messageSources;

    public AbstractComplexModel(String name,
                                String description,
                                List<OperationModel> operationModels,
                                List<ConnectionProviderModel> connectionProviders,
                                List<SourceModel> sourceModels,
                                Set<ModelProperty> modelProperties)
    {
        super(name, description, modelProperties);
        this.operations = toMap(operationModels);
        this.connectionProviders = toMap(connectionProviders);
        this.messageSources = toMap(sourceModels);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OperationModel> getOperationModels()
    {
        return toList(operations.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SourceModel> getSourceModels()
    {
        return toList(messageSources.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<SourceModel> getSourceModel(String name)
    {
        return findModel(messageSources, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ConnectionProviderModel> getConnectionProviderModel(String name)
    {
        return findModel(connectionProviders, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<OperationModel> getOperationModel(String name)
    {
        return findModel(operations, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ConnectionProviderModel> getConnectionProviders()
    {
        return toList(connectionProviders.values());
    }

    protected <T extends EnrichableModel> Optional<T> findModel(Map<String, T> map, String name)
    {
        return Optional.ofNullable(map.get(name));
    }

    protected <T extends Described> List<T> toList(Collection<T> collection)
    {
        if (collection == null || collection.isEmpty())
        {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<>(collection));
    }

    protected <T extends Described> Map<String, T> toMap(List<T> objects)
    {
        if (objects == null || objects.isEmpty())
        {
            return Collections.emptyMap();
        }

        Map<String, T> map = new LinkedHashMap<>(objects.size());
        for (T object : objects)
        {
            if (map.containsKey(object.getName()))
            {
                throw new IllegalArgumentException(String.format("Multiple entries with the same key[%s]", object.getName()));
            }
            map.put(object.getName(), object);
        }
        return Collections.unmodifiableMap(map);
    }
}
