/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getType;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.toSubTypesMap;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.SubTypesModel;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * {@link ExtensionModelValidator} which applies to {@link ExtensionModel}s.
 * <p>
 * This validator checks that all {@link ExtensionModel Extension} global elements declarations like {@link SubTypesModel
 * Subtypes} or {@link ImportedTypeModel Imported types} are valid.
 *
 * @since 1.0
 */
public final class SubtypesModelValidator implements ExtensionModelValidator {

  @Override
  public void validate(ExtensionModel model, ProblemsReporter problemsReporter) {
    final Map<ObjectType, Set<ObjectType>> typesMapping = toSubTypesMap(model.getSubTypes());
    validateBaseTypeNotFinal(model, typesMapping, problemsReporter);
    validateSubtypesExtendOrImplementBaseType(model, typesMapping, problemsReporter);
  }

  private void validateBaseTypeNotFinal(ExtensionModel model, Map<ObjectType, Set<ObjectType>> typesMapping,
                                        ProblemsReporter problemsReporter) {
    List<String> finalBaseTypes = typesMapping.keySet().stream()
        .filter(ExtensionMetadataTypeUtils::isFinal)
        .map(type -> getId(type).orElse(null))
        .filter(type -> type != null)
        .collect(toList());

    if (!finalBaseTypes.isEmpty()) {
      problemsReporter
          .addError(new Problem(model,
                                format("All the declared SubtypesMapping should have non final base types, but [%s] are final",
                                       Arrays.toString(finalBaseTypes.toArray()))));
    }
  }

  private void validateSubtypesExtendOrImplementBaseType(ExtensionModel model,
                                                         Map<ObjectType, Set<ObjectType>> typesMapping,
                                                         ProblemsReporter problemsReporter) {
    for (Map.Entry<ObjectType, Set<ObjectType>> subtypes : typesMapping.entrySet()) {
      Optional<Class<Object>> baseType = getType(subtypes.getKey());
      if (!baseType.isPresent()) {
        continue;
      }

      List<String> invalidTypes = subtypes.getValue().stream()
          .map(ExtensionMetadataTypeUtils::getType)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .filter(s -> !baseType.get().isAssignableFrom(s))
          .map(Class::getSimpleName)
          .collect(toList());

      if (!invalidTypes.isEmpty()) {
        problemsReporter
            .addError(new Problem(model,
                                  format("All the declared Subtypes should be concrete implementations of the give baseType,"
                                      + " but [%s] are not implementations of [%s]",
                                         Arrays.toString(invalidTypes.toArray()),
                                         baseType.get().getSimpleName())));
      }
    }
  }
}
