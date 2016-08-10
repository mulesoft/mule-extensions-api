/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import java.util.List;

/**
 * Defines a contract for a {@link BaseDeclaration} which contains a {@link List} of
 * {@link ParameterizedDeclaration}
 *
 * @since 1.0
 */
public interface ParameterizedDeclaration {

  /**
   * @return A {@link List} of {@link ParameterizedDeclaration}
   */
  List<ParameterDeclaration> getParameters();
}
