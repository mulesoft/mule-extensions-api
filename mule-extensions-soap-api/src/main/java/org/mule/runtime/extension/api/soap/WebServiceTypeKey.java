/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
}
