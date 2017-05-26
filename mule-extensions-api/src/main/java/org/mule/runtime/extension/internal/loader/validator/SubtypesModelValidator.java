/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getAlias;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.toSubTypesMap;
import static org.mule.runtime.extension.api.util.NameUtils.getTopLevelTypeName;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.SubTypesModel;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
    validateSubtypesNameClashing(model, typesMapping, problemsReporter);
  }

  private void validateBaseTypeNotFinal(ExtensionModel model, Map<ObjectType, Set<ObjectType>> typesMapping,
                                        ProblemsReporter problemsReporter) {
    List<String> finalBaseTypes = typesMapping.keySet().stream()
        .filter(ExtensionMetadataTypeUtils::isFinal)
        .map(ExtensionMetadataTypeUtils::getId)
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
      Optional<Class<?>> baseType = ExtensionMetadataTypeUtils.getType(subtypes.getKey());
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

  private void validateSubtypesNameClashing(ExtensionModel model, Map<ObjectType, Set<ObjectType>> typesMapping,
                                            ProblemsReporter problemsReporter) {

    ImmutableList<MetadataType> mappedTypes = ImmutableList.<MetadataType>builder()
        .addAll(typesMapping.keySet())
        .addAll(typesMapping.values().stream().flatMap(Collection::stream).collect(toList()))
        .build();

    Map<String, MetadataType> typesByName = new HashMap<>();
    for (MetadataType type : mappedTypes) {
      MetadataType previousType = typesByName.put(getTopLevelTypeName(type), type);
      if (previousType != null && !previousType.equals(type)) {
        problemsReporter.addError(new Problem(model, format(
                                                            "Subtypes mapped Type [%s] with alias [%s] in extension [%s] should have a"
                                                                + " different alias name than the previous mapped type [%s]",
                                                            getAlias(type), getTopLevelTypeName(type), model.getName(),
                                                            getAlias(previousType))));
      }
    }
  }
}
