/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static org.mule.runtime.extension.internal.loader.validator.ModelValidationUtils.validateConfigOverrideParametersNotAllowed;

import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.connection.HasConnectionProviderModels;
import org.mule.runtime.api.meta.model.util.ExtensionWalker;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

/**
 * {@link ExtensionModelValidator} which applies to {@link ExtensionModel}s which contains {@link ConnectionProviderModel}s
 * 
 * This validator makes sure that:
 * <ul>
 * <li>No parameter of any {@link ConnectionProviderModel} is defined as a configuration override</li>
 * </ul>
 *
 * @since 1.5
 */
public final class ConnectionProviderModelValidator implements ExtensionModelValidator {

  @Override
  public void validate(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {

    new ExtensionWalker() {

      @Override
      public void onConnectionProvider(HasConnectionProviderModels owner, ConnectionProviderModel model) {
        validateConfigOverrideParametersNotAllowed(model, problemsReporter, "Connection");
      }
    }.walk(extensionModel);

  }

}
