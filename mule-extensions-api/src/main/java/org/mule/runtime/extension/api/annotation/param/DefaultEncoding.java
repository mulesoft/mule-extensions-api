/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.param;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is meant to be applied on fields of classes or on operation's arguments.
 * <p>
 * This annotation signals that the target field should be injected with Mule's default encoding.
 * <p>
 *
 * The annotated field should be of type {@link String}
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DefaultEncoding {

}
