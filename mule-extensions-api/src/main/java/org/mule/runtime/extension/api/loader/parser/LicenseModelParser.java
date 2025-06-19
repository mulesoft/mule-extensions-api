/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader.parser;

import org.mule.api.annotation.NoImplement;

import java.util.Optional;

/**
 * Parses the syntactic definition of the licensing information.
 *
 * @since 1.10.0
 */
@NoImplement
public interface LicenseModelParser {

  /**
   * @return true if the extension requires an EE license, false otherwise.
   */
  boolean requiresEeLicense();

  /**
   * @return true if the extension can be run with the evaluation license, false otherwise.
   */
  boolean isAllowsEvaluationLicense();

  /**
   * @return the required entitlement in the license to be able to run this extension. Empty if the extension does not require an
   *         entitlement.
   */
  Optional<String> getRequiredEntitlement();
}
