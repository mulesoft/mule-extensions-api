/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.model;

import static com.google.common.collect.ImmutableList.copyOf;
import org.mule.runtime.api.dsl.model.ApplicationElementIdentifier;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * //TODO
 */
public class DslElementModel<T> {

  private final T model;
  private final DslElementSyntax dsl;
  private final Set<DslElementModel> innerElements;
  private final ApplicationElementIdentifier identifier;

  private DslElementModel(T model, DslElementSyntax dsl, Set<DslElementModel> innerElements) {
    this.dsl = dsl;
    this.model = model;
    this.innerElements = innerElements;
    this.identifier = createIdentifier();
  }

  public T getModel() {
    return model;
  }

  public DslElementSyntax getDsl() {
    return dsl;
  }

  public List<DslElementModel> getInnerElements() {
    return copyOf(innerElements);
  }

  public <E> Optional<DslElementModel<E>> getElement(ApplicationElementIdentifier identifier) {
    if (this.identifier != null && this.identifier.equals(identifier)) {
      return Optional.of((DslElementModel<E>) this);
    }

    return find(e -> e.getElement(identifier));
  }

  public <E> Optional<DslElementModel<E>> getElement(String parameterName) {
    if (dsl.getAttributeName().equals(parameterName)) {
      return Optional.of((DslElementModel<E>) this);
    }

    return find(e -> e.getElement(parameterName));
  }

  public Optional<ApplicationElementIdentifier> getIdentifier() {
    return Optional.ofNullable(identifier);
  }

  private ApplicationElementIdentifier createIdentifier() {
    if (!dsl.supportsTopLevelDeclaration() && !dsl.supportsChildDeclaration()) {
      return null;
    }

    return ApplicationElementIdentifier.Builder.getInstance()
        .withName(dsl.getElementName())
        .withNamespace(dsl.getNamespaceUri())
        .build();
  }

  private <E> Optional<DslElementModel<E>> find(Function<DslElementModel, Optional<DslElementModel>> finder) {
    return innerElements.stream()
        .map(finder::apply)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(e -> (DslElementModel<E>) e)
        .findFirst();
  }

  public static final class Builder<M> {

    private M model;
    private DslElementSyntax dsl;
    private Set<DslElementModel> innerElements;

    private Builder() {
      this.innerElements = new LinkedHashSet<>();
    }

    public static <M> Builder<M> getInstance() {
      return new Builder<>();
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

    public DslElementModel<M> build() {
      return new DslElementModel<>(model, dsl, innerElements);
    }

  }

}
