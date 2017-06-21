/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import static java.lang.String.valueOf;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.CONTENT;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.PRIMARY_CONTENT;
import static org.mule.runtime.api.util.Preconditions.checkArgument;
import static org.mule.runtime.extension.api.annotation.Extension.DEFAULT_CONFIG_NAME;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.utils.MetadataTypeUtils;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.SubTypesModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.operation.HasOperationModels;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterRole;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.meta.model.source.HasSourceModels;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.util.ExtensionWalker;
import org.mule.runtime.api.meta.model.util.IdempotentExtensionWalker;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.internal.property.InfrastructureParameterModelProperty;

import java.lang.reflect.AccessibleObject;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Utility methods for analyzing and decomposing {@link ExtensionModel} instances
 *
 * @since 1.0
 */
public class ExtensionModelUtils {

  private ExtensionModelUtils() {}

  /**
   * @param model the {@link ParameterModel parameter} who's default value is wanted
   * @return the default value of the parameter
   */
  public static Optional<String> getDefaultValue(ParameterModel model) {
    Object defaultValue = model.getDefaultValue();
    return defaultValue == null ? empty() : of(valueOf(defaultValue));
  }

  /**
   * Retrieves the default value of a field for a given {@link MetadataType}
   *
   * @param name  the field's name
   * @param model the {@link MetadataType} containing the field who's default value is wanted
   * @return the default value of the given parameter
   */
  public static Optional<String> getDefaultValue(String name, MetadataType model) {
    Reference<String> value = new Reference<>();

    model.accept(new MetadataTypeVisitor() {

      @Override
      protected void defaultVisit(MetadataType metadataType) {
        MetadataTypeUtils.getDefaultValue(metadataType).ifPresent(value::set);
      }

      @Override
      public void visitObject(ObjectType objectType) {
        objectType.getFields().stream()
            .filter(f -> f.getKey().getName().getLocalPart().equals(name))
            .findFirst()
            .ifPresent(fieldType -> MetadataTypeUtils.getDefaultValue(fieldType)
                .ifPresent(value::set));
      }

      @Override
      public void visitUnion(UnionType unionType) {
        unionType.getTypes().stream()
            .map(type -> getDefaultValue(name, type))
            .filter(Optional::isPresent)
            .findFirst()
            .ifPresent(defaultValue -> value.set(defaultValue.get()));
      }

    });

    return ofNullable(value.get());
  }

  /**
   * Collects the {@link ParameterModel parameters} from {@code model} which supports or requires expressions
   *
   * @param model a {@link ParameterizedModel}
   * @return a {@link List} of {@link ParameterModel}. Can be empty but will never be {@code null}
   */
  public static List<ParameterModel> getDynamicParameters(ParameterizedModel model) {
    return model.getAllParameterModels().stream().filter(parameter -> acceptsExpressions(parameter.getExpressionSupport()))
        .collect(toList());
  }

  /**
   * @param support a {@link ExpressionSupport}
   * @return Whether or not the given {@code support} is one which accepts or requires expressions
   */
  public static boolean acceptsExpressions(ExpressionSupport support) {
    return support == SUPPORTED || support == REQUIRED;
  }

  /**
   * Returns a {@link List} with all the {@link ComponentModel} available to the {@code extensionModel} which requires a
   * connection. This includes both {@link SourceModel} and {@link OperationModel}.
   *
   * @param extensionModel a {@link ExtensionModel}
   * @return a {@link List} of {@link ComponentModel}. It might be empty but will never be {@code null}
   */
  public static List<ComponentModel> getConnectedComponents(ExtensionModel extensionModel) {
    List<ComponentModel> connectedModels = new LinkedList<>();
    new IdempotentExtensionWalker() {

      @Override
      public void onOperation(OperationModel model) {
        collect(model);
      }

      @Override
      public void onSource(SourceModel model) {
        collect(model);
      }

      private void collect(ComponentModel model) {
        if (model.requiresConnection()) {
          connectedModels.add(model);
        }
      }
    }.walk(extensionModel);

    return connectedModels;
  }

