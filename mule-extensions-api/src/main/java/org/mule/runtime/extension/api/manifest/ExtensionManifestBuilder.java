/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.manifest;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.HashMap;
import java.util.Map;

/**
 * A builder to create instances of {@link ExtensionManifest}.
 * <p>
 * A new instance of this class should be used per each manifest to be
 * created. The created instances will be immutable.
 *
 * @since 1.0
 */
public final class ExtensionManifestBuilder
{

    private String name;
    private String description;
    private String version;
    private final DescriberManifestBuilder describerManifestBuilder = new DescriberManifestBuilder();

    /**
     * Sets the extension's name
     *
     * @param name the name the extension will have
     * @return {@code this} builder
     */
    public ExtensionManifestBuilder setName(String name)
    {
        this.name = name;
        return this;
    }

    /**
     * Sets the extension's description
     *
     * @param description the name the extension will have
     * @return {@code this} builder
     */
    public ExtensionManifestBuilder setDescription(String description)
    {
        this.description = description;
        return this;
    }

    /**
     * Sets the extension's version
     *
     * @param version the name the extension will have
     * @return {@code this} builder
     */
    public ExtensionManifestBuilder setVersion(String version)
    {
        this.version = version;
        return this;
    }

    /**
     * Returns a {@link DescriberManifestBuilder} to be used
     * for created the associated {@link DescriberManifest}.
     * <p>
     * Multiple invocations of this method over the same instance
     * will always return the same value
     *
     * @return a {@link DescriberManifestBuilder}
     */
    public DescriberManifestBuilder withDescriber()
    {
        return describerManifestBuilder;
    }

    /**
     * Creates and returns an {@link ExtensionManifest} according to the values set
     *
     * @return a {@link ExtensionManifest}
     * @throws IllegalStateException is {@link #setName(String)} or {@link #setVersion(String)} were never invoked or used with a blank value
     */
    public ExtensionManifest build()
    {
        return new ImmutableExtensionManifest(name, description, version, describerManifestBuilder.build());
    }

    /**
     * A builder to create instances of {@link DescriberManifest}.
     * <p>
     * A new instance of this class should be used per each manifest to be
     * created. The created instances will be immutable.
     * <p>
     * Instances of this class should be obtained through the
     * {@link #withDescriber()} method.
     *
     * @since 1.0
     */
    public static final class DescriberManifestBuilder
    {

        private String id;
        private final Map<String, String> properties = new HashMap<>();

        private DescriberManifestBuilder()
        {
        }

        /**
         * Sets the describer's ID
         *
         * @param id the id to be set
         * @return {@code this} builder
         */
        public DescriberManifestBuilder setId(String id)
        {
            this.id = id;
            return this;
        }

        /**
         * Sets the given property on the describer.
         * <p>
         * If a value is already associated with the {@code key}, then
         * it is overridden with the new {@code value}
         *
         * @param key   the property's key
         * @param value the property's value
         * @return {@code this} builder
         */
        public DescriberManifestBuilder addProperty(String key, String value)
        {
            validatePropertyKey(key);
            properties.put(key, value);
            return this;
        }

        /**
         * Adds all the entries in the given {@code properties} map
         * as a property into the describer.
         * <p>
         * If the {@code properties} map contains any key already present
         * on the describer, then the existing pair will be discarded in
         * favor of the new one.
         *
         * @param properties a {@link Map} of properties to be set
         * @return {@code this} builder
         */
        public DescriberManifestBuilder addProperties(Map<String, String> properties)
        {
            properties.keySet().forEach(this::validatePropertyKey);
            this.properties.putAll(properties);
            return this;
        }

        private void validatePropertyKey(String key)
        {
            if (isBlank(key))
            {
                throw new IllegalArgumentException("Cannot add a property with a blank key");
            }
        }

        /**
         * Creates and returns a new {@link DescriberManifest} according to the values set
         *
         * @return a {@link DescriberManifest}
         * @throws IllegalStateException if {@link #setId(String)} was never invoked or used with a blank value
         */
        private DescriberManifest build()
        {
            return new ImmutableDescriberManifest(id, properties);
        }
    }
}
