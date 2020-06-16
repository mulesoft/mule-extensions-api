/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param.stereotype;

import org.mule.metadata.api.model.StringType;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.annotation.param.ConfigOverride;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.Query;
import org.mule.runtime.extension.api.annotation.param.display.Text;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a Component's {@link ParameterModel} as the Component ID.
 *
 * Being a Component ID means that the value associated to the annotated parameter can be used to reference the
 * {@link ComponentModel component} in a mule application uniquely across all the instances of the same {@link ComponentModel}.
 * When used on a global element of the application, then this Component ID serves as a global ID in the application.
 * <p>
 * An example of a {@link ComponentModel} ID is the {@code name} parameter of a {@code config} element.
 * <p>
 * Restrictions apply in order for a {@link ParameterModel} to be a {@link ComponentModel} ID:
 * <ul>
 * <li>Only <b>one</b> {@link ParameterModel parameter} can be {@link ComponentId} for any given {@link ComponentModel}</li>
 * <li>Only <b>required</b> {@link ParameterModel parameters} serve as Component ID</li>
 * <li>The parameter's type has to be {@link StringType String}</li>
 * <li>The parameter's expression support will be {@link ExpressionSupport#NOT_SUPPORTED}, so no dynamic values are allowed</li>
 * <li>{@link Content} qualifier is not allowed for a Component ID</li>
 * <li>{@link Text} qualifier is not allowed for a Component ID</li>
 * <li>{@link Query} qualifier is not allowed for a Component ID</li>
 * <li>Defaulting to a {@link ConfigOverride} is not allowed for a Component ID since it describes the ID of each
 * individual component and no common global value should be used as ID</li>
 * </ul>
 *
 * @since 1.2.0
 * @deprecated use {@link org.mule.sdk.api.extension.annotation.param.stereotype.ComponentId} instead.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface ComponentId {

}
