package org.mule.runtime.extension;

import org.mule.runtime.api.meta.NameableObject;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

public class ExternalValidator implements ExtensionModelValidator {

    public void validate(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
        problemsReporter.addError(new Problem(new NameableObject() {
            public void setName(String s) {
            }

            public String getName() {
                return "hi";
            }
        }, "THIS IS WORKING"));
    }
}
