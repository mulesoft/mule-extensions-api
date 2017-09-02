/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param.stereotype;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.api.meta.model.nested.NestedComponentModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.runtime.process.Chain;
import org.mule.runtime.extension.api.stereotype.StereotypeDefinition;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declares an array of {@link Class classes} of an {@link StereotypeDefinition}, to communicate and declare
 * which {@link StereotypeDefinition} the {@link NestedComponentModel nested component} allows to be injected with.
 * <p>
 * This annotation can be applied to {@link ParameterModel parameters} of type {@link Chain}.
 *
 * @since 1.0
 * @see StereotypeDefinition
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface AllowedStereotypes {

  Class<? extends StereotypeDefinition>[] value();
}
