/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.isBasic;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.isMapOfStrings;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.getDefaultValue;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.hasExpressionDefaultValue;
import static org.mule.runtime.extension.api.util.NameUtils.CONFIGURATION;
import static org.mule.runtime.extension.api.util.NameUtils.CONNECTION_PROVIDER;
import static org.mule.runtime.extension.api.util.NameUtils.getComponentModelTypeName;

import org.mule.metadata.api.annotation.EnumAnnotation;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.StringType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.meta.model.util.ExtensionWalker;
import org.mule.runtime.extension.api.connectivity.oauth.OAuthParameterModelProperty;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Validates that all {@link ParameterModel parameters} provided by the {@link ConfigurationModel configurations},
 * {@link ConnectionProviderModel connection providers} and {@link OperationModel operations} from the {@link ExtensionModel
 * extension} complies with:
 * <ul>
 * <li>If the parameter is a {@link ArrayType} the name should be plural</li>
 * <li>The {@link MetadataType metadataType} must be provided</li>
 * <li>If required, cannot provide a default value</li>
 * <li>The {@link Class} of the parameter must be valid too, that implies that the class shouldn't contain any field with a
 * reserved name.
 * </ul>
 *
 * @since 1.0.0
 */
public final class ParameterModelValidator implements ExtensionModelValidator {

  @Override
  public void validate(ExtensionModel model, ProblemsReporter problemsReporter) {
    new ValidatorDelegate().validate(model, problemsReporter);
  }

  private class ValidatorDelegate implements ExtensionModelValidator {

    private ProblemsReporter problemsReporter;
    private Set<String> validatedComponentIdContainers = new HashSet<>();

    @Override
    public void validate(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
      this.problemsReporter = problemsReporter;

      new ExtensionWalker() {

        @Override
        public void onParameter(ParameterizedModel owner, ParameterGroupModel groupModel, ParameterModel model) {
          String ownerName = owner.getName();
          String ownerModelType = getComponentModelTypeName(owner);
          validateParameter(model, ownerName, ownerModelType, owner);
          validateOAuthParameter(model, ownerName, ownerModelType);
        }
      }.walk(extensionModel);
    }

    private void validateParameter(ParameterModel parameterModel, String ownerName, String ownerModelType,
                                   ParameterizedModel owner) {
      if (parameterModel.getType() == null) {
        addError(parameterModel, "Parameter '%s' in the %s '%s' must provide a type",
                 parameterModel.getName(), ownerModelType, ownerName);
      }

      if (getDefaultValue(parameterModel).isPresent()) {
        validateDefaultValue(parameterModel, ownerName, ownerModelType);
      }

      if (parameterModel.isComponentId()) {
        validateComponentId(parameterModel, ownerName, ownerModelType, owner);
      }

    }

    private void validateDefaultValue(ParameterModel parameterModel, String ownerName, String ownerModelType) {
      if (parameterModel.isOverrideFromConfig() &&
          !ownerModelType.equals(CONFIGURATION) && !ownerModelType.equals(CONNECTION_PROVIDER)) {
        // We skip failing for configs and connection here since a different error is thrown in their own validators
        addError(parameterModel,
                 "Parameter '%s' in the %s '%s' is declared as a config override,"
                     + " and must not provide a default value since one is already provided by the value"
                     + " declared in the config parameter",
                 parameterModel.getName(), ownerModelType, ownerName);
      } else if (parameterModel.isRequired()) {
        addError(parameterModel,
                 "Parameter '%s' in the %s '%s' is required, and must not provide a default value",
                 parameterModel.getName(), ownerModelType, ownerName);
      }

      if (!hasExpressionDefaultValue(parameterModel)) {
        parameterModel.getType().accept(new MetadataTypeVisitor() {

          @Override
          public void visitString(StringType type) {
            type.getAnnotation(EnumAnnotation.class).ifPresent(enumAnnotation -> {
              List<String> values = Stream.of(enumAnnotation.getValues()).map(Object::toString).collect(toList());
              String stringValue = parameterModel.getDefaultValue().toString();
              if (!values.contains(stringValue)) {
                addError(parameterModel,
                         "Parameter '%s' in the %s '%s' has '%s' as default value which is not listed as an available option (i.e.: %s).",
                         parameterModel.getName(), ownerModelType, ownerName, stringValue, String.join(", ", values));
              }
            });
          }
        });
      }
    }

