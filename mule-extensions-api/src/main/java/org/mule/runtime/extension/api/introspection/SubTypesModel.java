/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection;


import static java.util.Collections.unmodifiableSet;
import org.mule.metadata.api.model.MetadataType;

import java.util.Set;

/**
 * A model which describes that a given {@link #getBaseType() base type} is extended by a
 * {@link Set} of {@link #getSubTypes() sub-types}. In all cases, the types are described through
 * the {@link MetadataType} model.
 *
 * @since 1.0
 */
public final class SubTypesModel {

  private final MetadataType baseType;
  private final Set<MetadataType> subTypes;

  public SubTypesModel(MetadataType baseType, Set<MetadataType> subTypes) {
    this.baseType = baseType;
    this.subTypes = unmodifiableSet(subTypes);
  }

  public MetadataType getBaseType() {
    return baseType;
  }

  public Set<MetadataType> getSubTypes() {
    return subTypes;
  }
}
