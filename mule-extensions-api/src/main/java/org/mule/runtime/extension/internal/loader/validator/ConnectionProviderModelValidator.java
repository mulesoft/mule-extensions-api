/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
