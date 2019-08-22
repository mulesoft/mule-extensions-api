/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.validator;

import static java.util.Optional.empty;

import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.api.declaration.type.annotation.StereotypeTypeAnnotation;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;
import org.mule.runtime.extension.api.loader.Problem;
import org.mule.runtime.extension.api.loader.ProblemsReporter;
import org.mule.runtime.extension.api.stereotype.ImplicitStereotypeDefinition;
import org.mule.runtime.extension.api.stereotype.StereotypeDefinition;

import java.util.Optional;

/**
 * Validates that when stereotypes are defined in a type and a subtype, the stereotype in the subtype is a descendant of the one
 * from the supertype.
 *
 * @since 1.3.0
 */
public class StereotypesHierarchyConsistentValidator implements ExtensionModelValidator {

  @Override
  public void validate(ExtensionModel model, ProblemsReporter problemsReporter) {
    model.getSubTypes().forEach(stm -> stm.getBaseType().getAnnotation(StereotypeTypeAnnotation.class)
        .flatMap(stAnn -> stAnn.getDefinitionClasses().stream().findFirst())
        .flatMap(this::instantiateSteretypeDefinition)
        .ifPresent(baseStereotype -> stm.getSubTypes()
            .forEach(subType -> subType.getAnnotation(StereotypeTypeAnnotation.class).ifPresent(stAnn -> {
              if (stAnn.getDefinitionClasses().stream()
                  .map(this::instantiateSteretypeDefinition)
                  .anyMatch(s -> s.map(sd -> !isAncestor(sd, baseStereotype)).orElse(false))) {
                problemsReporter.addWarning(new Problem(model, "Type '" + subType
                    + "' has a @Stereotype which is not a descendant from the @Stereotype defined in a superclass/superinterface '"
                    + stm.getBaseType() + "'"));
              }
            }))));
  }

  private Optional<StereotypeDefinition> instantiateSteretypeDefinition(Class<? extends StereotypeDefinition> def) {
    if (def.equals(ImplicitStereotypeDefinition.class)) {
      return empty();
    } else {
      try {
        return Optional.of(def.newInstance());
      } catch (InstantiationException | IllegalAccessException e) {
        throw new MuleRuntimeException(e);
      }
    }
  }

  private boolean isAncestor(StereotypeDefinition defA, StereotypeDefinition defB) {
    if (defA.getName().equals(defB.getName())) {
      return true;
    }

    return defA.getParent().map(defParent -> isAncestor(defParent, defB)).orElse(false);
  }
}
