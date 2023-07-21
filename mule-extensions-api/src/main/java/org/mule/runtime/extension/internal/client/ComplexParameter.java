/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.client;

import java.util.Map;

/**
 * Represents a complex parameter that has a type and it's composed by other parameters.
 * <p>
 * This class is not part of the API and should not be used by anyone (or anything) but the runtime. Backwards compatibility not
 * guaranteed on this class.
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