    private void validateComponentId(ParameterModel parameterModel, String ownerName, String ownerModelType,
                                     ParameterizedModel owner) {
      if (!parameterModel.isRequired()) {
        addError(parameterModel,
                 "Parameter '%s' in the %s '%s' is declared as a Component ID, but is also marked as Optional. "
                     + "Only a required parameter can serve as Component ID",
                 parameterModel.getName(), ownerModelType, ownerName);
      }

      if (parameterModel.getType() instanceof StringType && !validatedComponentIdContainers.contains(ownerName)) {
        validatedComponentIdContainers.add(ownerName);
        List<String> componentIdParameters = owner.getAllParameterModels().stream()
            .filter(ParameterModel::isComponentId)
            .map(NamedObject::getName)
            .collect(toList());

        if (componentIdParameters.size() > 1) {
          addError(owner,
                   "The %s '%s' declares multiple parameters as Component ID. "
                       + "Only one parameter can serve as ID for a given Component. "
                       + "Affected parameters are: %s",
                   ownerModelType, ownerName, componentIdParameters);
        }

      } else {
        addError(parameterModel,
                 "Parameter '%s' in the %s '%s' is declared as a Component ID, but is of type '%s'. "
                     + "Only String parameters are allowed as Component ID",
                 parameterModel.getName(), ownerModelType, ownerName, getId(parameterModel.getType()).orElse("Unknown"));
      }

      if (!NOT_SUPPORTED.equals(parameterModel.getExpressionSupport())) {
        addError(parameterModel,
                 "Parameter '%s' in the %s '%s' is declared as a Component ID, but declares its expression support as '%s'. "
                     + "Dynamic values are not allowed for a Component ID parameter",
                 parameterModel.getName(), ownerModelType, ownerName, parameterModel.getExpressionSupport().name());
      }

      if (!BEHAVIOUR.equals(parameterModel.getRole())) {
        addError(parameterModel,
                 "Parameter '%s' in the %s '%s' is declared as a Component ID, but is also declared as '%s'. "
                     + "A parameter can't be declared both as Component ID and Content.",
                 parameterModel.getName(), ownerModelType, ownerName, parameterModel.getRole().name());
      }

      if (parameterModel.isOverrideFromConfig()) {
        addError(parameterModel,
                 "Parameter '%s' in the %s '%s' is declared as a Component ID, but is also declared as a ConfigOverride. "
                     + "A Component ID can't be declared as a ConfigOverride since it describe the ID of each individual component "
                     + "and no common global value should be used here.",
                 parameterModel.getName(), ownerModelType, ownerName);
      }

      parameterModel.getLayoutModel()
          .ifPresent(layout -> {
            if (layout.isText()) {
              addError(parameterModel,
                       "Parameter '%s' in the %s '%s' is declared as a Component ID, but is also declared as 'Text'. "
                           + "A Component ID can't be declared as a Text parameter.",
                       parameterModel.getName(), ownerModelType, ownerName);
            }

            if (layout.isPassword()) {
              addError(parameterModel,
                       "Parameter '%s' in the %s '%s' is declared as a Component ID, but is also declared as 'Password'. "
                           + "A Component ID can't be declared as a Password parameter.",
                       parameterModel.getName(), ownerModelType, ownerName);
            }

            if (layout.isQuery()) {
              addError(parameterModel,
                       "Parameter '%s' in the %s '%s' is declared as a Component ID, but is also declared as 'Query'. "
                           + "A Component ID can't be declared as a Query parameter.",
                       parameterModel.getName(), ownerModelType, ownerName);
            }
          });

    }

    private void validateOAuthParameter(ParameterModel parameterModel, String ownerName, String ownerModelType) {
      parameterModel.getModelProperty(OAuthParameterModelProperty.class).ifPresent(p -> {
        if (parameterModel.getExpressionSupport() != NOT_SUPPORTED) {
          addError(parameterModel,
                   "Parameter '%s' in the %s [%s] is an OAuth parameter yet it supports expressions. "
                       + "Expressions are not supported on OAuth parameters",
                   parameterModel.getName(), ownerModelType, ownerName);
        }

        if (!isBasic(parameterModel.getType()) && !isMapOfStrings(parameterModel.getType())) {
          addError(parameterModel,
                   "Parameter '%s' in the %s [%s] is an OAuth parameter but is a %s. "
                       + "Only basic types are supported on OAuth parameters",
                   parameterModel.getName(), ownerModelType, ownerName, parameterModel.getType().getClass().getSimpleName());
        }
      });
    }

    private void addError(NamedObject object, String msg, Object... args) {
      problemsReporter.addError(new Problem(object, format(msg, args)));
    }
  }
}
