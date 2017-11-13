/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;

import java.util.Collection;


/**
 * This {@link DeclarationEnricherPhase phases} are used to create a {@link DeclarationEnricher}s execution chain,
 * each {@link DeclarationEnricherPhase} describes a particular enrichment state from which {@link DeclarationEnricher}s can
 * take advantage of.
 *
 * @since 1.0
 */
public enum DeclarationEnricherPhase {

  /**
   * First phase of {@link DeclarationEnricher}s to be executed.
   * <p>
   * Basic settings that doesn't change the structure of the loaded extension but are required before the
   * structure enrichment phase is executed should go in this phase.
   * <p>
   * {@link DeclarationEnricher}s added to this phase should not add or change the structure of the extension nor
   * change/add any configuration to the different elements that were loaded (Connections, Configs, Parameters, Etc)
   */
  INITIALIZE,

  /**
   * This phase contains all {@link DeclarationEnricher}s that enrich the extension with additional elements, for example adding
   * common parameters.
   */
  STRUCTURE_DECLARATION,

  /**
   * This phase contains all {@link DeclarationEnricher}s that go over the final extension structure and add metadata and
   * {@link ModelProperty properties} to the declared elements.
   */
  POST_STRUCTURE_DECLARATION,

  /**
   * This phase contains all {@link DeclarationEnricher}s that add display elements to the final enriched extension.
   */
  DISPLAY_ELEMENTS_DECLARATION,

  /**
   * Final phase of {@link DeclarationEnricher}s to be executed, {@link DeclarationEnricher enrichers} that for some reason should
   * execute at the end of the chain should go in this phase.
   */
  FINALIZE
}
