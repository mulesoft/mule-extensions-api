/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;
import static org.mule.metadata.api.utils.MetadataTypeUtils.getLocalPart;
import static org.mule.metadata.java.api.utils.JavaTypeUtils.getType;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.isContent;
import static org.mule.runtime.extension.api.util.NameUtils.getComponentModelTypeName;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.runtime.api.meta.DescribedObject;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.SubTypesModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.connection.HasConnectionProviderModels;
import org.mule.runtime.api.meta.model.operation.HasOperationModels;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.meta.model.source.HasSourceModels;
import org.mule.runtime.api.meta.model.source.SourceCallbackModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.util.ExtensionWalker;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DslSyntaxResolver;
import org.mule.runtime.extension.api.dsl.syntax.resolver.SingleExtensionImportTypesStrategy;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils;
import org.mule.runtime.extension.api.util.ExtensionModelUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * Validates names clashes in the model by comparing:
 * <ul>
 * <li>The {@link NamedObject#getName()} value of all the {@link ConfigurationModel}, {@link OperationModel} and
 * {@link ConnectionProviderModel}</li>
 * <li>Makes sure that there no two {@link ParameterModel}s with the same name but different types, for those which represent an
 * object</li>
 * <li>Makes sure that no {@link ConfigurationModel}, {@link OperationModel} or {@link ConnectionProviderModel} have parameters
 * with repeated name</li>
 * </ul>
 *
 * @since 1.0
 */
public final class NameClashModelValidator implements ExtensionModelValidator {

  @Override
  public void validate(ExtensionModel model, ProblemsReporter problemsReporter) {
    new ValidationDelegate(model, problemsReporter).validate();
  }

  private class ValidationDelegate {

    public static final String SINGULARIZED_CLASH_MESSAGE =
        "Extension '%s' contains %d parameters that clash when singularized. %s";
    public static final String NAME_CLASH_MESSAGE =
        "%s '%s' contains parameter '%s' that when transformed into DSL language clashes with parameter '%s'";

    private final ExtensionModel extensionModel;
    private final Set<DescribedReference<NamedObject>> namedObjects = new HashSet<>();
    private final Map<String, DescribedParameter> singularizedObjects = new HashMap<>();
    private final Multimap<String, Element> elements = LinkedListMultimap.create();
    private final List<ParameterReference> contentParameters = new LinkedList<>();
    private final List<ParameterReference> nonContentParameters = new LinkedList<>();
    private final DslSyntaxResolver dslSyntaxResolver;
    private final ProblemsReporter problemsReporter;

    public ValidationDelegate(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
      this.extensionModel = extensionModel;
      this.problemsReporter = problemsReporter;
      this.dslSyntaxResolver = DslSyntaxResolver.getDefault(extensionModel,
                                                            new SingleExtensionImportTypesStrategy());
    }

    private void validate() {
      new ExtensionWalker() {

        @Override
        public void onConfiguration(ConfigurationModel model) {
          defaultValidation(model);
        }

        @Override
        public void onConnectionProvider(HasConnectionProviderModels owner, ConnectionProviderModel model) {
          defaultValidation(model);
        }

        @Override
        public void onOperation(HasOperationModels owner, OperationModel model) {
          validateOperation(model);
          registerNamedObject(model);
          validateSingularizedNameClash(model, dslSyntaxResolver.resolve(model).getElementName());
          splitParametersByContent(model);
        }

        @Override
        public void onSource(HasSourceModels owner, SourceModel model) {
          validateCallbackNames(model.getSuccessCallback(), model);
          validateCallbackNames(model.getErrorCallback(), model);
          defaultValidation(model);
          splitParametersByContent(model);
        }

        @Override
        public void onParameter(ParameterizedModel owner, ParameterGroupModel groupModel, ParameterModel model) {
          validateTopLevelParameter(model, owner);
        }

        private void defaultValidation(ParameterizedModel model) {
          validateNamesWithinGroups(model);
          registerNamedObject(model);
          validateSingularizedNameClash(model, dslSyntaxResolver.resolve(model).getElementName());
        }

        private void registerNamedObject(ParameterizedModel named) {
          namedObjects.add(new DescribedReference<>(named, dslSyntaxResolver.resolve(named).getElementName()));
        }

        private void validateCallbackNames(Optional<SourceCallbackModel> sourceCallback, SourceModel model) {
          sourceCallback.ifPresent(cb -> validateNamesWithinGroups(cb, concat(model.getParameterGroupModels().stream(),
                                                                              cb.getParameterGroupModels().stream())
                                                                                  .collect(toList())));
        }
      }.walk(extensionModel);

      validateSubtypes(extensionModel.getSubTypes());
      validateSingularizeNameClashesWithTopLevels();
      validateSingularizeNameClashesWithNamedObjects();
      validateNameClashes(namedObjects, elements.values(),
                          elements.values().stream().filter(e -> (e instanceof TopLevelParameter))
                              .map(e -> new TypedTopLevelParameter((TopLevelParameter) e)).collect(toSet()));

      validateContentNamesMatchType(extensionModel, problemsReporter);
      validateContentClashes(extensionModel, problemsReporter);
    }

    private void validateSubtypes(Set<SubTypesModel> subTypes) {
      subTypes.forEach(subTypesModel -> {
        subTypesModel.getSubTypes()
            .forEach(type -> ExtensionMetadataTypeUtils.getType(type)
                .ifPresent(objectElementType -> dslSyntaxResolver.resolve(type)
                    .filter(dsl -> dsl.supportsChildDeclaration() || dsl.supportsTopLevelDeclaration())
                    .ifPresent(subtypeDsl -> {

                      Collection<Element> sameNameElements = this.elements.get(subtypeDsl.getElementName());
                      if (sameNameElements != null) {
                        sameNameElements.stream()
                            .filter(element -> !element.type.equals(objectElementType))
                            .findAny()
                            .ifPresent(tp -> problemsReporter
                                .addError(new Problem(extensionModel,
                                                      format(
                                                             "An extension subtype '%s' of complex type '%s' is defined. However, there is already an element with the same name "
                                                                 + "but with a different type (%s). Complex parameter of different types cannot have the same name.",
                                                             subtypeDsl.getElementName(), objectElementType,
                                                             tp.toString()))));
                      } else {
                        elements.put(subtypeDsl.getElementName(), new Element(subtypeDsl.getElementName(), objectElementType));
                      }

                      validateType(type, objectElementType, subtypeDsl);
                    })));
      });
    }

    private void validateType(MetadataType type, Class<?> parameterType, DslElementSyntax elementSyntax) {
      type.accept(new MetadataTypeVisitor() {

        @Override
        public void visitObject(ObjectType objectType) {
          objectType.getFields().forEach(field -> {
            String fieldName = getLocalPart(field);
            elementSyntax.getChild(fieldName)
                .ifPresent(child -> validateInnerFields(fieldName, field.getValue(), parameterType.getSimpleName(), child));
          });
        }

        @Override
        public void visitArrayType(ArrayType arrayType) {
          arrayType.getType().accept(this);
        }
      });
    }

    private void splitParametersByContent(ParameterizedModel model) {
      model.getAllParameterModels().forEach(p -> {
        ParameterReference reference = new ParameterReference(p, model, dslSyntaxResolver.resolve(p));
        if (ExtensionModelUtils.isContent(p)) {
          contentParameters.add(reference);
        } else {
          nonContentParameters.add(reference);
        }
      });
    }

    private void validateOperation(OperationModel operation) {
      validateNamesWithinGroups(operation);
      String operationName = dslSyntaxResolver.resolve(operation).getElementName();
      operation.getAllParameterModels().stream().map(parameterModel -> dslSyntaxResolver.resolve(parameterModel))
          .filter(DslElementSyntax::supportsChildDeclaration)
          .forEach(parameterElement -> {

            validateClash(operationName,
                          parameterElement.getElementName(),
                          getComponentModelTypeName(operation), "argument");

            namedObjects.forEach(namedObject -> validateClash(namedObject.getName(), parameterElement.getElementName(),
                                                              namedObject.getDescription(),
                                                              format("%s named %s with an argument", operationName,
                                                                     getComponentModelTypeName(operation))));
          });
    }

    private void validateParameterNames(List<ParameterModel> parameterizedModel, String modelTypeName, String modelName) {
      Set<String> repeatedParameters = collectRepeatedNames(parameterizedModel);
      if (!repeatedParameters.isEmpty()) {
        problemsReporter.addError(new Problem(extensionModel, format("The %s '%s' has parameters with repeated names. "
            + "Offending parameters are: [%s]",
                                                                     modelTypeName,
                                                                     modelName, Joiner.on(",").join(repeatedParameters))));
      }
    }

    private void validateTopLevelParameter(ParameterModel parameter, ParameterizedModel owner) {
      validateTopLevelParameter(parameter, owner, dslSyntaxResolver.resolve(parameter));
    }

    private void validateTopLevelParameter(ParameterModel parameter, ParameterizedModel owner,
                                           DslElementSyntax parameterElement) {
      if (parameterElement.supportsChildDeclaration()) {
        final Class<?> parameterType = ExtensionMetadataTypeUtils.getType(parameter.getType()).orElse(null);
        if (parameterType == null) {
          return;
        }

        final String ownerName = owner.getName();
        final String ownerType = getComponentModelTypeName(owner);

        Collection<Element> foundParameters = elements.get(parameterElement.getElementName());

        if (foundParameters.isEmpty()) {
          elements.put(parameterElement.getElementName(), new TopLevelParameter(parameter, ownerName, ownerType));

        } else {
          foundParameters.stream()
              .filter(topLevelParameter -> !topLevelParameter.type.equals(parameterType))
              .findAny().ifPresent(
                                   tp -> {
                                     problemsReporter.addError(new Problem(extensionModel, format(
                                                                                                  "An %s of name '%s' contains parameter '%s' of complex type '%s'. However, there is already an element with the same name "
                                                                                                      + "but with a different type (%s). Complex parameter of different types cannot have the same name.",
                                                                                                  ownerType, ownerName,
                                                                                                  parameterElement
                                                                                                      .getElementName(),
                                                                                                  parameterType,
                                                                                                  tp.toString())));
                                   });
        }

        validateType(parameter.getType(), parameterType, parameterElement);
      }
    }

    private void validateInnerFields(String name, MetadataType fieldType, String parentType, DslElementSyntax parameterElement) {
      if (parameterElement.supportsChildDeclaration()) {
        ExtensionMetadataTypeUtils.getType(fieldType).ifPresent(parameterType -> {
          Collection<Element> foundParameters = elements.get(parameterElement.getElementName());

          if (foundParameters.isEmpty()) {
            elements.put(parameterElement.getElementName(), new Element(name, parameterType));
          } else {
            foundParameters.stream()
                .filter(topLevelParameter -> !topLevelParameter.type.equals(parameterType))
                .findAny().ifPresent(
                                     tp -> problemsReporter.addError(new Problem(extensionModel, format(
                                                                                                        "Object of type %s contains a field named '%s' of complex type '%s'. However, there is already an element with the same name "
                                                                                                            + "but with a different type (%s). Complex parameter of different types cannot have the same name.",
                                                                                                        parentType,
                                                                                                        parameterElement
                                                                                                            .getElementName(),
                                                                                                        parameterType,
                                                                                                        tp.toString()))));

          }

          validateType(fieldType, parameterType, parameterElement);
        });
      }
    }

    private Set<String> collectRepeatedNames(List<? extends NamedObject> namedObject) {
      Set<String> names = new HashSet<>();
      return namedObject.stream().map(parameter -> dslSyntaxResolver.resolve(parameter).getElementName())
          .filter(parameter -> !names.add(parameter)).collect(toSet());
    }

    private void validateNameClashes(Collection<? extends NamedObject>... collections) {
      Multimap<String, NamedObject> names = LinkedListMultimap.create();
      stream(collections).flatMap(Collection::stream)
          .forEach(named -> names.put(dslSyntaxResolver.resolve(named).getElementName(), named));
      validateNameClashBetweenElements(names);
    }

    private void validateNameClashBetweenElements(Multimap<String, NamedObject> names) {
      names.asMap().entrySet().forEach(entry -> {
        List<NamedObject> values = (List<NamedObject>) entry.getValue();
        if (values.size() > 1) {
          Set<String> offendingTypes = values.stream().map(NamedObject::getName).collect(toSet());
          StringBuilder errorMessage =
              new StringBuilder(format("Extension '%s' contains %d components ", extensionModel.getName(), values.size()));

          final int top = offendingTypes.size() - 1;
          int i = 0;
          for (String offender : offendingTypes) {
            errorMessage.append(format("'%s'", offender));

            if (i + 1 == top) {
              errorMessage.append(" and ");
            } else if (i != top) {
              errorMessage.append(", ");
            }

            i++;
          }

          errorMessage.append(format(" which it's transformed DSL name is '%s'. DSL Names should be unique", entry.getKey()));
          problemsReporter.addError(new Problem(extensionModel, errorMessage.toString()));
        }
      });
    }

    private void validateSingularizeNameClashesWithTopLevels() {
      Map<String, Collection<Element>> singularClashes = elements.keySet().stream()
          .filter(k -> singularizedObjects.containsKey(k) && !elements.get(k).isEmpty())
          .collect(toMap(identity(), elements::get));

      if (!singularClashes.isEmpty()) {
        List<String> errorMessages = new ArrayList<>();
        singularClashes.entrySet().forEach(e -> {
          DescribedParameter reference = singularizedObjects.get(e.getKey());
          e.getValue().stream()
              .filter(tp -> !Objects.equals(getType(reference.getDescribedType()), tp.type))
              .forEach(tp -> errorMessages
                  .add(format(NAME_CLASH_MESSAGE, reference.parent.getDescription(), reference.parent.getName(),
                              reference.getName(), tp.toString())));
        });

        if (!errorMessages.isEmpty()) {
          problemsReporter.addError(new Problem(extensionModel, format(SINGULARIZED_CLASH_MESSAGE,
                                                                       extensionModel.getName(), singularClashes.size(),
                                                                       errorMessages.stream().collect(joining(", ")))));
        }
      }
    }


    private void validateSingularizeNameClashesWithNamedObjects() {
      Set<DescribedReference<NamedObject>> singularClashes = namedObjects.stream()
          .filter(k -> singularizedObjects.containsKey(k.getName()))
          .collect(toSet());

      if (!singularClashes.isEmpty()) {
        List<String> errorMessages = new ArrayList<>();
        singularClashes.forEach(namedObject -> {
          DescribedParameter reference = singularizedObjects.get(namedObject.getName());
          errorMessages.add(format(NAME_CLASH_MESSAGE, reference.parent.getDescription(), reference.parent.getName(),
                                   reference.getName(), namedObject.getDescription()));
        });

        if (!errorMessages.isEmpty()) {
          problemsReporter.addError(new Problem(extensionModel, format(SINGULARIZED_CLASH_MESSAGE,
                                                                       extensionModel.getName(), singularClashes.size(),
                                                                       errorMessages.stream().collect(joining(", ")))));
        }
      }
    }

    private void validateClash(String existingNamingModel, String newNamingModel, String typeOfExistingNamingModel,
                               String typeOfNewNamingModel) {
      if (existingNamingModel != null && existingNamingModel.equalsIgnoreCase(newNamingModel)) {
        problemsReporter
            .addError(new Problem(extensionModel, format("Extension '%s' has a %s named '%s' and an %s type named equally.",
                                                         extensionModel.getName(), typeOfExistingNamingModel, existingNamingModel,
                                                         typeOfNewNamingModel)));
      }
    }

    private void validateContentNamesMatchType(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
      Map<String, List<ParameterReference>> clashingsByTagName = new HashMap<>();
      contentParameters
          .forEach(param -> clashingsByTagName.computeIfAbsent(param.dsl.getElementName(), k -> {
            List<ParameterReference> others = contentParameters.stream()
                .filter(other -> param.dsl.getElementName().equals(other.dsl.getElementName()) && !param.type.equals(other.type))
                .collect(toList());
            if (!others.isEmpty()) {
              others.add(param);
            }
            return others;
          }));

      clashingsByTagName.forEach((tag, invalidParams) -> {
        if (!invalidParams.isEmpty()) {
          String msg =
              format(
                     "Parameters with name [%s] declared in [%s] with tag name [%s] are declared as @%s but have different types [%s]",
                     invalidParams.get(0).model.getName(),
                     invalidParams.stream().map(p -> p.owner.getName()).collect(joining(", ")),
                     tag,
                     Content.class.getSimpleName(),
                     invalidParams.stream()
                         .map(p -> getId(p.type).orElse(""))
                         .filter(StringUtils::isBlank)
                         .collect(joining(", ")));
          problemsReporter.addError(new Problem(extensionModel, msg));
        }
      });
    }

    private void validateContentClashes(ExtensionModel extensionModel, ProblemsReporter problemsReporter) {
      contentParameters.forEach(contentParam -> {
        List<ParameterReference> clashes = nonContentParameters.stream()
            .filter(p -> p.dsl.getElementName().equals(contentParam.dsl.getElementName()))
            .collect(toList());

        if (!clashes.isEmpty()) {
          String msg =
              format("Parameter '%s' is declared as @%s in component '%s' but is also declared as non @%s in component(s) [%s]",
                     contentParam.model.getName(),
                     Content.class.getSimpleName(),
                     contentParam.owner.getName(),
                     Content.class.getSimpleName(),
                     clashes.stream().map(p -> p.owner.getName()).collect(joining(", ", "'", "'")));

          problemsReporter.addError(new Problem(extensionModel, msg));
        }
      });
    }

    private void validateNamesWithinGroups(ParameterizedModel model) {
      validateNamesWithinGroups(model, model.getParameterGroupModels());
    }

    private void validateNamesWithinGroups(ParameterizedModel model, List<ParameterGroupModel> groups) {
      String componentTypeName = getComponentModelTypeName(model);
      MultiMap<NamedObject, ParameterModel> parametersMap = new MultiMap<>();
      groups.forEach(group -> {
        NamedObject parameterContainer = group.isShowInDsl() ? group : model;
        group.getParameterModels()
            .forEach(parameter -> parametersMap.put(parameterContainer, parameter));
      });
      parametersMap.keySet()
          .forEach(parameterContainer -> validateParameterNames(parametersMap.getAll(parameterContainer), componentTypeName,
                                                                model.getName()));
    }

    private void validateSingularizedNameClash(ParameterizedModel model, String modelElementName) {
      List<ParameterModel> parameters = filterContentParameters(model.getAllParameterModels());
      parameters.forEach(parameter -> parameter.getType().accept(new MetadataTypeVisitor() {

        @Override
        public void visitObject(ObjectType objectType) {
          objectType.getOpenRestriction().ifPresent(this::validateSingularizedChildName);
        }

        @Override
        public void visitArrayType(ArrayType arrayType) {
          validateSingularizedChildName(arrayType.getType());
        }

        private void validateSingularizedChildName(MetadataType type) {
          DslElementSyntax parameterSyntax = dslSyntaxResolver.resolve(parameter);
          String describedReference = new DescribedReference<>(model, modelElementName).getDescription();

          parameterSyntax.getGeneric(type).filter(t -> parameterSyntax.supportsChildDeclaration())
              .ifPresent(childSyntax -> {
                singularizedObjects.put(childSyntax.getElementName(),
                                        new DescribedParameter(parameter, parameterSyntax.getElementName(),
                                                               model, modelElementName, type));

                parameters.stream()
                    .filter(p -> Objects.equals(dslSyntaxResolver.resolve(p).getElementName(),
                                                childSyntax.getElementName()))
                    .filter(p -> !Objects.equals(getType(p.getType()), getType(type))).findAny()
                    .ifPresent(clashParam -> {
                      problemsReporter.addError(new Problem(extensionModel, format(
                                                                                   "Extension '%s' defines an %s of name '%s' which contains a parameter '%s' that when transformed to"
                                                                                       + "DSL language clashes with another parameter '%s' in the same %s",
                                                                                   extensionModel
                                                                                       .getName(),
                                                                                   describedReference,
                                                                                   model.getName(),
                                                                                   parameter.getName(),
                                                                                   clashParam.getName(),
                                                                                   describedReference)));
                    });
              });
        }
      }));
    }
  }


  private class Element implements NamedObject, DescribedObject {

    protected final String name;
    protected final Class<?> type;

    private Element(String name, Class<?> type) {
      this.name = name;
      this.type = type;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getDescription() {
      return "top level parameter";
    }

    @Override
    public String toString() {
      return format("element %s of type %s", name, type.getSimpleName());
    }
  }


  private class TopLevelParameter extends Element {

    public TopLevelParameter(ParameterModel parameterModel, String owner, String ownerType) {
      super(parameterModel.getName(), getType(parameterModel.getType()));
      this.parameterModel = parameterModel;
      this.owner = owner;
      this.ownerType = ownerType;
    }

    protected final ParameterModel parameterModel;
    protected final String owner;
    protected final String ownerType;

    @Override
    public String toString() {
      return format("element named '%s' of type '%s' defined in %s '%s'", name, type.getSimpleName(), ownerType, owner);
    }
  }


  private class TypedTopLevelParameter extends TopLevelParameter {

    public TypedTopLevelParameter(TopLevelParameter parameter) {
      super(parameter.parameterModel, parameter.owner, parameter.ownerType);
    }

    @Override
    public String getName() {
      return type.getName();
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof TypedTopLevelParameter && type.equals(((TypedTopLevelParameter) obj).type);

    }

    @Override
    public int hashCode() {
      return type.hashCode();
    }
  }


  private class DescribedReference<T extends NamedObject> extends Reference<T> implements NamedObject, DescribedObject {

    private final String elementName;

    private DescribedReference(T value, String elementName) {
      super(value);
      this.elementName = elementName;
    }

    @Override
    public String getName() {
      return elementName;
    }

    @Override
    public String getDescription() {
      NamedObject value = get();
      if (value instanceof ConfigurationModel) {
        return "configuration";
      } else if (value instanceof OperationModel) {
        return "operation";
      } else if (value instanceof SourceModel) {
        return "message source";
      } else if (value instanceof ConnectionProviderModel) {
        return "connection provider";
      }

      return "";
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof DescribedReference && super.equals(obj);

    }

    @Override
    public int hashCode() {
      return super.hashCode();
    }
  }

  private List<ParameterModel> filterContentParameters(List<ParameterModel> parameters) {
    return parameters.stream().filter(p -> !isContent(p)).collect(toList());
  }


  private class DescribedParameter extends DescribedReference<ParameterModel> {

    private DescribedReference<? extends NamedObject> parent;

    private MetadataType describedType;

    private DescribedParameter(ParameterModel value, String elementName, ParameterizedModel parent, String parentElementName,
                               MetadataType describedType) {
      super(value, elementName);
      this.parent = new DescribedReference<>(parent, parentElementName);
      this.describedType = describedType;
    }

    public MetadataType getDescribedType() {
      return describedType;
    }
  }


  private static class ParameterReference {

    private final ParameterModel model;
    private final MetadataType type;
    private final ParameterizedModel owner;
    private final DslElementSyntax dsl;


    public ParameterReference(ParameterModel model, ParameterizedModel owner, DslElementSyntax dsl) {
      this.model = model;
      this.type = model.getType();
      this.owner = owner;
      this.dsl = dsl;
    }

  }

}
