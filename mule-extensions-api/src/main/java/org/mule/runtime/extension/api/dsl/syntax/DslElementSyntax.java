/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.syntax;

import static com.google.common.collect.ImmutableList.copyOf;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.NamedObject;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Provides a declaration of how a {@link NamedObject Component} is represented in {@code XML}, containing
 * all the required information for the {@code XML} element creation and parsing.
 *
 * @since 1.0
 */
public class DslElementSyntax {

  private final String attributeName;
  private final String elementName;
  private final String prefix;
  private final String namespace;
  private final boolean isWrapped;
  private final boolean supportsAttributeDeclaration;
  private final boolean supportsChildDeclaration;
  private final boolean supportsTopLevelDeclaration;
  private final boolean requiresConfig;
  private final Map<MetadataType, DslElementSyntax> genericsDsl;
  private final Map<String, DslElementSyntax> childs;
  private final Map<String, DslElementSyntax> attributes;
  private final Map<String, DslElementSyntax> containedElements;

  /**
   * Creates a new instance of {@link DslElementSyntax}
   *
   * @param attributeName                the name of the attribute in the parent element that
   *                                     references this element
   * @param elementName                  the name of this xml element
   * @param prefix                       the prefix of this xml element
   * @param isWrapped                    {@code false} if the element implements the Component's type
   *                                     as an xml extension, or {@code true} if the element is a
   *                                     wrapper of a ref to the Component's type
   * @param supportsAttributeDeclaration {@code true} if this element supports to be declared as an
   *                                     attribute in the parent element
   * @param supportsChildDeclaration     {@code true} if this element supports to be declared as a
   *                                     child element of its parent
   * @param requiresConfig               whether the element requires a parameter pointing to the
   *                                     config
   * @param genericsDsl                  the {@link DslElementSyntax} of this element's type generics,
   *                                     if any is present, that complete the element description of
   *                                     container elements of generic types, like Collections or Maps
   *                                     for which the Dsl declaration is modified depending on the
   *                                     contained type.
   */
  public DslElementSyntax(String attributeName,
                          String elementName,
                          String prefix,
                          String namespace,
                          boolean isWrapped,
                          boolean supportsAttributeDeclaration, boolean supportsChildDeclaration,
                          boolean supportsTopLevelDeclaration,
                          boolean requiresConfig,
                          Map<MetadataType, DslElementSyntax> genericsDsl,
                          Map<String, DslElementSyntax> containedElements) {
    this.attributeName = attributeName;
    this.elementName = elementName;
    this.prefix = prefix;
    this.namespace = namespace;
    this.isWrapped = isWrapped;
    this.supportsAttributeDeclaration = supportsAttributeDeclaration;
    this.supportsChildDeclaration = supportsChildDeclaration;
    this.supportsTopLevelDeclaration = supportsTopLevelDeclaration;
    this.requiresConfig = requiresConfig;
    this.genericsDsl = genericsDsl;

    this.containedElements = containedElements;

    this.childs =
        containedElements.entrySet().stream().filter(e -> e.getValue().supportsChildDeclaration())
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    this.attributes =
        containedElements.entrySet().stream().filter(e -> e.getValue().supportsAttributeDeclaration())
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /***
   * @return the name of this xml element
   */
  public String getElementName() {
    return elementName;
  }

  /**
   * @return the prefix of this xml element
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * @return the namespace of this xml element
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @return {@code false} if the element implements the Component's type as an xml extension, or
   * {@code true} if the element is a wrapper of a ref to the Component's type
   */
  public boolean isWrapped() {
    return isWrapped;
  }

  /**
   * @return the name of the attribute in the parent element that references this element
   */
  public String getAttributeName() {
    return attributeName;
  }

  /**
   * @return {@code true} if this element supports to be declared as an attribute of its parent
   */
  public boolean supportsAttributeDeclaration() {
    return supportsAttributeDeclaration;
  }

  /**
   * @return {@code true} if this element supports to be declared as a child element of its parent
   */
  public boolean supportsChildDeclaration() {
    return supportsChildDeclaration;
  }

  /**
   * @return {@code true} if this element supports to be declared as a top level element
   */
  public boolean supportsTopLevelDeclaration() {
    return supportsTopLevelDeclaration;
  }

  /**
   * @return {@code true} if this element requires having an attribute which points to a config
   */
  public boolean requiresConfig() {
    return requiresConfig;
  }

  /**
   * @param type {@link MetadataType} of the generic for which its {@link DslElementSyntax dsl} is
   *             required
   * @return the {@link DslElementSyntax dsl} for the given generic's type if one is present
   */
  public Optional<DslElementSyntax> getGeneric(MetadataType type) {
    return ofNullable(genericsDsl.get(type));
  }

  public Map<MetadataType, DslElementSyntax> getGenerics() {
    return genericsDsl;
  }

  /**
   * @param name name of the child element for which its {@link DslElementSyntax dsl} is required
   * @return the {@link DslElementSyntax dsl} of the child if one is present
   */
  public Optional<DslElementSyntax> getChild(String name) {
    return ofNullable(childs.get(name));
  }

  /**
   * @return the {@link DslElementSyntax dsl} of the childs of this element
   */
  public List<DslElementSyntax> getChilds() {
    return copyOf(childs.values());
  }

  /**
   * @param name name of the attribute element for which its {@link DslElementSyntax dsl} is required
   * @return the {@link DslElementSyntax dsl} of the attribute if one is present
   */
  public Optional<DslElementSyntax> getAttribute(String name) {
    return ofNullable(attributes.get(name));
  }

  /**
   * @return the {@link DslElementSyntax dsl} of the attributes of this element
   */
  public List<DslElementSyntax> getAttributes() {
    return copyOf(attributes.values());
  }

  /**
   * @param name name of the element for which its {@link DslElementSyntax dsl} is required
   * @return the {@link DslElementSyntax dsl} of the element if one is present
   */
  public Optional<DslElementSyntax> getContainedElement(String name) {
    return ofNullable(containedElements.get(name));
  }

  /**
   * @return the {@link DslElementSyntax dsl} of all the contained elements of this element
   */
  public List<DslElementSyntax> getContainedElements() {
    return copyOf(containedElements.values());
  }

  @Override
  public boolean equals(Object o) {
    return reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(this);
  }

  @Override
  public String toString() {
    return "{ \n" +
        "AttributeName: " + attributeName + ",\n" +
        "ElementName: " + elementName + ",\n" +
        "Prefix: " + prefix + ",\n" +
        "Namespace: " + namespace + ",\n" +
        "SupportsAttributeDeclaration: " + supportsAttributeDeclaration + ",\n" +
        "SupportsChildDeclaration: " + supportsChildDeclaration + ",\n" +
        "SupportsTopLevelDeclaration: " + supportsTopLevelDeclaration + ",\n" +
        "RequiresConfig: " + requiresConfig +
        "}";
  }
}
