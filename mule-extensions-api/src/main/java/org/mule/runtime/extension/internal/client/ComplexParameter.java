/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.client;

import java.util.Map;

/**
 * Represents a complex parameter that has a type and it's composed by other parameters.
 * <p>
 * This class is not part of the API and should not be used by anyone (or anything) but the runtime. Backwards compatibility
 * not guaranteed on this class.
 *
 * @since 1.0
 */
public class ComplexParameter {

  private final Class<?> type;
  private final Map<String, Object> parameters;

  public ComplexParameter(Class<?> type, Map<String, Object> parameters) {
    this.type = type;
    this.parameters = parameters;
  }

  public Class<?> getType() {
    return type;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }
}
