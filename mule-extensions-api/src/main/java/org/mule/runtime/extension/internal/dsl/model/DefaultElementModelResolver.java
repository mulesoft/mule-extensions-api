/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.dsl.model;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.dsl.model.ApplicationElement;
import org.mule.runtime.api.dsl.model.ApplicationElementIdentifier;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.operation.HasOperationModels;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.meta.model.source.HasSourceModels;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.util.ExtensionWalker;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.extension.api.dsl.model.DslElementModel;
import org.mule.runtime.extension.api.dsl.model.DslElementModelResolver;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DslSyntaxResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Default implementation of a {@link DslElementModelResolver}
 *
 * @since 1.0
 */
public class DefaultElementModelResolver implements DslElementModelResolver {

  private Map<ExtensionModel, DslSyntaxResolver> resolvers = new HashMap<>();

  public DefaultElementModelResolver(Set<ExtensionModel> extensions) {

    final Map<String, ExtensionModel> extByName = extensions.stream()
        .collect(toMap(ExtensionModel::getName, e -> e));

    extensions.forEach(extensionModel -> resolvers.put(extensionModel,
                                                       DslSyntaxResolver.getDefault(extensionModel, s -> of(extByName.get(s)))));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> Optional<DslElementModel<T>> resolve(ApplicationElement applicationElement) {
    return Optional.ofNullable(createIdentifiedElement(applicationElement));
  }

  private DslElementModel createIdentifiedElement(ApplicationElement applicationElement) {

    final ApplicationElementIdentifier identifier = applicationElement.getIdentifier();

    Optional<Map.Entry<ExtensionModel, DslSyntaxResolver>> entry =
        resolvers.entrySet().stream()
            .filter(e -> e.getKey().getXmlDslModel().getNamespaceUri().equals(identifier.getNamespace()))
            .findFirst();

    if (!entry.isPresent()) {
      return null;
    }

    final ExtensionModel extension = entry.get().getKey();
    final DslSyntaxResolver dsl = entry.get().getValue();


    Reference<DslElementModel> elementModel = new Reference<>();
    new ExtensionWalker() {

      @Override
      protected void onConfiguration(ConfigurationModel model) {
        final DslElementSyntax elementDsl = dsl.resolve(model);
        getIdentifier(elementDsl).ifPresent(elementId -> {
          if (elementId.equals(identifier)) {
            DslElementModel.Builder<ConfigurationModel> element = createElementModel(model, elementDsl, applicationElement);
            addConnectionProviders(dsl, model, element, applicationElement);
            elementModel.set(element.build());
            stop();
          }
        });

      }

      @Override
      protected void onOperation(HasOperationModels owner, OperationModel model) {
        final DslElementSyntax elementDsl = dsl.resolve(model);
        getIdentifier(elementDsl).ifPresent(elementId -> {
          if (elementId.equals(identifier)) {
            elementModel.set(createElementModel(model, elementDsl, applicationElement).build());
            stop();
          }
        });
      }

      @Override
      protected void onSource(HasSourceModels owner, SourceModel model) {
        final DslElementSyntax elementDsl = dsl.resolve(model);
        getIdentifier(elementDsl).ifPresent(elementId -> {
          if (elementId.equals(identifier)) {
            elementModel.set(createElementModel(model, elementDsl, applicationElement).build());
            stop();
          }
        });
      }

    }.walk(extension);

    if (elementModel.get() == null) {
      resolveBasedOnTypes(extension, dsl, identifier)
          .ifPresent(elementModel::set);
    }

    return elementModel.get();
  }

  private Optional<DslElementModel<ObjectType>> resolveBasedOnTypes(ExtensionModel extension, DslSyntaxResolver dsl,
                                                                    ApplicationElementIdentifier identifier) {
    return extension.getTypes().stream()
        .map(type -> {
          Optional<DslElementSyntax> typeDsl = dsl.resolve(type);
          if (typeDsl.isPresent()) {
            Optional<ApplicationElementIdentifier> elementIdentifier = getIdentifier(typeDsl.get());
            if (elementIdentifier.isPresent() && elementIdentifier.get().equals(identifier)) {
              return DslElementModel.<ObjectType>builder()
                  .withModel(type)
                  .withDsl(typeDsl.get())
                  .build();
            }
          }
          return null;
        }).filter(Objects::nonNull)
        .findFirst();
  }

  private DslElementModel.Builder<ConfigurationModel> addConnectionProviders(DslSyntaxResolver dsl, ConfigurationModel model,
                                                                             DslElementModel.Builder<ConfigurationModel> element,
                                                                             ApplicationElement applicationElement) {
    for (ConnectionProviderModel provider : model.getConnectionProviders()) {
      DslElementSyntax providerDsl = dsl.resolve(provider);
      ApplicationElementIdentifier identifier = getIdentifier(providerDsl).orElse(null);
      Optional<ApplicationElement> providerComponent =
          applicationElement.getInnerComponents().stream()
              .filter(c -> c.getIdentifier().equals(identifier))
              .findFirst();

      if (providerComponent.isPresent()) {
        element.containing(createElementModel(provider, providerDsl, providerComponent.get()).build());
        break;
      }
    }

    return element;
  }

  private <T extends ParameterizedModel> DslElementModel.Builder<T> createElementModel(T model, DslElementSyntax elementDsl,
                                                                                       ApplicationElement applicationElement) {
    DslElementModel.Builder<T> builder = DslElementModel.builder();
    builder.withModel(model)
        .withDsl(elementDsl)
        .withElement(applicationElement);

    populateParameterizedElements(model, elementDsl, builder, applicationElement);
    return builder;
  }

  private void populateParameterizedElements(ParameterizedModel model, DslElementSyntax elementDsl,
                                             DslElementModel.Builder builder, ApplicationElement applicationElement) {

    Map<ApplicationElementIdentifier, ApplicationElement> innerComponents = applicationElement.getInnerComponents().stream()
        .collect(toMap(ApplicationElement::getIdentifier, e -> e));

    Map<String, String> parameters = applicationElement.getParameters();

    List<ParameterModel> inlineGroupedParameters = model.getParameterGroupModels().stream()
        .filter(ParameterGroupModel::isShowInDsl)
        .peek(group -> addInlineGroup(elementDsl, innerComponents, parameters, group))
        .flatMap(g -> g.getParameterModels().stream())
        .collect(toList());

    model.getAllParameterModels().stream()
        .filter(p -> !inlineGroupedParameters.contains(p))
        .forEach(p -> addElementParameter(innerComponents, parameters, elementDsl, builder, p));
  }

  private void addInlineGroup(DslElementSyntax elementDsl, Map<ApplicationElementIdentifier, ApplicationElement> innerComponents,
                              Map<String, String> parameters, ParameterGroupModel group) {
    elementDsl.getChild(group.getName())
        .ifPresent(groupDsl -> {
          ApplicationElement groupComponent = getIdentifier(groupDsl).map(innerComponents::get).orElse(null);

          if (groupComponent != null) {
            DslElementModel.Builder<ParameterGroupModel> groupElementBuilder = DslElementModel.builder();
            groupElementBuilder.withModel(group).withDsl(groupDsl);

            group.getParameterModels()
                .forEach(p -> addElementParameter(innerComponents, parameters, groupDsl, groupElementBuilder, p));
          }
        });
  }

  private void addElementParameter(Map<ApplicationElementIdentifier, ApplicationElement> innerComponents,
                                   Map<String, String> parameters,
                                   DslElementSyntax groupDsl, DslElementModel.Builder<ParameterGroupModel> groupElementBuilder,
                                   ParameterModel p) {
    groupDsl.getContainedElement(p.getName())
        .ifPresent(pDsl -> {
          ApplicationElement paramComponent = getIdentifier(pDsl).map(innerComponents::get).orElse(null);

          if (!pDsl.isWrapped()) {
            String paramValue = pDsl.supportsAttributeDeclaration() ? parameters.get(pDsl.getAttributeName()) : null;
            if (paramComponent != null || paramValue != null) {
              DslElementModel.Builder<ParameterModel> paramElement =
                  DslElementModel.<ParameterModel>builder().withModel(p).withDsl(pDsl);

              if (paramComponent != null && paramComponent.getInnerComponents().size() > 0) {
                paramComponent.getInnerComponents().forEach(c -> this.resolve(c).ifPresent(paramElement::containing));
              }

              groupElementBuilder.containing(paramElement.build());
            }
          } else {
            resolveWrappedElement(groupElementBuilder, p, pDsl, paramComponent);
          }

        });
  }

  private void resolveWrappedElement(DslElementModel.Builder<ParameterGroupModel> groupElementBuilder, ParameterModel p,
                                     DslElementSyntax pDsl, ApplicationElement paramComponent) {
    if (paramComponent != null) {
      DslElementModel.Builder<ParameterModel> paramElement =
          DslElementModel.<ParameterModel>builder().withModel(p).withDsl(pDsl);

      if (paramComponent.getInnerComponents().size() > 0) {
        ApplicationElement wrappedComponent = paramComponent.getInnerComponents().get(0);
        this.resolve(wrappedComponent).ifPresent(paramElement::containing);
      }

      groupElementBuilder.containing(paramElement.build());
    }
  }

  private Optional<ApplicationElementIdentifier> getIdentifier(DslElementSyntax dsl) {
    if (dsl.supportsTopLevelDeclaration() || dsl.supportsChildDeclaration()) {
      return Optional.of(ApplicationElementIdentifier.builder()
          .withName(dsl.getElementName())
          .withNamespace(dsl.getNamespaceUri())
          .build());
    }

    return empty();
  }
}
