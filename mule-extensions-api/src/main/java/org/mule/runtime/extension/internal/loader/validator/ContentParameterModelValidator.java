/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.PRIMARY_CONTENT;
import static org.mule.runtime.extension.api.declaration.type.TypeUtils.getAllFields;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;
import static org.mule.runtime.extension.api.util.NameUtils.getComponentModelTypeName;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.utils.MetadataTypeUtils;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.util.IdempotentExtensionWalker;
import org.mule.runtime.extension.api.annotation.metadata.MetadataKeyId;
import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.declaration.type.TypeUtils;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils;
import org.mule.runtime.extension.api.util.ExtensionModelUtils;

import com.google.common.base.Joiner;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Validates that all content parameters are property configured.
 * <p>
 * For configs and connection providers, tt validates that they do not have content parameters.
 * For Operations and sources, it validates that content parameters meet the following:
 * <p>
 * <ul>
 * <li>Support or require expressions</li>
 * <li>They don't allow DSL references</li>
 * <li>There's no more than one {@link ParameterRole#PRIMARY_CONTENT}</li>
 * <li>{@link ParameterRole#PRIMARY_CONTENT} parameter is optional and defaults to {@link Optional#PAYLOAD}</li>
 * </ul>
 *
 * @since 1.0
 */
public class ContentParameterModelValidator implements ExtensionModelValidator {

  @Override
  public void validate(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
    new IdempotentExtensionWalker() {

      @Override
      public void onConfiguration(ConfigurationModel model) {
        validateNoContent(model, problemsReporter);
      }

      @Override
      protected void onConnectionProvider(ConnectionProviderModel model) {
        validateNoContent(model, problemsReporter);
      }

      @Override
      protected void onOperation(OperationModel model) {
        validateContent(model, problemsReporter);
      }

      @Override
      protected void onSource(SourceModel model) {
        validateContent(model, problemsReporter);
      }

      @Override
      protected void onParameter(ParameterGroupModel groupModel, ParameterModel model) {
        model.getType().accept(new MetadataTypeVisitor() {

          @Override
          public void visitObject(ObjectType objectType) {
            validateNoContentField(objectType);
            validateNoMetadataField(objectType);
          }

          @Override
          public void visitArrayType(ArrayType arrayType) {
            arrayType.getType().accept(this);
          }

          @Override
          public void visitUnion(UnionType unionType) {
            unionType.getTypes().forEach(t -> t.accept(this));
          }

          void validateNoContentField(ObjectType objectType) {
            final List<String> contentFields = objectType.getFields().stream()
                .filter(TypeUtils::isContent)
                .map(MetadataTypeUtils::getLocalPart)
                .collect(toList());

            if (!contentFields.isEmpty()) {
              problemsReporter.addError(new Problem(model, String
                  .format("Parameter '%s' of type '%s' in group '%s' contains content fields: [%s]."
                      + " Content fields are not allowed in complex types.",
                          model.getName(), getId(objectType), groupModel.getName(),
                          Joiner.on(", ").join(contentFields))));
            }
          }

          void validateNoMetadataField(ObjectType objectType) {

            ExtensionMetadataTypeUtils.getType(objectType)
                .ifPresent(clazz -> {
                  List<String> metadataFields = getAllFields(clazz).stream()
                      .filter(f -> f.getAnnotation(MetadataKeyId.class) != null || f.getAnnotation(TypeResolver.class) != null)
                      .map(Field::getName)
                      .collect(toList());

                  if (!metadataFields.isEmpty()) {
                    problemsReporter.addError(new Problem(model, String
                        .format("Parameter '%s' of type '%s' in group '%s' contains fields [%s]"
                            + " annotated with metadata and dynamic type resolution."
                            + " Metadata annotations cannot be used as part of types, and are allowed only in Parameters",
                                model.getName(), getId(objectType), groupModel.getName(),
                                Joiner.on(", ").join(metadataFields))));
                  }
                });
          }
        });
      }
    }.walk(extensionModel);
  }

  private void validateNoContent(ParameterizedModel model, ProblemsReporter problemsReporter) {
    List<ParameterModel> contentParameters = getContentParameters(model);

    if (!contentParameters.isEmpty()) {
      problemsReporter.addError(problem(model, String
          .format("contains content parameters. Content parameters are not allowed on %s components",
                  getComponentModelTypeName(model))));
    }
  }

  private void validateContent(ParameterizedModel model, ProblemsReporter problemsReporter) {
    List<ParameterModel> contentParameters = getContentParameters(model);

    if (contentParameters.isEmpty()) {
      return;
    }

    validatePrimaryContent(model, contentParameters, problemsReporter);
    validateDsl(model, contentParameters, problemsReporter);
    validateExpressionSupport(model, contentParameters, problemsReporter);
  }

  private void validateExpressionSupport(ParameterizedModel model, List<ParameterModel> contentParameters,
                                         ProblemsReporter problemsReporter) {
    List<ParameterModel> expressionLess = contentParameters.stream()
        .filter(p -> p.getExpressionSupport() == NOT_SUPPORTED)
        .collect(toList());

    if (!expressionLess.isEmpty()) {
      problemsReporter.addError(problem(model,
                                        format("contains content parameters which don't allow expressions. Expressions"
                                            + " are mandatory for all content parameters. Offending parameters are: [%s]",
                                               join(expressionLess))));
    }
  }

  private void validateDsl(ParameterizedModel model, List<ParameterModel> contentParameters, ProblemsReporter problemsReporter) {
    List<ParameterModel> offending = contentParameters.stream()
        .filter(p -> p.getDslConfiguration().allowsReferences())
        .collect(toList());

    if (!offending.isEmpty()) {
      problemsReporter.addError(problem(model, format("contains content parameters which allow references. "
          + "Offending parameters are: [%s]", join(offending))));
    }
  }

  private Problem problem(NamedObject model, String message) {
    return new Problem(model, String.format("'%s' %s %s ",
                                            getComponentModelTypeName(model),
                                            model.getName(),
                                            message));
  }

  private void validatePrimaryContent(ParameterizedModel model, List<ParameterModel> contentParameters,
                                      ProblemsReporter problemsReporter) {
    List<ParameterModel> primaryContents = contentParameters.stream()
        .filter(p -> p.getRole() == PRIMARY_CONTENT)
        .collect(toList());

    if (primaryContents.isEmpty()) {
      problemsReporter
          .addError(problem(model, format("contains %d content parameters but none of them is primary", primaryContents.size())));
    } else if (primaryContents.size() > 1) {
      problemsReporter.addError(problem(model, format("contains %d content parameters marked as primary. Only one primary "
          + "content parameter is allowed. Offending parameters are [%s]",
                                                      primaryContents.size(),
                                                      join(primaryContents))));
    }
  }

  private List<ParameterModel> getContentParameters(ParameterizedModel model) {
    return model.getAllParameterModels().stream().filter(ExtensionModelUtils::isContent).collect(toList());
  }

  private String join(List<ParameterModel> offending) {
    return Joiner.on(", ").join(offending);
  }
}
