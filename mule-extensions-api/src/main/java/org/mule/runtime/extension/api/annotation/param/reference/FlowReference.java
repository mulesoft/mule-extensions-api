/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.param.reference;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to be used in a {@link String} type {@link Parameter} field or parameter that is a reference to a flow in a mule
 * app.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface FlowReference {

}
