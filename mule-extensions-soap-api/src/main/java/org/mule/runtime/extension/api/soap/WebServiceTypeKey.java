/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.soap;

/**
 * Represents a multilevel metadata key that describes a soap operation giving a web service.
 *
 * @since 1.0
 */
public final class WebServiceTypeKey {

  /**
   * The service that contains the operation
   */
  private String service;

  /**
   * The operation to be executed
   */
  private String operation;

  public String getService() {
    return service;
  }

  public String getOperation() {
    return operation;
  }

  /**
   * This is for call from the runtime when building this component. Additional calls to this will fail with
   * {@link IllegalStateException}.
   * 
   * @param service the service name
   * 
   * @since 1.8
   */
  public final void setService(String service) {
    if (this.service != null) {
      throw new IllegalStateException("'service' had been already set in WebServiceTypeKey '" + this.toString() + "'");
    }
    this.service = service;
  }

  /**
   * This is for call from the runtime when building this component. Additional calls to this will fail with
   * {@link IllegalStateException}.
   * 
   * @param operation the operation name
   * 
   * @since 1.8
   */
  public final void setOperation(String operation) {
    if (this.operation != null) {
      throw new IllegalStateException("'operation' had been already set in WebServiceTypeKey '" + this.toString() + "'");
    }
    this.operation = operation;
  }

  @Override
  public String toString() {
    return "WebServiceTypeKey{service=" + this.service + ";operation=" + this.operation + "}";
  }
}
