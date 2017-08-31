/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param.stereotype;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.api.meta.model.ExecutableComponentModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.stereotype.StereotypeDefinition;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for {@link SourceModel Sources} and {@link OperationModel Operations} level to communicate and declare
 * the {@link StereotypeDefinition}s that characterize the annotated {@link ExecutableComponentModel components}
 *
 * @since 1.0
 * @see StereotypeDefinition
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Stereotype {

  Class<? extends StereotypeDefinition> value();
}
