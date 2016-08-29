/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.exception;

import org.mule.runtime.extension.api.dsql.DsqlQuery;

/**
 * {@link RuntimeException} implementation that is throwed when an unexpected behaviour
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
