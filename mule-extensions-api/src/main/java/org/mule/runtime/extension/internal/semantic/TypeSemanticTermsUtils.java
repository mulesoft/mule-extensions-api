/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.semantic;

import static org.mule.runtime.connectivity.internal.platform.schema.SemanticTermsHelper.getParameterTermsFromAnnotations;

import org.mule.metadata.api.builder.WithAnnotation;
import org.mule.runtime.connectivity.internal.platform.schema.SemanticTermsHelper;
import org.mule.runtime.extension.api.declaration.type.annotation.SemanticTermsTypeAnnotation;

import java.lang.reflect.AnnotatedElement;
import java.util.Set;

/**
 * Utility class for handling semantic terms declared at a type level
 *
 * @since 1.4.0
 */
public final class TypeSemanticTermsUtils {

  /**
   * Introspects the given {@code element} for semantic annotations (by the rules of
   * {@link SemanticTermsHelper#getParameterTermsFromAnnotations(java.util.function.Function)}).
   *
   * If terms are found, the {@code annotatedBuilder} is enriched with a {@link SemanticTermsTypeAnnotation} carrying the found
   * terms. no
   * 
   * @param element          an {@link AnnotatedElement}
   * @param annotatedBuilder a {@link WithAnnotation}
   */
  public static void enrichWithTypeAnnotation(AnnotatedElement element, final WithAnnotation annotatedBuilder) {
    Set<String> terms = getParameterTermsFromAnnotations(element::isAnnotationPresent);
    if (!terms.isEmpty()) {
      annotatedBuilder.with(new SemanticTermsTypeAnnotation(terms));
    }
  }

  private TypeSemanticTermsUtils() {}
}
