/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence.metadata;

import org.mule.runtime.api.metadata.resolving.FailureCode;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;

/**
 * DTO that represents a {@link MetadataFailure} into a serializable format.
 *
 * @since 1.0
 */
class Failure {

  private final String failureCode;
  private final String message;
  private final String reason;
  private final String failureComponent;

  Failure(MetadataFailure failure, String failureComponent) {
    this.failureComponent = failureComponent;
    this.reason = failure.getReason();
    this.message = failure.getMessage();
    this.failureCode = failure.getFailureCode().getName();
  }

  FailureCode getFailureCode() {
    return new FailureCode(failureCode);
  }

  String getMessage() {
    return message;
  }

  String getReason() {
    return reason;
  }

  String getFailureComponent() {
    return failureComponent;
  }
}
