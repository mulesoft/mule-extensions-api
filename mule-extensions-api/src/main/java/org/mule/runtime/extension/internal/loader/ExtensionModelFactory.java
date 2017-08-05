/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader;

import static com.google.common.collect.ImmutableSet.of;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.api.meta.model.stereotype.StereotypeModelBuilder.newStereotype;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.ANY;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.PROCESSOR;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.SOURCE;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;
import static org.mule.runtime.extension.api.util.NameUtils.alphaSortDescribedList;
import static org.mule.runtime.internal.dsl.DslConstants.CORE_PREFIX;
import static org.slf4j.LoggerFactory.getLogger;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.OutputModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.construct.ConstructModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConstructDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.FunctionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.NestableElementDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.NestedChainDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.NestedComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.NestedRouteDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OutputDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceCallbackDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.function.FunctionModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ExclusiveParametersModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.meta.model.source.SourceCallbackModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.exception.IllegalParameterModelDefinitionException;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.api.model.ImmutableExtensionModel;
import org.mule.runtime.extension.api.model.ImmutableOutputModel;
import org.mule.runtime.extension.api.model.config.ImmutableConfigurationModel;
import org.mule.runtime.extension.api.model.connection.ImmutableConnectionProviderModel;
import org.mule.runtime.extension.api.model.construct.ImmutableConstructModel;
import org.mule.runtime.extension.api.model.function.ImmutableFunctionModel;
import org.mule.runtime.extension.api.model.nested.ImmutableNestedChainModel;
import org.mule.runtime.extension.api.model.nested.ImmutableNestedComponentModel;
import org.mule.runtime.extension.api.model.nested.ImmutableNestedRouteModel;
import org.mule.runtime.extension.api.model.operation.ImmutableOperationModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableExclusiveParametersModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableParameterGroupModel;
import org.mule.runtime.extension.api.model.parameter.ImmutableParameterModel;
import org.mule.runtime.extension.api.model.source.ImmutableSourceCallbackModel;
import org.mule.runtime.extension.api.model.source.ImmutableSourceModel;
import org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils;
import org.mule.runtime.extension.api.util.ParameterModelComparator;
import org.mule.runtime.extension.internal.loader.enricher.ClassLoaderDeclarationEnricher;
import org.mule.runtime.extension.internal.loader.enricher.ConnectionProviderDeclarationEnricher;
import org.mule.runtime.extension.internal.loader.enricher.ContentParameterDeclarationEnricher;
import org.mule.runtime.extension.internal.loader.enricher.ExecutionTypeDeclarationEnricher;
import org.mule.runtime.extension.internal.loader.enricher.ExtensionTypesDeclarationEnricher;
import org.mule.runtime.extension.internal.loader.enricher.OAuthDeclarationEnricher;
import org.mule.runtime.extension.internal.loader.enricher.SourceDeclarationEnricher;
import org.mule.runtime.extension.internal.loader.enricher.StreamingDeclarationEnricher;
import org.mule.runtime.extension.internal.loader.enricher.TargetParameterDeclarationEnricher;
import org.mule.runtime.extension.internal.loader.enricher.TransactionalDeclarationEnricher;
import org.mule.runtime.extension.internal.loader.enricher.XmlDeclarationEnricher;
import org.mule.runtime.extension.internal.loader.validator.ConnectionProviderNameModelValidator;
import org.mule.runtime.extension.internal.loader.validator.ContentParameterModelValidator;
import org.mule.runtime.extension.internal.loader.validator.ExclusiveParameterModelValidator;
import org.mule.runtime.extension.internal.loader.validator.FunctionModelValidator;
import org.mule.runtime.extension.internal.loader.validator.NameClashModelValidator;
import org.mule.runtime.extension.internal.loader.validator.OperationParametersModelValidator;
import org.mule.runtime.extension.internal.loader.validator.ParameterModelValidator;
import org.mule.runtime.extension.internal.loader.validator.SourceCallbacksModelValidator;
import org.mule.runtime.extension.internal.loader.validator.SubtypesModelValidator;
import org.mule.runtime.extension.internal.loader.validator.TransactionalParametersValidator;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.UncheckedExecutionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import org.slf4j.Logger;