  /**
   * Returns a {@link List} with all the {@link ComponentModel} available to the {@code configurationModel} which requires a
   * connection. This includes both {@link SourceModel} and {@link OperationModel}.
   *
   * @param extensionModel     the {@link ExtensionModel} that owns the {@code configurationModel}
   * @param configurationModel the {@link ConfigurationModel} which components you want
   * @return a {@link List} of {@link ComponentModel}. It might be empty but will never be {@code null}
   */
  public static List<ComponentModel> getConnectedComponents(ExtensionModel extensionModel,
                                                            ConfigurationModel configurationModel) {
    List<ComponentModel> connectedModels = new LinkedList<>();
    new ExtensionWalker() {

      @Override
      public void onOperation(HasOperationModels owner, OperationModel model) {
        if (owner == configurationModel) {
          collect(owner, model);
        }
      }

      @Override
      public void onSource(HasSourceModels owner, SourceModel model) {
        collect(owner, model);
      }

      private void collect(Object owner, ComponentModel model) {
        if (owner == configurationModel && model.requiresConnection()) {
          connectedModels.add(model);
        }
      }
    }.walk(extensionModel);

    return connectedModels;
  }

  /**
   * @param extensionModel the model which owns the {@code component}
   * @param component      a component
   * @return Whether the given {@code component} needs to be provided with a config
   * in order to function
   */
  public static boolean requiresConfig(ExtensionModel extensionModel, NamedObject component) {
    if (!(component instanceof ComponentModel)) {
      return false;
    }

    ComponentModel model = (ComponentModel) component;
    if (model.requiresConnection()) {
      return true;
    }

    Reference<Boolean> result = new Reference<>(false);
    new ExtensionWalker() {

      @Override
      public void onOperation(HasOperationModels owner, OperationModel model) {
        resolve(model, owner);
      }

      @Override
      public void onSource(HasSourceModels owner, SourceModel model) {
        resolve(model, owner);
      }

      private void resolve(ComponentModel model, Object owner) {
        if (!(owner instanceof ExtensionModel) && model == component) {
          result.set(true);
        }
      }
    }.walk(extensionModel);

    return result.get();
  }

  /**
   * @param extensionModel the model which owns the {@code component}
   * @param component a component
   * @return A {@link Set} with the {@link ConfigurationModel} that the can be used alongside with the {@code component}
   */
  public static Set<ConfigurationModel> getConfigurationForComponent(ExtensionModel extensionModel,
                                                                     ComponentModel component) {
    Set<ConfigurationModel> result = new HashSet<>();
    new ExtensionWalker() {

      @Override
      public void onOperation(HasOperationModels owner, OperationModel model) {
        resolve(model, owner);
      }

      @Override
      public void onSource(HasSourceModels owner, SourceModel model) {
        resolve(model, owner);
      }

      private void resolve(ComponentModel model, Object owner) {
        if (model == component && owner != extensionModel) {
          result.add((ConfigurationModel) owner);
        }
      }
    }.walk(extensionModel);

    if (component.requiresConnection()) {
      extensionModel.getConfigurationModel(DEFAULT_CONFIG_NAME).ifPresent(result::add);
    }

    return result;
  }

  public static boolean isContent(ParameterModel parameterModel) {
    return isContent(parameterModel.getRole());
  }

  /**
   * @return {@code true} if the given {@link ParameterModel} has layout property
   * {@link LayoutModel#isText()}
   */
  public static boolean isText(ParameterModel parameter) {
    return parameter.getLayoutModel().map(LayoutModel::isText).orElse(false);
  }

  public static boolean isContent(ParameterRole purpose) {
    checkArgument(purpose != null, "cannot evaluate null purpose");
    return purpose != BEHAVIOUR;
  }

  public static ParameterRole roleOf(Optional<Content> content) {
    return content.map(c -> c.primary() ? PRIMARY_CONTENT : CONTENT).orElse(BEHAVIOUR);
  }

