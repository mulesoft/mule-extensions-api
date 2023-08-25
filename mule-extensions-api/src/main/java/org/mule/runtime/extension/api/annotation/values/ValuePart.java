/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.values;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.api.value.Value;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a field inside a POJO that represents a composed {@link Value} as one of the parts of that composed {@link Value}.
 * <p>
 * Multiple {@link ValuePart parts} describe a POJO that can be annotated with {@link OfValues};
 * <p>
 * {@link ValuePart} annotated fields must be of type {@link String}
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target({FIELD})
@Retention(RUNTIME)
@Documented
public @interface ValuePart {

  /**
   * @return the resolution order of this key part during the building of the {@link Value} annotated parameter corresponding to
   *         this {@link ValuePart}.
   */
  int order();
}
