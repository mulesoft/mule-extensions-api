/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.stereotype;

import static java.util.Objects.hash;
import static java.util.Optional.ofNullable;

import java.util.Optional;

/**
 * A special kind of stereotype definition, to be automatically added to top level pojos and interfaces form an extension that do
 * not already have an associated stereotype.
 * <p>
 * The idea behind this is to make all referenceable types have a stereotype, so it is the primary method of managing references
 * between objects defined in the DSL.
 * <p>
 * The namespace is not provided, since it will be set when this object is processed, as explained in
 * {@link StereotypeDefinition#getNamespace()}.
 *
 * @since 1.3
 */
public class ImplicitStereotypeDefinition implements StereotypeDefinition {

  private final String name;
  private final StereotypeDefinition parent;

  public ImplicitStereotypeDefinition(String name) {
    this.name = name;
    this.parent = null;
  }

  public ImplicitStereotypeDefinition(String name, StereotypeDefinition parent) {
    this.name = name;
    this.parent = parent;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Optional<StereotypeDefinition> getParent() {
    return ofNullable(parent);
  }

  @Override
  public int hashCode() {
    return hash(name, parent);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ImplicitStereotypeDefinition other = (ImplicitStereotypeDefinition) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (parent == null) {
      if (other.parent != null) {
        return false;
      }
    } else if (!parent.equals(other.parent)) {
      return false;
    }
    return true;
  }


}
