/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.util;

import static org.mule.runtime.extension.api.error.MuleErrors.VALIDATION;

import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.sdk.api.error.MuleErrors;

import java.util.Collection;
import java.util.Optional;

/**
 * Utilities for handling extension errors.
 * <p>
 * This class <b>IS NOT</b> part of the API. To be used by the Mule Runtime only
 *
 * @since 1.5.0
 */
public final class ExtensionErrorUtils {

  /**
   * Traverses the {@code errors} collection and returns the first item which represents a {@link MuleErrors#VALIDATION} error
   *
   * @param errors a set of {@link ErrorModel}
   * @return an optional {@link ErrorModel}
   */
  public static Optional<ErrorModel> getValidationError(Collection<ErrorModel> errors) {
    return errors.stream()
        .filter(ExtensionErrorUtils::isValidationError)
        .findAny();
  }

  private static boolean isValidationError(ErrorModel errorModel) {
    if (VALIDATION.getType().equals(errorModel.getType()) &&
        "MULE".equals(errorModel.getNamespace())) {
      return true;
    }

    return errorModel.getParent().map(ExtensionErrorUtils::isValidationError).orElse(false);
  }

  private ExtensionErrorUtils() {}
}
