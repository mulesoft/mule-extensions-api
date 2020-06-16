/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.values;

import static java.util.Collections.unmodifiableSet;
import org.mule.api.annotation.NoExtend;
import org.mule.runtime.api.value.Value;

import java.util.Objects;
import java.util.Set;

/**
 * Immutable implementation of {@link Value}
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.extension.values.ImmutableValue} instead.
 */
@NoExtend
@Deprecated
public class ImmutableValue implements Value {

  private final String id;
  private final String displayName;
  private final String partName;
  private final Set<Value> childs;

  ImmutableValue(String id, String displayName, Set<Value> childs, String partName) {
    this.id = id;
    this.displayName = displayName;
    this.childs = childs;
    this.partName = partName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId() {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDisplayName() {
    return displayName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Value> getChilds() {
    return unmodifiableSet(childs);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPartName() {
    return partName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ImmutableValue that = (ImmutableValue) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(displayName, that.displayName) &&
        Objects.equals(partName, that.partName) &&
        Objects.equals(childs, that.childs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, displayName, partName, childs);
  }

  @Override
  public String toString() {
    return "{" +
        "id:'" + id + '\'' +
        ", displayName:'" + displayName + '\'' +
        ", partName:'" + partName + '\'' +
        ", childs:" + childs +
        '}';
  }
}
