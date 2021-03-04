/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.error;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.Set;

/**
 * A component responsible for providing an idempotent {@link Set} of {@link ErrorTypeDefinition} representing the list of errors
 * that a component can throw.
 *
 * @since 1.0
 * @see Throws
 * @see ErrorTypeDefinition
 * @deprecated use {@link org.mule.sdk.api.annotation.error.ErrorTypeProvider} instead.
 */
@Deprecated
public interface ErrorTypeProvider {

  /**
   * @return a {@link Set} of {@link ErrorTypeDefinition}
   */
  Set<ErrorTypeDefinition> getErrorTypes();
}