  public static Map<ObjectType, Set<ObjectType>> toSubTypesMap(Collection<SubTypesModel> subTypes) {
    return subTypes.stream().collect(toMap(SubTypesModel::getBaseType, SubTypesModel::getSubTypes));
  }

  /**
   * @return {@code true} if the given {@link ParameterModel} was enriched with {@link InfrastructureParameterModelProperty}
   */
  public static boolean isInfrastructure(ParameterModel parameter) {
    return parameter.getModelProperty(InfrastructureParameterModelProperty.class).isPresent();
  }

  /**
   * Returns the first item in the {@code models} {@link List} that can be used implicitly.
   * <p>
   * A {@link ParameterizedModel} is consider to be implicit when all its {@link ParameterModel}s are optional
   *
   * @param models a {@link List} of {@code T}
   * @param <T> the generic type of the items in the {@code models}. It's a type which is assignable from
   *        {@link ParameterizedModel}
   * @return one of the items in {@code models} or {@code null} if none of the models are implicit
   */
  public static <T extends ParameterizedModel> T getFirstImplicit(List<T> models) {
    return models.stream()
        .filter(ExtensionModelUtils::canBeUsedImplicitly)
        .findFirst()
        .orElse(null);
  }

  /**
   * Returns whether the given model can be used implicitly. That means that all of its parameters are optional.
   *
   * @param parameterizedModel model
   * @return whether the given model can be used implicitly or not.
   */
  public static boolean canBeUsedImplicitly(ParameterizedModel parameterizedModel) {
    return parameterizedModel.getAllParameterModels().stream().noneMatch(ParameterModel::isRequired);
  }

  /**
   * Returns the default value associated with the given annotation.
   * <p>
   * The reason for this method to be instead of simply using
   * {@link org.mule.runtime.extension.api.annotation.param.Optional#defaultValue()} is a limitation on the Java language to have
   * an annotation which defaults to a {@code null} value. For that reason, this method tests the default value for equality
   * against the {@link org.mule.runtime.extension.api.annotation.param.Optional#NULL}. If such test is positive, then
   * {@code null} is returned.
   * <p>
   * If a {@code null} {@code optional} is supplied, then this method returns {@code null}
   *
   * @param optional a nullable annotation
   * @return the default value associated to the annotation or {@code null}
   */
  public static String getDefaultValue(org.mule.runtime.extension.api.annotation.param.Optional optional) {
    if (optional == null) {
      return null;
    }

    String defaultValue = optional.defaultValue();
    return org.mule.runtime.extension.api.annotation.param.Optional.NULL.equals(defaultValue) ? null : defaultValue;
  }

  /**
   * Tests the given {@code object} to be annotated with {@link org.mule.runtime.extension.api.annotation.param.Optional}.
   * <p>
   * If the annotation is present, then a default value is extracted by the rules of
   * {@link #getDefaultValue(org.mule.runtime.extension.api.annotation.param.Optional)}. Otherwise, {@code null} is returned.
   * <p>
   * Notice that a {@code null} return value doesn't necessarily mean that the annotation is not present. It could well be that
   * {@code null} happens to be the default value.
   *
   * @param object an object potentially annotated with {@link org.mule.runtime.extension.api.annotation.param.Optional}
   * @return A default value or {@code null}
   */
  public static Object getDefaultValue(AccessibleObject object) {
    return getDefaultValue(object.getAnnotation(org.mule.runtime.extension.api.annotation.param.Optional.class));
  }

  /**
   * Tests if the given {@code group} contains at least one required {@link ParameterModel}
   *
   * @param group the {@link ParameterGroupModel} to be inspected
   * @return {@code true} if the given {@code group} contains at least one required {@link ParameterModel}
   */
  public static boolean isRequired(ParameterGroupModel group) {
    return group.getParameterModels().stream().anyMatch(ParameterModel::isRequired);
  }
}
