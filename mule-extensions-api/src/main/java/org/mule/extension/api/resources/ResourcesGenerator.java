/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.resources;

import org.mule.extension.api.introspection.ExtensionModel;
import org.mule.extension.api.resources.spi.GenerableResourceContributor;

import java.util.List;

/**
 * A component capable of dynamically generating resources to back up
 * a set of {@link ExtensionModel extensionModels}.
 * Although extensions resolve their functionality mainly on runtime,
 * some configuration resources such as XML schemas, service registration files,
 * spring bundles, or whatever resource the runtime requires need to be generated
 * in compile or run time.
 * <p/>
 * Additionally, those resources might reference more than one
 * {@link ExtensionModel},
 * for example, if a module registers many extensions,
 * we need only one service registration file for all of them.
 * <p/>
 * This interface provides semantics to generate resources for many
 * {@link ExtensionModel}s which might share the
 * generated resources and when finished, dump all of the generated content to some persistent store.
 * <p/>
 * To do so, implementations will work in tandem with instances of
 * {@link GenerableResourceContributor} which will be
 * obtained and injected by the platform.
 * <p/>
 * Implementations are to be assumed not thread-safe and to be used in a
 * fire and forget fashion
 *
 * @since 1.0
 */
public interface ResourcesGenerator
{

    /**
     * Returns a {@link GeneratedResource} that
     * will point to the given {@code filepath}. If another extension already contributed
     * to that resource, then the same instance is returned. If no resource has been
     * contributed to yet, then it's created and registered.
     *
     * @param filepath the path in which the resource is to be generated
     * @return a {@link GeneratedResource}
     */
    GeneratedResource get(String filepath);

    /**
     * Generates resources for the given {@code extension}. This doesn't mean that
     * the resource will be generated and written to disk. It means that
     * the discovered instances of {@link GenerableResourceContributor}
     * will be asked to contribute resources for the given extension. The contributors
     * will use {@link #get(String)} to get a hold of the resource in generation
     * and will contribute their part of the content. Nothing gets written to disk
     * until {@link #dumpAll()} is invoked
     *
     * @param extensionModel a {@link ExtensionModel}
     */
    void generateFor(ExtensionModel extensionModel);

    /**
     * Writes all the {@link GeneratedResource}s that were
     * generated through {@link #get(String)} and writes them to a persistent store.
     * The details of where and how those files are written are completely up to the implementation.
     *
     * @return the list of {@link GeneratedResource} that was written
     */
    List<GeneratedResource> dumpAll();
}