/**
 * A factory that can take an {@link ExtensionDeclarer} and transform it into an actual
 * {@link ExtensionModel}.
 * <p>
 * This factory is also responsible of applying the {@link DeclarationEnricher} which are default
 * to the runtime plus the ones specified through {@link ExtensionLoadingContext#addCustomValidators(Collection)}
 * <p>
 * This class is not part of the API and should not be used by anyone (or anything) but the runtime. Backwards compatibility
 * not guaranteed on this class.
 *
 * @since 1.0
 */
public final class ExtensionModelFactory {

  private final Logger LOGGER = getLogger(ExtensionModelFactory.class);

  private final List<DeclarationEnricher> declarationEnrichers;
  private final List<ExtensionModelValidator> extensionModelValidators;

  public ExtensionModelFactory() {
    declarationEnrichers = unmodifiableList((asList(
                                                    new ClassLoaderDeclarationEnricher(),
                                                    new ContentParameterDeclarationEnricher(),
                                                    new ExecutionTypeDeclarationEnricher(),
                                                    new ExtensionTypesDeclarationEnricher(),
                                                    new XmlDeclarationEnricher(),
                                                    new TargetParameterDeclarationEnricher(),
                                                    new ConnectionProviderDeclarationEnricher(),
                                                    new SourceDeclarationEnricher(),
                                                    new StreamingDeclarationEnricher(),
                                                    new OAuthDeclarationEnricher(),
                                                    new TransactionalDeclarationEnricher())));

    extensionModelValidators = unmodifiableList(asList(
                                                       new ConnectionProviderNameModelValidator(),
                                                       new ContentParameterModelValidator(),
                                                       new ExclusiveParameterModelValidator(),
                                                       new NameClashModelValidator(),
                                                       new OperationParametersModelValidator(),
                                                       new FunctionModelValidator(),
                                                       new ParameterModelValidator(),
                                                       new SubtypesModelValidator(),
                                                       new SourceCallbacksModelValidator(),
                                                       new TransactionalParametersValidator()));
  }

