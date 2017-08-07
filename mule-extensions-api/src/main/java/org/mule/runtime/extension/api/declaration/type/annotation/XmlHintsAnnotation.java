/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import org.mule.metadata.api.annotation.TypeAnnotation;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

/**
 * Contains directives regarding syntax and semantics of the generated XML DSL
 *
 * @since 1.0
 */
public class XmlHintsAnnotation implements TypeAnnotation {

  public static final String NAME = "xmlHints";

  private final boolean allowInlineDefinition;
  private final boolean allowTopLevelDefinition;
  private final boolean allowReferences;
  private final SubstitutionGroup substitutionGroup;
  private final String baseType;

  /**
   * Creates a new instance
   *
   * @param allowInlineDefinition   whether the associated element should support inline definition as child element
   * @param allowTopLevelDefinition whether the associated element should support being defined as a top level element
   * @param allowReferences         whether the associated element should support registry references
   * @param substitutionGroup       the substitutionGroup that the xml element should have as attribute
   */
  public XmlHintsAnnotation(boolean allowInlineDefinition, boolean allowTopLevelDefinition, boolean allowReferences,
                            String substitutionGroup, String baseType) {
    this.allowInlineDefinition = allowInlineDefinition;
    this.allowTopLevelDefinition = allowTopLevelDefinition;
    this.allowReferences = allowReferences;
    SubstitutionGroup substitutionGroupObject = new SubstitutionGroup(substitutionGroup);
    this.substitutionGroup = substitutionGroupObject.isDefined() ? substitutionGroupObject : null;
    this.baseType = baseType;
  }

  /**
   * @return {@code xmlHints}
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
   * @return whether the associated element should support registry references
   */
  public boolean allowsReferences() {
    return allowReferences;
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
   * @return any baseType defined for the type to extend from
   */
  public Optional<String> getBaseType() {
    return StringUtils.isEmpty(baseType) ? Optional.empty() : Optional.of(baseType);
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof XmlHintsAnnotation) {
      XmlHintsAnnotation other = (XmlHintsAnnotation) obj;
      return allowInlineDefinition == other.allowsInlineDefinition() &&
          allowTopLevelDefinition == other.allowsTopLevelDefinition() &&
          allowReferences == other.allowsReferences()
          && Optional.ofNullable(substitutionGroup) == other.getSubstitutionGroup() && Optional.ofNullable(baseType) == other.getBaseType();
    }

    return false;
  }
}
