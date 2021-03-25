/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.values;

import org.mule.api.annotation.NoImplement;
import org.mule.sdk.api.values.Value;
import org.mule.sdk.api.values.ValueResolvingException;

import java.util.Set;

/**
 * This interface allows the exposure of the {@link Value values} associated to a parameter of a Component.
 *
 * @since 1.4
 */
@NoImplement
public interface SdkComponentValueProvider {

  /**
   * @param providerName the name of the parameter for which resolve their possible {@link Value values}
   * @return the resolved {@link Value values}
   */
  Set<Value> getValues(String providerName) throws ValueResolvingException;
}
