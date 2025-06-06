/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.CONTENT;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.PRIMARY_CONTENT;
import static org.mule.runtime.extension.api.annotation.Extension.DEFAULT_CONFIG_NAME;
import static org.mule.runtime.extension.privileged.util.ComponentDeclarationUtils.isConnectionProvisioningRequired;

import static java.lang.String.valueOf;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.utils.MetadataTypeUtils;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ConnectableComponentModel;
import org.mule.runtime.api.meta.model.EnrichableModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.SubTypesModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.construct.ConstructModel;
import org.mule.runtime.api.meta.model.display.ClassValueModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.nested.NestedChainModel;
import org.mule.runtime.api.meta.model.nested.NestedRouteModel;
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
import org.mule.runtime.api.util.Pair;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.display.ClassValue;
import org.mule.runtime.extension.api.property.ClassLoaderModelProperty;
import org.mule.runtime.extension.api.property.ImplicitConfigNameModelProperty;
import org.mule.runtime.extension.api.property.InfrastructureParameterModelProperty;
import org.mule.runtime.extension.api.property.ManyImplicitConfigsModelProperty;
import org.mule.runtime.extension.api.property.NoImplicitModelProperty;

import java.lang.reflect.AccessibleObject;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Utility methods for analyzing and decomposing {@link ExtensionModel} instances
 *
 * @since 1.0
 */
public class ExtensionModelUtils {

  private static final String EXPRESSION_PREFIX = "#[";
  private static final String EXPRESSION_POSTFIX = "]";

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
   * @param model the {@link ParameterModel parameter} who's default value is analyzed
   * @return Whether or not the default value is an expression
   * @since 1.2
   */
  public static boolean hasExpressionDefaultValue(ParameterModel model) {
    Object defaultValue = model.getDefaultValue();
    if (defaultValue instanceof String) {
      String trim = ((String) defaultValue).trim();
      return trim.startsWith(EXPRESSION_PREFIX) && trim.endsWith(EXPRESSION_POSTFIX);
    } else {
      return false;
    }
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

      private void collect(ConnectableComponentModel model) {
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
      protected void onConfiguration(ConfigurationModel model) {
        if (model == configurationModel) {
          model.getOperationModels()
              .forEach(this::collect);
          model.getSourceModels()
              .forEach(this::collect);

          stop();
        }
      }

      private void collect(ConnectableComponentModel model) {
        if (model.requiresConnection()) {
          connectedModels.add(model);
        }
      }
    }.walk(extensionModel);

    return connectedModels;
  }

  /**
   * @param extensionModel     a {@link ExtensionModel}
   * @param configurationModel a {@link ConfigurationModel}
   * @return Whether at least one of the models have a {@link ConnectionProviderModel}
   * @since 1.1.4
   */
  public static boolean supportsConnectivity(ExtensionModel extensionModel, ConfigurationModel configurationModel) {
    return !extensionModel.getConnectionProviders().isEmpty() || !configurationModel.getConnectionProviders().isEmpty();
  }

