/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static org.mule.runtime.extension.api.util.NameUtils.getComponentModelTypeName;
import static org.mule.runtime.extension.api.util.NameUtils.getModelName;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.SimpleType;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.meta.model.util.ExtensionWalker;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

import java.util.Set;

/**
 * This validator makes sure that all {@link ParameterGroupModel group models} which contain exclusive parameters are correctly
 * defined. Which means that it must contain more than one optional parameters, and those optional parameter's
 * {@link MetadataType} must be either an {@link ObjectType} or a {@link SimpleType}.
 *
 * @since 1.0
 */
public final class ExclusiveParameterModelValidator implements ExtensionModelValidator {

  /**
   * {@inheritDoc}
   */
  @Override
  public void validate(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
    new ExtensionWalker() {

      @Override
      public void onParameterGroup(ParameterizedModel owner, ParameterGroupModel model) {
        model.getExclusiveParametersModels().forEach(exclusiveParametersModel -> {

          final Set<String> exclusiveParameterNames = exclusiveParametersModel.getExclusiveParameterNames();
          if (exclusiveParameterNames.isEmpty()) {
            problemsReporter.addError(new Problem(owner, String
                .format("In %s '%s', parameter group '%s' defines an empty set of exclusive parameters",
                        getComponentModelTypeName(owner),
                        getModelName(owner),
                        model.getName())));
          }

          if (exclusiveParameterNames.size() < 2) {
            problemsReporter.addError(new Problem(owner, String
                .format("In %s '%s', parameter group '%s' defines exclusive optional parameters, and thus should contain more than one "
                    + "parameter marked as optional but %d was/were found",
                        getComponentModelTypeName(owner),
                        getModelName(owner),
                        model.getName(),
                        exclusiveParameterNames.size())));
          }
        });
      }
    }.walk(extensionModel);
  }
}
