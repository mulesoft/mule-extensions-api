/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.mule.runtime.extension.api.util.NameUtils.getComponentModelTypeName;

import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.ComposableModel;
import org.mule.runtime.api.meta.model.EnrichableModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.construct.ConstructModel;
import org.mule.runtime.api.meta.model.function.FunctionModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.nested.NestedChainModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.util.IdempotentExtensionWalker;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.api.property.NoWrapperModelProperty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NoWrapperModelValidator implements ExtensionModelValidator {

  private static final String ERROR_PREFIX_REASON = "%s named %s is enriched with the NoWrapperModelProperty. %s";
  private static final String GENERIC_ERROR_REASON = "%ss are not allowed to be enriched with it.";
  private static final String PARAMETERS_ON_ENRICHED_COMPONENT =
      "A component cannot be enriched with the NoWrapperModelProperty and have parameters.";
  private static final String PARAMETER_OF_PROHIBITED_TYPE =
      "Parameters that are not array types or maps cannot be enriched with the NoWrapperModelProperty.";
  private static final String OWNER_IS_A_CHAIN =
      "A component cannot be enriched with the NoWrapperModelProperty if its owner is a Chain Model.";
  private static final String OWNER_IS_ALSO_ENRICHED =
      "Its owner is also enriched with the NoWrapperModelProperty. The owner and its child cannot be both enriched with it.";
  private static final String CHAIN_SIBILINGS_ENRICHED_FORMAT =
      "Component named %s has more than one nested chain enriched with the NoWrapperModelProperty [%s]. Only one chain child can be enriched with it.";

  private static final String PARAMETER_GROUP = "parameter group";
  private static final String PARAMETER = "parameter";

  @Override
  public void validate(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
    new IdempotentExtensionWalker() {

      private Set<ComposableModel> visitedComposableModel = new HashSet<>();

      @Override
      protected void onParameterGroup(ParameterizedModel owner, ParameterGroupModel model) {
        validateNoWrapperIsNotPresent(model, PARAMETER_GROUP, problemsReporter);
      }

      @Override
      protected void onParameter(ParameterizedModel owner, ParameterGroupModel groupModel, ParameterModel model) {
        model.getType().accept(new MetadataTypeVisitor() {

          @Override
          protected void defaultVisit(MetadataType metadataType) {
            validateNoWrapperIsNotPresent(model, PARAMETER, problemsReporter, PARAMETER_OF_PROHIBITED_TYPE);
          }

          @Override
          public void visitArrayType(ArrayType arrayType) {
            // no-op
          }

          @Override
          public void visitObject(ObjectType objectType) {
            if (!(objectType.isOpen() && objectType.getFields().isEmpty())) {
              validateNoWrapperIsNotPresent(model, PARAMETER, problemsReporter, PARAMETER_OF_PROHIBITED_TYPE);
            }
          }
        });
      }

      @Override
      protected void onNestable(ComposableModel owner, NestableElementModel model) {
        if (!visitedComposableModel.contains(owner)) {
          visitedComposableModel.add(owner);
          verifyChainChildren(owner);
        }

        if (owner instanceof NestedChainModel) {
          validateNoWrapperIsNotPresent(model, getComponentModelTypeName(model), problemsReporter, OWNER_IS_A_CHAIN);
        }

        if (!model.getAllParameterModels().isEmpty()) {
          validateNoWrapperIsNotPresent(model, getComponentModelTypeName(model), problemsReporter,
                                        PARAMETERS_ON_ENRICHED_COMPONENT);
        }
        if (owner instanceof EnrichableModel
            && ((EnrichableModel) owner).getModelProperty(NoWrapperModelProperty.class).isPresent()) {
          validateNoWrapperIsNotPresent(model, getComponentModelTypeName(model), problemsReporter, OWNER_IS_ALSO_ENRICHED);
        }
      }

      private void verifyChainChildren(ComposableModel owner) {
        List<String> noWrappedChainNames =
            owner.getNestedComponents().stream().filter(nestedComponent -> nestedComponent
                .getModelProperty(NoWrapperModelProperty.class).isPresent() && nestedComponent instanceof NestedChainModel)
                .map(nestedComponent -> nestedComponent.getName())
                .collect(Collectors.toList());
        if (noWrappedChainNames.size() > 1) {
          problemsReporter
              .addError(new Problem(owner,
                                    format(CHAIN_SIBILINGS_ENRICHED_FORMAT,
                                           owner.getName(), join(", ", noWrappedChainNames))));
        }
      }

      @Override
      protected void onConnectionProvider(ConnectionProviderModel model) {
        validateNoWrapperIsNotPresent(model, getComponentModelTypeName(model), problemsReporter);
      }

      @Override
      protected void onSource(SourceModel model) {
        validateNoWrapperIsNotPresent(model, getComponentModelTypeName(model), problemsReporter);
      }

      @Override
      protected void onConstruct(ConstructModel model) {
        validateNoWrapperIsNotPresent(model, getComponentModelTypeName(model), problemsReporter);
      }

      @Override
      protected void onOperation(OperationModel model) {
        validateNoWrapperIsNotPresent(model, getComponentModelTypeName(model), problemsReporter);
      }

      @Override
      protected void onFunction(FunctionModel model) {
        validateNoWrapperIsNotPresent(model, getComponentModelTypeName(model), problemsReporter);
      }

      @Override
      protected void onConfiguration(ConfigurationModel model) {
        validateNoWrapperIsNotPresent(model, getComponentModelTypeName(model), problemsReporter);
      }
    }.walk(extensionModel);
  }

  private <T extends EnrichableModel & NamedObject> void validateNoWrapperIsNotPresent(T model, String modelType,
                                                                                       ProblemsReporter problemsReporter) {
    if (model.getModelProperty(NoWrapperModelProperty.class).isPresent()) {
      String reason = format(GENERIC_ERROR_REASON, capitalize(modelType));
      validateNoWrapperIsNotPresent(model, modelType, problemsReporter, reason);
    }
  }

  private <T extends EnrichableModel & NamedObject> void validateNoWrapperIsNotPresent(T model, String modelType,
                                                                                       ProblemsReporter problemsReporter,
                                                                                       String reason) {
    if (model.getModelProperty(NoWrapperModelProperty.class).isPresent()) {
      problemsReporter
          .addError(new Problem(model,
                                format(ERROR_PREFIX_REASON, capitalize(modelType), model.getName(), reason)));
    }
  }

}
