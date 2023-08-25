/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ModelProperty;

import java.util.Set;

/**
 * Marker {@link ModelProperty} to indicate if the current {@link ExtensionModel} containing it should be expanded in the Mule
 * application.
 * <p>
 * It also works to determine that the ComponentBuildingDefinitionProvider must NOT be registered (as it's templating, it doesn't
 * make sense to register a definition provider)
 *
 * @since 1.0
 */
public class XmlExtensionModelProperty implements ModelProperty {

  public static final String NAME = "xmlExtensionModelProperty";
  private final Set<String> namespacesDependencies;

  public XmlExtensionModelProperty(Set<String> namespacesDependencies) {
    this.namespacesDependencies = namespacesDependencies;
  }

  /**
   * @return list of namespaces dependencies used in MacroExpansionModulesModel to determine the order in which the <module/>s
   *         must be macro expanded. Not null.
   */
  public Set<String> getNamespacesDependencies() {
    return namespacesDependencies;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean isPublic() {
    return true;
  }
}
