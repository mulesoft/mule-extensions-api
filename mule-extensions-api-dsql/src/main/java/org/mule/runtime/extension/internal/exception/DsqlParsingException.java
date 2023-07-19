/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.exception;

import org.mule.runtime.extension.api.dsql.DsqlQuery;

/**
 * {@link RuntimeException} implementation that is thrown when an unexpected behaviour
 * occurs when parsing a {@link DsqlQuery}.
 *
 * @since 1.0
 */
public class DsqlParsingException extends RuntimeException {

  public DsqlParsingException(Throwable t) {
    super(t);
  }

  public DsqlParsingException(String s) {
    super(s);
  }

  public DsqlParsingException() {}
}