  /**
   * @param extensionModel the model which owns the {@code component}
   * @param component      a component
   * @return Whether the given {@code component} needs to be provided with a config in order to function
   */
  public static boolean requiresConfig(ExtensionModel extensionModel, NamedObject component) {
    if (!(component instanceof ConnectableComponentModel)) {
      return false;
    }

    if (isConnectionProvisioningRequired((ConnectableComponentModel) component)) {
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
          stop();
        }
      }
    }.walk(extensionModel);

    return result.get();
  }

  /**
   * @param extensionModel the model which owns the {@code component}
   * @param component      a component
   * @return A {@link Set} with the {@link ConfigurationModel} that the can be used alongside the {@code component}
   *
   * @deprecated since 1.10 Use {@link ImplicitConfigNameModelProperty} and {@link ManyImplicitConfigsModelProperty} instead.
   */
  @Deprecated
  public static Set<ConfigurationModel> getConfigurationForComponent(ExtensionModel extensionModel, ComponentModel component) {
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

    if (component instanceof ConnectableComponentModel && ((ConnectableComponentModel) component).requiresConnection()) {
      extensionModel.getConfigurationModel(DEFAULT_CONFIG_NAME).ifPresent(result::add);
    }

    return result;
  }

  public static boolean isContent(ParameterModel parameterModel) {
    return isContent(parameterModel.getRole());
  }

  /**
   * @return {@code true} if the given {@link ParameterModel} has layout property {@link LayoutModel#isText()}
   */
  public static boolean isText(ParameterModel parameter) {
    return parameter.getLayoutModel().map(LayoutModel::isText).orElse(false);
  }

  public static boolean isContent(ParameterRole purpose) {
    requireNonNull(purpose, "cannot evaluate null purpose");
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
   * @param <T>    the generic type of the items in the {@code models}. It's a type which is assignable from
   *               {@link ParameterizedModel}
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
    if (parameterizedModel instanceof EnrichableModel) {
      if (((EnrichableModel) parameterizedModel).getModelProperty(NoImplicitModelProperty.class).isPresent()) {
        return false;
      }
    }
    return parameterizedModel.getAllParameterModels().stream()
        .filter(p -> !p.isComponentId())
        .noneMatch(ParameterModel::isRequired);
  }

  /**
   * Returns whether the given {@link ComponentModel} has an associated configuration that can be created implicitly.
   *
   * @param extension the extension where the component belongs
   * @param component the actual component to check for the implicit config
   * @return true if the component has an implicit associated config, false otherwise.
   */
  public static boolean componentHasAnImplicitConfiguration(ExtensionModel extension, ComponentModel component) {
    List<ConfigurationModel> configs = extension.getConfigurationModels();

    if (configs.isEmpty()) {
      return true;
    }

    List<ConfigurationModel> implicitConfigs = configs.stream()
        .filter(config -> config.getOperationModels().contains(component) || config.getSourceModels().contains(component))
        .filter(ExtensionModelUtils::canBeUsedImplicitly)
        .collect(toList());

    return implicitConfigs.stream().anyMatch(config -> {
      List<ConnectionProviderModel> providers = config.getConnectionProviders();
      return providers.isEmpty()
          || providers.stream()
              .anyMatch(ExtensionModelUtils::canBeUsedImplicitly);
    });

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
   * Returns the default value associated with the given annotation.
   * <p>
   * The reason for this method to be instead of simply using {@link org.mule.sdk.api.annotation.param.Optional#defaultValue()} is
   * a limitation on the Java language to have an annotation which defaults to a {@code null} value. For that reason, this method
   * tests the default value for equality against the {@link org.mule.sdk.api.annotation.param.Optional#NULL}. If such test is
   * positive, then {@code null} is returned.
   * <p>
   * If a {@code null} {@code optional} is supplied, then this method returns {@code null}
   *
   * @param optional a nullable annotation
   * @return the default value associated to the annotation or {@code null}
   */
  public static String getDefaultValue(org.mule.sdk.api.annotation.param.Optional optional) {
    if (optional == null) {
      return null;
    }

    String defaultValue = optional.defaultValue();
    return org.mule.sdk.api.annotation.param.Optional.NULL.equals(defaultValue) ? null : defaultValue;
  }

  /**
   * Tests the given {@code object} to be annotated with {@link org.mule.runtime.extension.api.annotation.param.Optional} or
   * {@link org.mule.sdk.api.annotation.param.Optional}.
   * <p>
   * If the annotation is present, then a default value is extracted by the rules of
   * {@link #getDefaultValue(org.mule.runtime.extension.api.annotation.param.Optional)} or
   * {@link #getDefaultValue(org.mule.sdk.api.annotation.param.Optional)}. Otherwise, {@code null} is returned.
   * <p>
   * Notice that a {@code null} return value doesn't necessarily mean that the annotation is not present. It could well be that
   * {@code null} happens to be the default value.
   *
   * @param object an object potentially annotated with {@link org.mule.runtime.extension.api.annotation.param.Optional} or
   *               {@link org.mule.sdk.api.annotation.param.Optional}
   * @return A default value or {@code null}
   */
  public static Object getDefaultValue(AccessibleObject object) {
    org.mule.sdk.api.annotation.param.Optional optional = object.getAnnotation(org.mule.sdk.api.annotation.param.Optional.class);
    if (optional != null) {
      return getDefaultValue(optional);
    } else {
      return getDefaultValue(object.getAnnotation(org.mule.runtime.extension.api.annotation.param.Optional.class));
    }
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

  /**
   * Uses the value in the given {@code annotation} and transforms it into a {@link ClassValueModel}
   *
   * @param annotation an annotation
   * @return a {@link ClassValueModel}
   */
  public static ClassValueModel toClassValueModel(ClassValue annotation) {
    String[] parents = annotation.extendsOrImplements();
    if (parents != null) {
      return new ClassValueModel(Stream.of(parents).filter(p -> !isBlank(p)).collect(toList()));
    } else {
      return new ClassValueModel(emptyList());
    }
  }

  /**
   * Gets the extension {@link ClassLoader}
   *
   * @param extensionModel
   * @return an {@link Optional} with the extension {@link ClassLoader}
   */
  public static Optional<ClassLoader> getExtensionClassLoader(ExtensionModel extensionModel) {
    return extensionModel.getModelProperty(ClassLoaderModelProperty.class)
        .map(ClassLoaderModelProperty::getClassLoader);
  }

  /**
   * Given a {@link ParameterizedModel}, it returns a stream with all pairs of groups and parameters involving this model
   *
   * @param model
   * @return a {@link Stream} of {@link Pair} of {@link ParameterGroupModel} and its {@link ParameterModel}, associated to the
   *         model.
   */
  public static Stream<Pair<ParameterGroupModel, ParameterModel>> getGroupAndParametersPairs(ParameterizedModel model) {
    return model.getParameterGroupModels()
        .stream()
        .flatMap(g -> g.getParameterModels().stream().map(p -> new Pair<>(g, p)));
  }

  /**
   * @param model a {@link ComponentModel}
   * @return Whether the given {@code model} represents a Scope
   *
   * @since 1.5.0
   */
  public static boolean isScope(ComponentModel model) {
    return model.getNestedComponents().stream()
        .anyMatch(NestedChainModel.class::isInstance);
  }

  /**
   * @param model a {@link ConstructModel}
   * @return Whether the given {@code model} represents a Router
   *
   * @since 1.5.0
   */
  public static boolean isRouter(ConstructModel model) {
    return isRouter((ComponentModel) model);
  }

  /**
   * @param model a {@link ComponentModel}
   * @return Whether the given {@code model} represents a Router
   *
   * @since 1.7.0
   */
  public static boolean isRouter(ComponentModel model) {
    return model.getNestedComponents().stream()
        .anyMatch(NestedRouteModel.class::isInstance);
  }
}
