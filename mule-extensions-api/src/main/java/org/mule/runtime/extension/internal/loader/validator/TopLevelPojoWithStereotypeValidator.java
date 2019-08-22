/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.api.declaration.type.annotation.StereotypeTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeDslAnnotation;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Validates that any element (not a config) that is allowed to be defined as topLevel has stereotypes assigned.
 *
 * @since 1.3.0
 */
public class TopLevelPojoWithStereotypeValidator implements ExtensionModelValidator {

  @Override
  public void validate(ExtensionModel model, ProblemsReporter problemsReporter) {
    Map<ObjectType, ObjectType> subTypeToParentType = new HashMap<>();

    model.getSubTypes()
        .forEach(subTypeModel -> subTypeModel.getSubTypes()
            .forEach(subType -> subTypeToParentType.put(subType, subTypeModel.getBaseType())));

    final Set<ObjectType> types = model.getTypes();

    types.stream()
        .filter(ot -> ot.getAnnotation(TypeDslAnnotation.class).map(TypeDslAnnotation::allowsTopLevelDefinition).orElse(false))
        .filter(ot -> !ot.getAnnotation(StereotypeTypeAnnotation.class).isPresent()
            && (!subTypeToParentType.containsKey(ot)
                || !subTypeToParentType.get(ot).getAnnotation(StereotypeTypeAnnotation.class).isPresent()))
        .forEach(ot -> problemsReporter.addWarning(new Problem(model, "Type '" + ot
            + (subTypeToParentType.containsKey(ot)
                ? "' (or its parent type, '" + subTypeToParentType.get(ot) + "')"
                : "")
            + " is allowed as a top-level element but does not have a @Stereotype assigned. Add the @Stereotype annotation to it"
            + (subTypeToParentType.containsKey(ot) ? "(or its parent)" : "")
            + " with a meaningful value.")));
  }

}
