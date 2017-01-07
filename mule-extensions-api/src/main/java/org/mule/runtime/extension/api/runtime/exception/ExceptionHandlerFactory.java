/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.exception;

/**
 * Creates an instance of an {@link ExceptionHandler}
 */
public interface ExceptionHandlerFactory {

  /**
   * Creates a new instance of a {@link ExceptionHandler}
   *
   * @return a new {@link ExceptionHandler}
   */
  ExceptionHandler createHandler();
}