  /**
   * Transforms the given {@code extensionLoadingContext} into a valid {@link ExtensionModel}
   * using a specifying {@code describingContext}
   *
   * @param extensionLoadingContext a {@link ExtensionLoadingContext}, useful to specify custom settings
   * @return an {@link ExtensionModel}
   */
  public ExtensionModel create(ExtensionLoadingContext extensionLoadingContext) {

    enrichModel(extensionLoadingContext);

    ExtensionModel extensionModel =
        new FactoryDelegate().toExtension(extensionLoadingContext.getExtensionDeclarer().getDeclaration());

    ProblemsReporter problemsReporter = new ProblemsReporter(extensionModel);

    validate(extensionModel, problemsReporter, extensionLoadingContext);

    if (problemsReporter.hasErrors()) {
      throw new IllegalModelDefinitionException(format("Extension '%s' has definition errors:\n%s", extensionModel.getName(),
                                                       problemsReporter.toString()));
    } else if (problemsReporter.hasWarnings()) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(format("Extension '%s' has definition warnings:\n%s", extensionModel.getName(),
                            problemsReporter.getWarningsAsString()));
      }
    }

    return extensionModel;
  }

  private void validate(ExtensionModel extensionModel, ProblemsReporter problemsReporter,
                        ExtensionLoadingContext extensionLoadingContext) {
    List<ExtensionModelValidator> validators = new LinkedList<>(extensionModelValidators);
    validators.addAll(extensionLoadingContext.getCustomValidators());

    validators.forEach(v -> v.validate(extensionModel, problemsReporter));
  }

  private void validateMuleVersion(ExtensionDeclaration extensionDeclaration) {
    final String version = extensionDeclaration.getVersion();
    if (version == null || version.trim().length() == 0) {
      throw new IllegalModelDefinitionException(format("Extension '%s' did not specified a version",
                                                       extensionDeclaration.getName()));
    }

    try {
      new MuleVersion(version);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(format("Invalid version '%s' for extension '%s'",
                                                version, extensionDeclaration.getName()));
    }
  }

  private void enrichModel(ExtensionLoadingContext extensionLoadingContext) {
    List<DeclarationEnricher> enrichers = new LinkedList<>(extensionLoadingContext.getCustomDeclarationEnrichers());
    enrichers.addAll(declarationEnrichers);

    enrichers.forEach(enricher -> enricher.enrich(extensionLoadingContext));
  }

  private boolean isExpression(String value) {
    return value.startsWith("#[") && value.endsWith("]");
  }

  private class FactoryDelegate {

    private Cache<ParameterizedDeclaration, ParameterizedModel> modelCache = CacheBuilder.newBuilder().build();

    private ExtensionModel toExtension(ExtensionDeclaration extensionDeclaration) {
      validateMuleVersion(extensionDeclaration);
      ExtensionModel extensionModel =
          new ImmutableExtensionModel(extensionDeclaration.getName(), extensionDeclaration.getDescription(),
                                      extensionDeclaration.getVersion(), extensionDeclaration.getVendor(),
                                      extensionDeclaration.getCategory(), extensionDeclaration.getMinMuleVersion(),
                                      sortConfigurations(toConfigurations(extensionDeclaration.getConfigurations())),
                                      toOperations(extensionDeclaration.getOperations()),
                                      toConnectionProviders(extensionDeclaration.getConnectionProviders()),
                                      toMessageSources(extensionDeclaration.getMessageSources()),
                                      toFunctions(extensionDeclaration.getFunctions()),
                                      toConstructs(extensionDeclaration.getConstructs()),
                                      extensionDeclaration.getDisplayModel(),
                                      extensionDeclaration.getXmlDslModel(),
                                      extensionDeclaration.getSubTypes(),
                                      toExtensionTypes(extensionDeclaration.getTypes(), extensionDeclaration.getImportedTypes()),
                                      extensionDeclaration.getResources(),
                                      extensionDeclaration.getImportedTypes(),
                                      extensionDeclaration.getErrorModels(),
                                      extensionDeclaration.getExternalLibraryModels(),
                                      extensionDeclaration.getModelProperties(),
                                      extensionDeclaration.getPrivilegedPackages(),
                                      extensionDeclaration.getPrivilegedArtifacts());

      return extensionModel;
    }

    private List<ConfigurationModel> sortConfigurations(List<ConfigurationModel> configurationModels) {
      if (configurationModels.isEmpty()) {
        return configurationModels;
      }

      return alphaSortDescribedList(configurationModels);
    }


    private List<ConfigurationModel> toConfigurations(List<ConfigurationDeclaration> declarations) {
      return declarations.stream().map(this::toConfiguration).collect(toList());
    }

    private <T extends ParameterizedModel> T fromCache(ParameterizedDeclaration declaration,
                                                       Supplier<ParameterizedModel> supplier) {
      try {
        return (T) modelCache.get(declaration, supplier::get);
      } catch (UncheckedExecutionException e) {
        if (e.getCause() instanceof RuntimeException) {
          throw (RuntimeException) e.getCause();
        }
        throw e;
      } catch (ExecutionException e) {
        throw new MuleRuntimeException(e);
      }
    }

    private ConfigurationModel toConfiguration(ConfigurationDeclaration declaration) {
      return fromCache(declaration,
                       () -> new ImmutableConfigurationModel(declaration.getName(),
                                                             declaration.getDescription(),
                                                             toParameterGroups(declaration.getParameterGroups()),
                                                             toOperations(declaration.getOperations()),
                                                             toConnectionProviders(declaration.getConnectionProviders()),
                                                             toMessageSources(declaration.getMessageSources()),
                                                             declaration.getExternalLibraryModels(),
                                                             declaration.getDisplayModel(),
                                                             declaration.getModelProperties()));
    }

    private List<ConstructModel> toConstructs(List<ConstructDeclaration> constructs) {
      return constructs.stream().map(this::toConstruct).collect(toList());
    }

    private ConstructModel toConstruct(ConstructDeclaration declaration) {
      return fromCache(declaration,
                       () -> new ImmutableConstructModel(declaration.getName(),
                                                         declaration.getDescription(),
                                                         toParameterGroups(declaration.getParameterGroups()),
                                                         declaration.getDisplayModel(),
                                                         getProcessorStereotypes(declaration.getStereotypes()),
                                                         declaration.getModelProperties(),
                                                         toNestedComponentModels(declaration.getNestedComponents()),
                                                         declaration.allowsTopLevelDefinition()));
    }

    private List<SourceModel> toMessageSources(List<SourceDeclaration> declarations) {
      return unmodifiableList(alphaSortDescribedList(declarations.stream().map(this::toMessageSource).collect(toList())));
    }

    private SourceModel toMessageSource(SourceDeclaration declaration) {
      return fromCache(declaration,
                       () -> new ImmutableSourceModel(declaration.getName(), declaration.getDescription(),
                                                      declaration.hasResponse(),
                                                      toParameterGroups(declaration.getParameterGroups()),
                                                      toOutputModel(declaration.getOutput()),
                                                      toOutputModel(declaration.getOutputAttributes()),
                                                      toSourceCallback(declaration.getSuccessCallback()),
                                                      toSourceCallback(declaration.getErrorCallback()),
                                                      toSourceCallback(declaration.getTerminateCallback()),
                                                      declaration.isRequiresConnection(),
                                                      declaration.isTransactional(),
                                                      declaration.isSupportsStreaming(),
                                                      declaration.getDisplayModel(),
                                                      getSourceStereotypes(declaration),
                                                      declaration.getErrorModels(),
                                                      declaration.getModelProperties(),
                                                      toNestedComponentModels(declaration.getNestedComponents())));
    }

    private Set<StereotypeModel> getSourceStereotypes(SourceDeclaration declaration) {
      if (!declaration.getStereotypes().isEmpty()) {
        return declaration.getStereotypes();
      }

      return of(newStereotype(SOURCE.getName(), CORE_PREFIX.toUpperCase())
          .withParent(newStereotype(ANY.getName(), CORE_PREFIX.toUpperCase()).build()).build());
    }

    private Optional<SourceCallbackModel> toSourceCallback(Optional<SourceCallbackDeclaration> callbackDeclaration) {
      return callbackDeclaration.map(callback -> new ImmutableSourceCallbackModel(callback.getName(),
                                                                                  callback.getDescription(),
                                                                                  toParameterGroups(callback
                                                                                      .getParameterGroups()),
                                                                                  callback.getDisplayModel(),
                                                                                  callback.getModelProperties()));
    }

    private Set<StereotypeModel> getProcessorStereotypes(Set<StereotypeModel> stereotypeModels) {
      if (!stereotypeModels.isEmpty()) {
        return stereotypeModels;
      }

      return of(newStereotype(PROCESSOR.name(), CORE_PREFIX.toUpperCase())
          .withParent(newStereotype(ANY.name(), CORE_PREFIX.toUpperCase()).build()).build());
    }

    private List<OperationModel> toOperations(List<OperationDeclaration> declarations) {
      return unmodifiableList(alphaSortDescribedList(declarations.stream().map(this::toOperation).collect(toList())));
    }

    private OperationModel toOperation(OperationDeclaration declaration) {
      return fromCache(declaration, () -> {
        OperationModel operation;

        operation = new ImmutableOperationModel(declaration.getName(),
                                                declaration.getDescription(),
                                                toParameterGroups(declaration.getParameterGroups()),
                                                toOutputModel(declaration.getOutput()),
                                                toOutputModel(declaration.getOutputAttributes()),
                                                declaration.isBlocking(),
                                                declaration.getExecutionType(),
                                                declaration.isRequiresConnection(),
                                                declaration.isTransactional(),
                                                declaration.isSupportsStreaming(),
                                                declaration.getDisplayModel(),
                                                declaration.getErrorModels(),
                                                getProcessorStereotypes(declaration.getStereotypes()),
                                                declaration.getModelProperties(),
                                                toNestedComponentModels(declaration.getNestedComponents()));

        return operation;
      });
    }

    private List<NestableElementModel> toNestedComponentModels(List<NestableElementDeclaration> nestedComponents) {
      return nestedComponents.stream().map(this::toNestedComponent).collect(toList());
    }

    private NestableElementModel toNestedComponent(NestableElementDeclaration declaration) {
      if (declaration instanceof NestedRouteDeclaration) {
        return new ImmutableNestedRouteModel(declaration.getName(),
                                             declaration.getDescription(),
                                             toParameterGroups(((NestedRouteDeclaration) declaration).getParameterGroups()),
                                             declaration.getDisplayModel(),
                                             ((NestedRouteDeclaration) declaration).getMinOccurs(),
                                             ((NestedRouteDeclaration) declaration).getMaxOccurs(),
                                             toNestedComponentModels(((NestedRouteDeclaration) declaration)
                                                 .getNestedComponents()),
                                             declaration.getModelProperties());
      }
      if (declaration instanceof NestedChainDeclaration) {
        return new ImmutableNestedChainModel(declaration.getName(),
                                             declaration.getDescription(),
                                             declaration.getDisplayModel(),
                                             declaration.isRequired(),
                                             getProcessorStereotypes(((NestedChainDeclaration) declaration)
                                                 .getAllowedStereotypes()),
                                             declaration.getModelProperties());
      }
      return new ImmutableNestedComponentModel(declaration.getName(),
                                               declaration.getDescription(),
                                               declaration.getDisplayModel(),
                                               declaration.isRequired(),
                                               getProcessorStereotypes(((NestedComponentDeclaration<NestedComponentDeclaration>) declaration)
                                                   .getAllowedStereotypes()),
                                               declaration.getModelProperties());
    }

    private List<ConnectionProviderModel> toConnectionProviders(List<ConnectionProviderDeclaration> declarations) {
      return unmodifiableList(alphaSortDescribedList(declarations.stream().map(this::toConnectionProvider).collect(toList())));
    }

    private OutputModel toOutputModel(OutputDeclaration declaration) {
      return declaration != null
          ? new ImmutableOutputModel(declaration.getDescription(), declaration.getType(), declaration.hasDynamicType(),
                                     declaration.getModelProperties())
          : new ImmutableOutputModel("", BaseTypeBuilder.create(JAVA).voidType().build(), false, emptySet());
    }

    private ConnectionProviderModel toConnectionProvider(ConnectionProviderDeclaration declaration) {
      return fromCache(declaration,
                       () -> new ImmutableConnectionProviderModel(declaration.getName(),
                                                                  declaration.getDescription(),
                                                                  toParameterGroups(declaration.getParameterGroups()),
                                                                  declaration.getConnectionManagementType(),
                                                                  declaration.getExternalLibraryModels(),
                                                                  declaration.getDisplayModel(),
                                                                  declaration.getModelProperties()));
    }

    private List<ParameterGroupModel> toParameterGroups(List<ParameterGroupDeclaration> declarations) {
      if (declarations.isEmpty()) {
        return ImmutableList.of();
      }

      //copy the list so that executing this doesn't affect the input data
      declarations = new ArrayList<>(declarations);
      declarations.sort((left, right) -> {
        if (DEFAULT_GROUP_NAME.equals(left.getName())) {
          return -1;
        } else if (DEFAULT_GROUP_NAME.equals(right.getName())) {
          return 1;
        } else {
          return 0;
        }
      });

      return unmodifiableList(declarations.stream().map(this::toParameterGroup).collect(toList()));
    }

    private ParameterGroupModel toParameterGroup(ParameterGroupDeclaration declaration) {
      return new ImmutableParameterGroupModel(declaration.getName(),
                                              declaration.getDescription(),
                                              toParameters(declaration.getParameters()),
                                              toExclusiveParametersModels(declaration),
                                              declaration.isShowInDsl(),
                                              declaration.getDisplayModel(),
                                              declaration.getLayoutModel(),
                                              declaration.getModelProperties());
    }

    private List<ExclusiveParametersModel> toExclusiveParametersModels(ParameterGroupDeclaration groupDeclaration) {
      return unmodifiableList(groupDeclaration.getExclusiveParameters().stream()
          .map(exclusive -> new ImmutableExclusiveParametersModel(exclusive.getParameterNames(),
                                                                  exclusive.isRequiresOne()))
          .collect(toList()));
    }

    private List<ParameterModel> toParameters(List<ParameterDeclaration> declarations) {
      if (declarations.isEmpty()) {
        return ImmutableList.of();
      }

      return unmodifiableList(declarations.stream()
          .map(this::toParameter)
          .sorted(new ParameterModelComparator(false))
          .collect(toList()));
    }

    private ParameterModel toParameter(ParameterDeclaration parameter) {
      Object defaultValue = parameter.getDefaultValue();
      if (defaultValue instanceof String) {
        if (parameter.getExpressionSupport() == NOT_SUPPORTED && isExpression((String) defaultValue)) {
          throw new IllegalParameterModelDefinitionException(
                                                             format("Parameter '%s' is marked as not supporting expressions yet it"
                                                                 + " contains one as a default value. Please fix this",
                                                                    parameter.getName()));
        } else if (parameter.getExpressionSupport() == REQUIRED && !isExpression((String) defaultValue)) {
          throw new IllegalParameterModelDefinitionException(format("Parameter '%s' requires expressions yet it "
              + "contains a constant as a default value. Please fix this",
                                                                    parameter.getName()));
        }
      }

      return new ImmutableParameterModel(parameter.getName(),
                                         parameter.getDescription(),
                                         parameter.getType(),
                                         parameter.hasDynamicType(),
                                         parameter.isRequired(),
                                         parameter.isConfigOverride(),
                                         parameter.getExpressionSupport(),
                                         parameter.getDefaultValue(),
                                         parameter.getRole(),
                                         parameter.getDslConfiguration(),
                                         parameter.getDisplayModel(),
                                         parameter.getLayoutModel(),
                                         parameter.getValueProviderModel(),
                                         parameter.getElementReferences(),
                                         parameter.getModelProperties());
    }

    private List<FunctionModel> toFunctions(List<FunctionDeclaration> expressionFunctions) {
      return unmodifiableList(expressionFunctions.stream()
          .map(declaration -> new ImmutableFunctionModel(declaration.getName(),
                                                         declaration.getDescription(),
                                                         toParameterGroups(declaration.getParameterGroups()),
                                                         toOutputModel(declaration.getOutput()),
                                                         declaration.getDisplayModel(),
                                                         declaration.getModelProperties()))
          .collect(toList()));
    }
  }

  private Set<ObjectType> toExtensionTypes(Set<ObjectType> types, Set<ImportedTypeModel> importedTypes) {
    Set<String> importedTypesIds = importedTypes.stream()
        .map(ImportedTypeModel::getImportedType)
        .map(ExtensionMetadataTypeUtils::getId)
        .collect(toSet());

    return types.stream()
        .filter(t -> !importedTypesIds.contains(getId(t)))
        .collect(toCollection(LinkedHashSet::new));
  }
}
