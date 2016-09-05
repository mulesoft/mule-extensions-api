/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.api.property;

import org.mule.runtime.extension.api.annotation.dsl.xml.XmlHints;
import org.mule.runtime.extension.api.introspection.ModelProperty;

/**
 * Contains directives regarding syntax and semantics of the generated XML DSL
 *
 * @since 1.0
 */
public class XmlHintsModelProperty implements ModelProperty {

  private final boolean allowInlineDefinition;
  private final boolean allowTopLevelDefinition;
  private final boolean allowReferences;

  /**
   * Creates a new instance
   *
   * @param styleAnnotation a {@link XmlHints}
   */
  public XmlHintsModelProperty(XmlHints styleAnnotation) {
    this(styleAnnotation.allowInlineDefinition(), styleAnnotation.allowTopLevelDefinition(), styleAnnotation.allowReferences());
  }

  /**
   * Creates a new instance
   *
   * @param allowInlineDefinition   whether the associated element should support inline definition as child element
   * @param allowTopLevelDefinition whether the associated element should support being defined as a top level element
   * @param allowReferences         whether the associated element should support registry references
   */
  public XmlHintsModelProperty(boolean allowInlineDefinition, boolean allowTopLevelDefinition, boolean allowReferences) {
    this.allowInlineDefinition = allowInlineDefinition;
    this.allowTopLevelDefinition = allowTopLevelDefinition;
    this.allowReferences = allowReferences;
  }

  /**
   * @return {@code xmlHints}
   */
  @Override
  public String getName() {
    return "xmlHints";
  }

  /**
   * @return {@code false}
   */
  @Override
  public boolean isExternalizable() {
    return false;
  }

  /**
   * @return whether the associated element should support inline definition as child element
   */
  public boolean allowsInlineDefinition() {
    return allowInlineDefinition;
  }

  /**
   * @return whether the associated element should support being defined as a top level definition
   */
  public boolean isAllowTopLevelDefinition() {
    return allowTopLevelDefinition;
  }

  /**
   * @return whether the associated element should support registry references
   */
  public boolean allowsReferences() {
    return allowReferences;
  }
}
