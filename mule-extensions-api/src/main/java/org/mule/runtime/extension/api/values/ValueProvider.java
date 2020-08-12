/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.values;

import org.mule.runtime.api.value.Value;

import java.util.Set;

/**
 * Provider of {@link Value values}.
 *
 * @since 1.0
 * @see Value
 * @deprecated use {@link org.mule.sdk.api.values.ValueProvider} instead.
 */
@Deprecated
public interface ValueProvider {

  /**
   * Resolves and provides a {@link Set} of {@link Value values} which represents a set of possible and valid values for
   * a parameter.
   *
   * @return a {@link Set} of {@link Value values}.
   * @throws ValueResolvingException if an error occurs during the resolving
   */
  Set<Value> resolve() throws ValueResolvingException;

  /**
   * This is the id of the ValueProvider. It should not be repeated among {@link ValueProvider} in the same extension.
   * Multiple calls to this method must always return the same fixed value.
   *
   * @return name of the resolver
   * @since 1.4.0
   */
  default String getId() {
    return getClass().getName();
  }

}
