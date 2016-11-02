/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.spi;


import org.mule.runtime.extension.api.declaration.DescribingContext;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;

/**
 * Returns a {@link ExtensionDeclarer} which contains a raw representation of the
 * extension.
 * <p/>
 * @since 1.0
 */
public interface Describer {

  /**
   * Describes the extension as a {@link ExtensionDeclarer}
   *
   * @param context a {@link DescribingContext} with state relevant to the operation
   * @return a {@link ExtensionDeclarer}
   */
  ExtensionDeclarer describe(DescribingContext context);

}
