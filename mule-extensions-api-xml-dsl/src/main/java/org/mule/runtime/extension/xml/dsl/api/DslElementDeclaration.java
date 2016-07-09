/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.api;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.introspection.Named;

import java.util.Map;
import java.util.Optional;

/**
 * Provides a declaration of how a {@link Named Component} is represented in {@code XML}, containing
 * all the required information for the {@code XML} element creation and parsing.
 *
 * @since 1.0
 */
public class DslElementDeclaration
{

    private final String attributeName;
    private final String elementName;
    private final String elementNameSpace;
    private final boolean isWrapped;
    private final boolean supportsChildDeclaration;

    private final Map<MetadataType, DslElementDeclaration> genericsDsl;
    private final Map<String, DslElementDeclaration> childsByName;

    /**
     * Creates a new instance of {@link DslElementDeclaration}
     *
     * @param attributeName            the name of the attribute in the parent element that references this element
     * @param elementName              the name of this xml element
     * @param elementNameSpace         the namespace of this xml element
     * @param isWrapped                {@code false} if the element implements the Component's type as an xml extension,
     *                                 or {@code true} if the element is a wrapper of a ref to the Component's type
     * @param supportsChildDeclaration {@code true} if this element supports to be declared as a child element of its parent
     * @param genericsDsl              the {@link DslElementDeclaration} of this element's type generics, if any is present,
     *                                 that complete the element description of container elements of generic types, like
     *                                 Collections or Maps for which the Dsl declaration is modified depending on the contained type.
     * @param childsByName             the {@link DslElementDeclaration} of this element's named childs. For complex types
     *                                 with fields that are mapped as child elements of this element, the Dsl varies
     *                                 depending on each fields definition, associating each field's child element to this
     *                                 parent element.
     */
    public DslElementDeclaration(String attributeName, String elementName, String elementNameSpace, boolean isWrapped,
                                 boolean supportsChildDeclaration,
                                 Map<MetadataType, DslElementDeclaration> genericsDsl,
                                 Map<String, DslElementDeclaration> childsByName)
    {
        this.attributeName = attributeName;
        this.elementName = elementName;
        this.elementNameSpace = elementNameSpace;
        this.isWrapped = isWrapped;
        this.supportsChildDeclaration = supportsChildDeclaration;
        this.genericsDsl = genericsDsl;
        this.childsByName = childsByName;
    }

    /***
     * @return the name of this xml element
     */
    public String getElementName()
    {
        return elementName;
    }

    /**
     * @return the namespace of this xml element
     */
    public String getElementNamespace()
    {
        return elementNameSpace;
    }

    /**
     * @return {@code false} if the element implements the Component's type as an xml extension,
     * or {@code true} if the element is a wrapper of a ref to the Component's type
     */
    public boolean isWrapped()
    {
        return isWrapped;
    }

    /**
     * @return the name of the attribute in the parent element that references this element
     */
    public String getAttributeName()
    {
        return attributeName;
    }

    /**
     * @return {@code true} if this element supports to be declared as a child element of its parent
     */
    public boolean supportsChildDeclaration()
    {
        return supportsChildDeclaration;
    }

    /**
     * @param type {@link MetadataType} of the generic for which its {@link DslElementDeclaration dsl} is required
     * @return the {@link DslElementDeclaration dsl} for the given generic's type if one is present
     */
    public Optional<DslElementDeclaration> getGeneric(MetadataType type)
    {
        return Optional.ofNullable(genericsDsl.get(type));
    }

    /**
     * @param name name of the child element for which its {@link DslElementDeclaration dsl} is required
     * @return the {@link DslElementDeclaration dsl} of the child if one is present
     */
    public Optional<DslElementDeclaration> getChild(String name)
    {
        return Optional.ofNullable(childsByName.get(name));
    }

}
