/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.param;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field inside a mule extension as being a parameter that the user can set. It can either be applied to a class defining
 * a configuration, or to a class in which operations are implemented.
 * <p>
 * If applied to an operation class, then the parameter will be added to all the operations defined in that class.
 * <p>
 * The field must be a Java bean property (ie it needs to have setters and getters matching the field name).
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Parameter {

}
