/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

/**
 * Alternative to boxed {@link Boolean} which is disallowed as parameter types in the extensions API.
 * <p>
 * This enum is meant to model cases in which there's a boolean value which represents either true or false.
 * Traditional {@link Boolean} values are not a good fit for these, because they are represented as check boxes
 * which cannot really show the third state.
 * <p>
 * As a non-mandatory alternative, we offer this enum. Notice that you are free to implement your own, which better
 * fits your own domain model.
 *
 * @since 1.0
 */
public enum TripleStateBoolean {

  TRUE {

    @Override
    public Optional<Boolean> asBoolean() {
      return of(Boolean.TRUE);
    }

    @Override
    public boolean isAny() {
      return false;
    }
  },
  FALSE {

    @Override
    public Optional<Boolean> asBoolean() {
      return of(Boolean.FALSE);
    }

    @Override
    public boolean isAny() {
      return false;
    }
  },
  ANY {

    @Override
    public Optional<Boolean> asBoolean() {
      return empty();
    }

    @Override
    public boolean isAny() {
      return true;
    }
  };

  /**
   * @return the boolean value associated to each option
   */
  public abstract Optional<Boolean> asBoolean();

  /**
   * @return Whether {@code this} is {@link #ANY}
   */
  public abstract boolean isAny();
}
