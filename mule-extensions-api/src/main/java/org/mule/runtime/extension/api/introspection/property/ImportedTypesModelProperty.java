/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.property;


import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.ModelProperty;

import java.util.Collections;
import java.util.Map;

/**
 * An immutable model property which specifies the {@link MetadataType} used by
 * the {@link ExtensionModel} that has to be imported from a different {@link ExtensionModel}
 *
 * @since 1.0
 */
public final class ImportedTypesModelProperty implements ModelProperty {

  private final Map<MetadataType, String> importedTypes;

  /**
   * Creates a new instance containing all the imported {@link MetadataType} declarations
   * along with the {@link MetadataType} from where they have to be imported from
   *
   * @param importedTypes
   */
  public ImportedTypesModelProperty(Map<MetadataType, String> importedTypes) {
    this.importedTypes = importedTypes;
  }

  /**
   * @return a {@link Map} containing the {@code name} of the {@link ExtensionModel} from where the {@code key}
   * {@link MetadataType} was imported from
   */
  public Map<MetadataType, String> getImportedTypes() {
    return Collections.unmodifiableMap(importedTypes);
  }

  /**
   * {@inheritDoc}
   *
   * @return {@code importedTypesProperty}
   */
  @Override
  public String getName() {
    return "importedTypesProperty";
  }

  /**
   * {@inheritDoc}
   *
   * @return {@code true}
   */
  @Override
  public boolean isExternalizable() {
    return true;
  }
}
