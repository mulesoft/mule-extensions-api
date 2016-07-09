/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.xml.dsl.internal;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.xml.dsl.api.DslElementDeclaration;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the builder design pattern to create instances of {@link DslElementDeclaration}
 *
 * @since 1.0
 */
public final class DslElementDeclarationBuilder
{

    private String attributeName;
    private String elementName;
    private String elementNameSpace;
    private boolean isWrapped = false;
    private boolean supportsChildDeclaration = false;
    private Map<MetadataType, DslElementDeclaration> genericChilds = new HashMap<>();
    private Map<String, DslElementDeclaration> namedChilds = new HashMap<>();


    private DslElementDeclarationBuilder()
    {
        attributeName = "";
        elementName = "";
        elementNameSpace = "";
    }

    /**
     * @return a new instance of {@link DslElementDeclarationBuilder}
     */
    public static DslElementDeclarationBuilder create()
    {
        return new DslElementDeclarationBuilder();
    }

    /**
     * Adds a {@code name} that describes how this element will be represented as an attribute.
     *
     * @return the builder instance enriched with the {@code attributeName}
     */
    public DslElementDeclarationBuilder withAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
        return this;
    }

    /**
     * Adds a {@code name} to the element being declared
     *
     * @return the builder instance enriched with the {@code elementName}
     */
    public DslElementDeclarationBuilder withElementName(String elementName)
    {
        this.elementName = elementName;
        return this;
    }

    /**
     * Adds a {@code namespace} to the element being declared
     *
     * @return the builder instance enriched with the {@code namespace}
     */
    public DslElementDeclarationBuilder withNamespace(String namespace)
    {
        this.elementNameSpace = namespace;
        return this;
    }

    /**
     * Declares whether or not {@code this} {@link DslElementDeclaration} is a wrapped element.
     *
     * @return the builder instance enriched with the {@code isWrapped}
     */
    public DslElementDeclarationBuilder asWrappedElement(boolean isWrapped)
    {
        this.isWrapped = isWrapped;
        return this;
    }

    /**
     * Declares whether or not {@code this} {@link DslElementDeclaration} supports to be declared as child element
     * in the context for which it was created.
     *
     * @return the builder instance enriched with {@code supportsChildDeclaration}
     */
    public DslElementDeclarationBuilder supportsChildDeclaration(boolean supportsChild)
    {
        this.supportsChildDeclaration = supportsChild;
        return this;
    }

    /**
     * Adds a {@link DslElementDeclaration childElement} declaration to {@code this} {@link DslElementDeclaration} that
     * represents a generic type of {@code this} element.
     *
     * @return the builder instance enriched with the {@code typed} {@link DslElementDeclaration childElement}
     */
    public DslElementDeclarationBuilder withGeneric(MetadataType type, DslElementDeclaration child)
    {
        if (child == null)
        {
            throw new IllegalArgumentException("Invalid child declaration, child element should not be null");
        }

        this.genericChilds.put(type, child);
        return this;
    }

    /**
     * Adds a {@link DslElementDeclaration childElement} declaration to {@code this} {@link DslElementDeclaration} that
     * can be referenced by {@code name}
     *
     * @return the builder instance enriched with the {@code named} {@link DslElementDeclaration childElement}
     */
    public DslElementDeclarationBuilder withChild(String name, DslElementDeclaration child)
    {
        if (child == null)
        {
            throw new IllegalArgumentException("Invalid child declaration, child element should not be null");
        }

        this.namedChilds.put(name, child);
        return this;
    }

    /**
     * @return a new instance of {@link DslElementDeclaration}
     */
    public DslElementDeclaration build()
    {
        return new DslElementDeclaration(attributeName, elementName, elementNameSpace, isWrapped, supportsChildDeclaration, genericChilds, namedChilds);
    }

}

