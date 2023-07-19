/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.hash;

import org.mule.metadata.api.annotation.TypeAnnotation;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A public {@link TypeAnnotation} that holds the semantic terms associated to annotated type.
 * <p>
 * A semantic terms describe the type's meaning and effect
 *
 * @since 1.4.0
 */
public final class SemanticTermsTypeAnnotation implements TypeAnnotation {

  private static final String NAME = "semanticTerms";
  private final Set<String> semanticTerms;

  public SemanticTermsTypeAnnotation(Set<String> semanticTerms) {
    this.semanticTerms = semanticTerms != null ? unmodifiableSet(new LinkedHashSet<>(semanticTerms)) : emptySet();
  }

  public Set<String> getSemanticTerms() {
    return semanticTerms;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean isPublic() {
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SemanticTermsTypeAnnotation) {
      return Objects.equals(semanticTerms, ((SemanticTermsTypeAnnotation) obj).semanticTerms);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return hash(semanticTerms);
  }
}
