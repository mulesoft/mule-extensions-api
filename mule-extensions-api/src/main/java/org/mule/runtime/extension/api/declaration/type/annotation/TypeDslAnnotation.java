/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import static java.util.Objects.hash;

import org.mule.metadata.api.annotation.TypeAnnotation;

import java.util.Objects;
import java.util.Optional;

/**
 * {@link TypeAnnotation} storing information defined by {@code TypsDsl} annotations
 *
 * @since 1.0
 */
public class TypeDslAnnotation implements TypeAnnotation {

  public static final String NAME = "typeDsl";

  private final boolean allowInlineDefinition;
  private final boolean allowTopLevelDefinition;
  private final SubstitutionGroup substitutionGroup;
  private final DslBaseType dslBaseType;

  /**
   * Creates a new instance
   *
   * @param allowInlineDefinition   whether the associated element should support inline definition as child element
   * @param allowTopLevelDefinition whether the associated element should support being defined as a top level element
   * @param substitutionGroup       the substitutionGroup that the xml element should have as attribute
   * @param baseType                the dslBaseType that the type should declare as base attribute
   */
  public TypeDslAnnotation(boolean allowInlineDefinition, boolean allowTopLevelDefinition, String substitutionGroup,
                           String baseType) {
    this.allowInlineDefinition = allowInlineDefinition;
    this.allowTopLevelDefinition = allowTopLevelDefinition;
    SubstitutionGroup substitutionGroupObject = new SubstitutionGroup(substitutionGroup);
    this.substitutionGroup = substitutionGroupObject.isDefined() ? substitutionGroupObject : null;
    DslBaseType dslBaseTypeObject = new DslBaseType(baseType);
    this.dslBaseType = dslBaseTypeObject.isDefined() ? dslBaseTypeObject : null;
  }

  /**
   * @return {@code typeDsl}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * @return whether the associated element should support inline definition as child element
   */
  public boolean allowsInlineDefinition() {
    return allowInlineDefinition;
  }

  /**
   * @return whether the associated element should support being defined as a top level element
   */
  public boolean allowsTopLevelDefinition() {
    return allowTopLevelDefinition;
  }

  /**
   * @return any subtitutionGroup the element might extend from or {@code Optional.empty()} if not defined
   */
  public Optional<SubstitutionGroup> getSubstitutionGroup() {
    return Optional.ofNullable(this.substitutionGroup);
  }

  /**
   * @return any dslBaseType defined for the type to extend from or {@code Optional.empty()} it not defined
   */
  public Optional<DslBaseType> getDslBaseType() {
    return Optional.ofNullable(dslBaseType);
  }


  @Override
  public int hashCode() {
    return hash(allowInlineDefinition, allowTopLevelDefinition, substitutionGroup, dslBaseType);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TypeDslAnnotation that = (TypeDslAnnotation) o;
    return allowInlineDefinition == that.allowInlineDefinition && allowTopLevelDefinition == that.allowTopLevelDefinition
        && Objects.equals(substitutionGroup, that.substitutionGroup) && Objects.equals(dslBaseType,
                                                                                       that.dslBaseType);
  }
}
