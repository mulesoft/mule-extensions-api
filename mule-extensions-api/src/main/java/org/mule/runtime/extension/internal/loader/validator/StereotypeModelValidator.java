/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypeDefinition.NAMESPACE;
import static org.mule.runtime.extension.internal.util.ExtensionNamespaceUtils.getExtensionsNamespace;

import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.construct.ConstructModel;
import org.mule.runtime.api.meta.model.function.FunctionModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.stereotype.HasStereotypeModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.api.meta.model.util.IdempotentExtensionWalker;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

public class StereotypeModelValidator implements ExtensionModelValidator {

  @Override
  public void validate(ExtensionModel model, ProblemsReporter problemsReporter) {
    new Delegate(model, problemsReporter).validate();
  }

  private class Delegate {

    private final String extensionNamespace;
    private final ProblemsReporter problemsReporter;
    private final ExtensionModel extensionModel;

    private Delegate(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
      this.extensionModel = extensionModel;
      this.problemsReporter = problemsReporter;
      extensionNamespace = getExtensionsNamespace(extensionModel);
    }

    public void validate() {
      new IdempotentExtensionWalker() {

        @Override
        protected void onConfiguration(ConfigurationModel model) {
          doValidate(model);
        }

        @Override
        protected void onConnectionProvider(ConnectionProviderModel model) {
          doValidate(model);
        }

        @Override
        protected void onOperation(OperationModel model) {
          doValidate(model);
        }

        @Override
        protected void onConstruct(ConstructModel model) {
          doValidate(model);
        }

        @Override
        protected void onFunction(FunctionModel model) {
          super.onFunction(model);
        }
      }.walk(extensionModel);
    }

    private <T extends HasStereotypeModel & NamedObject> void doValidate(T model) {
      StereotypeModel stereotype = model.getStereotype();
      if (!isValidStereotype(stereotype)) {
        problemsReporter.addError(new Problem(model, format(
                                                            "Stereotype '%s' defines namespace '%s' which doesn't match extension stereotype '%s'. No extension can define "
                                                                + "stereotypes on namespaces other than its own",
                                                            stereotype.getType(), stereotype.getNamespace(),
                                                            extensionNamespace)));
      }
    }

    private boolean isValidStereotype(StereotypeModel stereotypeModel) {
      if (isBlank(stereotypeModel.getNamespace())) {
        return true;
      }

      return extensionNamespace.equals(stereotypeModel.getNamespace()) || NAMESPACE.equals(stereotypeModel.getNamespace());
    }
  }
}
