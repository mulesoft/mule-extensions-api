/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model;

import static com.google.common.collect.ImmutableList.copyOf;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;

import org.mule.runtime.api.meta.DescribedObject;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.connection.HasConnectionProviderModels;
import org.mule.runtime.api.meta.model.deprecated.DeprecableModel;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.operation.HasOperationModels;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.source.HasSourceModels;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A specialization of {@link AbstractComplexModel} which also implements {@link HasConnectionProviderModels},
 * {@link HasSourceModels} and {@link HasOperationModels}
 *
 * @since 1.0
 */
public abstract class AbstractComplexModel extends AbstractNamedImmutableModel
    implements HasConnectionProviderModels, HasSourceModels, HasOperationModels, DeprecableModel {

  private final List<OperationModel> operations;
  private final List<ConnectionProviderModel> connectionProviders;
  private final List<SourceModel> messageSources;
  private final DeprecationModel deprecationModel;

  public AbstractComplexModel(String name,
                              String description,
                              List<OperationModel> operationModels,
                              List<ConnectionProviderModel> connectionProviders,
                              List<SourceModel> sourceModels,
                              DisplayModel displayModel,
                              Set<ModelProperty> modelProperties) {
    super(name, description, displayModel, modelProperties);
    this.operations = copy(operationModels);
    this.connectionProviders = copy(connectionProviders);
    this.messageSources = copy(sourceModels);
    this.deprecationModel = null;
  }

  public AbstractComplexModel(String name,
                              String description,
                              List<OperationModel> operationModels,
                              List<ConnectionProviderModel> connectionProviders,
                              List<SourceModel> sourceModels,
                              DisplayModel displayModel,
                              Set<ModelProperty> modelProperties,
                              DeprecationModel deprecationModel) {
    super(name, description, displayModel, modelProperties);
    this.operations = copy(operationModels);
    this.connectionProviders = copy(connectionProviders);
    this.messageSources = copy(sourceModels);
    this.deprecationModel = deprecationModel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<OperationModel> getOperationModels() {
    return operations;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SourceModel> getSourceModels() {
    return messageSources;
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<SourceModel> getSourceModel(String name) {
    return findModel(messageSources, name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<ConnectionProviderModel> getConnectionProviderModel(String name) {
    return findModel(connectionProviders, name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<OperationModel> getOperationModel(String name) {
    return findModel(operations, name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ConnectionProviderModel> getConnectionProviders() {
    return connectionProviders;
  }

  /**
   * Returns the first item in the {@code values} collection which matches the given {@code name}.
   *
   * @param values a {@link Collection} of {@link NamedObject} items
   * @param name   the matching criteria
   * @param <T>    the generic type of the {@code values} items
   * @return an {@link Optional} matching item
   */
  protected <T extends NamedObject> Optional<T> findModel(Collection<T> values, String name) {
    return values.stream().filter(v -> v.getName().equals(name)).findFirst();
  }

  protected <T extends DescribedObject> List<T> toList(Collection<T> collection) {
    if (collection == null || collection.isEmpty()) {
      return emptyList();
    }
    return unmodifiableList(new ArrayList<>(collection));
  }

  /**
   * Returns an immutable copy of the {@code values} collection, validating that no items exist such that its name is repeated
   *
   * @param values     the collection to copy
   * @param identifier human friendly identifier of the {@code values} content
   * @param <T>        the generic type of the {@code values} items
   * @return an immutable copy of the {@code values}
   */
  protected <T extends NamedObject> List<T> unique(Collection<T> values, String identifier) {
    Multiset<String> names = HashMultiset.create();
    values.stream().map(NamedObject::getName).forEach(names::add);

    List<String> invalid = names.entrySet().stream()
        .filter(entry -> entry.getCount() > 1)
        .map(Multiset.Entry::getElement)
        .collect(Collectors.toList());

    if (!invalid.isEmpty()) {
      throw new IllegalModelDefinitionException(format("%s %s were defined multiple times",
                                                       identifier,
                                                       invalid));
    }

    return copyOf(values);
  }

  @Override
  public Optional<DeprecationModel> getDeprecationModel() {
    return ofNullable(deprecationModel);
  }

  @Override
  public boolean isDeprecated() {
    return deprecationModel != null;
  }
}
