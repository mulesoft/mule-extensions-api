/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.values;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.api.value.Value;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a field inside a POJO that represents a composed {@link Value} as one of the parts
 * of that composed {@link Value}.
 * <p>
 * Multiple {@link ValuePart parts} describe a POJO that can be annotated with {@link OfValues};
 * <p>
 * {@link ValuePart} annotated fields must be of type {@link String}
 *
 * @since 1.0
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
public @interface ValuePart {

  /**
   * @return the resolution order of this key part during the building of
   * the {@link Value} annotated parameter corresponding to this {@link ValuePart}.
   */
  int order();
}
