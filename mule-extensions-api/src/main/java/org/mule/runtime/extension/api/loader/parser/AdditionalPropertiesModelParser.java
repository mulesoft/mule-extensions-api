/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader.parser;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.meta.model.ModelProperty;

import java.util.List;

/**
 * General contract for a model parser capable of reading additional model properties
 *
 * @since 1.10.0
 */
@NoImplement
public interface AdditionalPropertiesModelParser {

  /**
   * Returns a list with all the {@link ModelProperty model properties} to be applied at the extension level which are
   * specifically linked to the type of syntax used to define the extension.
   *
   * @return a list with {@link ModelProperty} instances.
   */
  List<ModelProperty> getAdditionalModelProperties();

}
