/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.model;

import static com.google.common.collect.ImmutableList.copyOf;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.dsl.model.ApplicationElement;
import org.mule.runtime.api.dsl.model.ApplicationElementIdentifier;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Provides a declaration of how a given {@code model} of type {@code T} is related to its
 * {@link DslElementSyntax DSL} representation.
 * <p>
 * This {@link DslElementModel} can be related to an {@link ApplicationElement} of a configuration file
 * by using the {@link #findElement} lookup with the required {@link ApplicationElementIdentifier}, and
 * thus providing a way to relate an {@link ApplicationElement} to the {@link ExtensionModel} component
 * or {@link MetadataType} it represents.
 *
 * @since 1.0
 */
public class DslElementModel<T> {

  private final T model;
  private final DslElementSyntax dsl;
  private final Set<DslElementModel> innerElements;
  private final ApplicationElement appElement;
  private final ApplicationElementIdentifier identifier;

  private DslElementModel(T model, DslElementSyntax dsl, Set<DslElementModel> innerElements, ApplicationElement appElement) {
    this.dsl = dsl;
    this.model = model;
    this.innerElements = innerElements;
    this.appElement = appElement;
    this.identifier = createIdentifier();
  }

  /**
   * @return the model associated to {@code this} {@link DslElementModel element}
   */
  public T getModel() {
    return model;
  }

  /**
   * @return the {@link DslElementSyntax} associated to {@code this} {@link DslElementModel element}
   */
  public DslElementSyntax getDsl() {
    return dsl;
  }

  /**
   * @return a {@link List} with all the child {@link DslElementModel elements}
   */
  public List<DslElementModel> getInnerElements() {
    return copyOf(innerElements);
  }

  /**
   * @return the {@link ApplicationElementIdentifier identifier} associated to
   * {@code this} {@link DslElementModel element}, if one was provided.
   */
  public Optional<ApplicationElementIdentifier> getIdentifier() {
    return Optional.ofNullable(identifier);
  }

  /**
   * @return the {@link ApplicationElement} associated to {@code this}
   * {@link DslElementModel element}, if one was provided.
   */
  public Optional<ApplicationElement> getApplicationElement() {
    return Optional.ofNullable(appElement);
  }

  /**
   * Lookup method for finding a given {@link DslElementModel} based on its
   * {@link ApplicationElementIdentifier identifier} from {@code this} element as root.
   * If {@code this} {@link DslElementModel} doesn't match with the given identifier,
   * then a DFS lookup is performed for each of its {@link #getInnerElements inner elements}.
   *
   * @param identifier the {@link ApplicationElementIdentifier} used for matching
   * @return the {@link DslElementModel} associated to the given {@code identifier},
   * if one was found.
   */
  public <E> Optional<DslElementModel<E>> findElement(ApplicationElementIdentifier identifier) {
    if (this.identifier != null && this.identifier.equals(identifier)) {
      return Optional.of((DslElementModel<E>) this);
    }

    return find(e -> e.findElement(identifier));
  }

  /**
   * Lookup method for finding a given {@link DslElementModel} based on its
   * {@code parameterName} from {@code this} element as root.
   * If {@code this} {@link DslElementModel} name doesn't match with the given parameterName,
   * then a DFS lookup is performed for each of its {@link #getInnerElements inner elements}.
   * Since not all the elements may in an application may have an
   * {@link DslElementSyntax::getElementName} this lookup method may produce different results
   * than the lookup by {@link ApplicationElementIdentifier identifier}
   *
   * @param parameterName the {@code parameterName} used for matching
   * @return the {@link DslElementModel} associated to the given {@code identifier},
   * if one was found.
   */
  public <E> Optional<DslElementModel<E>> findElement(String parameterName) {
    if (dsl.getAttributeName().equals(parameterName)) {
      return Optional.of((DslElementModel<E>) this);
    }

    return find(e -> e.findElement(parameterName));
  }

  private ApplicationElementIdentifier createIdentifier() {
    if (appElement != null) {
      return appElement.getIdentifier();
    }

    if (!dsl.supportsTopLevelDeclaration() && !dsl.supportsChildDeclaration()) {
      return null;
    }

    return ApplicationElementIdentifier.builder()
        .withName(dsl.getElementName())
        .withNamespace(dsl.getNamespaceUri())
        .build();
  }

  private <E> Optional<DslElementModel<E>> find(Function<DslElementModel, Optional<DslElementModel>> finder) {
    return innerElements.stream()
        .map(finder)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(e -> (DslElementModel<E>) e)
        .findFirst();
  }

  public static final class Builder<M> {

    private M model;
    private DslElementSyntax dsl;
    private Set<DslElementModel> innerElements;
    private ApplicationElement appElement;

    private Builder() {
      this.innerElements = new LinkedHashSet<>();
    }

    public Builder<M> withModel(M model) {
      this.model = model;
      return this;
    }

    public Builder<M> withDsl(DslElementSyntax dsl) {
      this.dsl = dsl;
      return this;
    }

    public Builder<M> containing(DslElementModel inner) {
      this.innerElements.add(inner);
      return this;
    }

    public Builder<M> withElement(ApplicationElement element) {
      this.appElement = element;
      return this;
    }

    public DslElementModel<M> build() {
      return new DslElementModel<>(model, dsl, innerElements, appElement);
    }

  }

  /**
   * @return a new {@link Builder}
   */
  public static <M> Builder<M> builder() {
    return new Builder<>();
  }

}
