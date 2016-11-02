/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.spi;

import org.mule.runtime.api.meta.model.EnrichableModel;
import org.mule.runtime.extension.api.declaration.DescribingContext;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;


/**
 * A component which can enrich {@link ExtensionDeclarer declarers} before they are actually turned
 * into models. This is useful for implementations of the extensions API which rely
 * on non programmatic describers.
 * <p/>
 * Because actual capabilities might be defined across several modules (or even extensions!)
 * instances are fetched through SPI
 *
 * @since 4.0
 */
public interface ModelEnricher {

  /**
   * Enriches the descriptor provided in the given {@code describingContext}. If this
   * enricher requires receiving state or wants to pass information to the next enricher,
   * then it should do it through the given {@code describingContext}
   *
   * @param describingContext the {@link DescribingContext} on which a {@link EnrichableModel} is being described
   */
  void enrich(DescribingContext describingContext);

}
