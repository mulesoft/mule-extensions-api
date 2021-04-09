/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.hash;

import org.mule.metadata.api.annotation.TypeAnnotation;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

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
