/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.component.value;

import static java.util.Optional.ofNullable;

import org.mule.runtime.api.util.Pair;

import java.util.Optional;


/**
 * Represents a value that holds extra information about that value
 *
 * @since 1.5
 */
public class EnrichedValue {

  private final Object value;

  /**
   * The typeInformation contains the extensionIdentifier which indicates the extension in which the
   * {@link org.mule.metadata.api.model.MetadataType} is defined and the typeIdentifier which is either its typeId or typeAlias
   */
  private final Pair<String, String> typeInformation;

  public EnrichedValue(Object value, Pair<String, String> typeInformation) {
    this.value = value;
    this.typeInformation = typeInformation;
  }

  public Object getValue() {
    return value;
  }

  public Optional<Pair<String, String>> getTypeInformation() {
    return ofNullable(typeInformation);
  }

}
