/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection;

import org.mule.runtime.api.meta.model.EnrichableModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.api.introspection.declaration.DescribingContext;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.extension.api.introspection.declaration.spi.ModelEnricher;

/**
 * A factory that can take an {@link ExtensionDeclarer} and transform it into an actual
 * {@link ExtensionModel}.
 * <p>
 * This factory is also responsible for leveraging the available {@link ModelEnricher} implementations
 * to enrich the model before it's actually transformed into a {@link EnrichableModel}
 *
 * @since 1.0
 */
public interface ExtensionFactory {

  /**
   * Creates a {@link ExtensionModel} from the given {@code declarer}
   * using a specifying {@code describingContext}
   *
   * @param declarer          an {@link ExtensionDeclarer}. Cannot be {@code null}
   * @param describingContext a {@link DescribingContext}, useful to specify custom settings
   * @return an {@link ExtensionModel}
   */
  ExtensionModel createFrom(ExtensionDeclarer declarer, DescribingContext describingContext);
}
