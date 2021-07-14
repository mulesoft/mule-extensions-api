/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.source;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import static org.mule.runtime.extension.api.annotation.source.SourceClusterSupport.DEFAULT_ALL_NODES;

import org.mule.runtime.extension.api.ExtensionConstants;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates the {@link SourceClusterSupport} that a {@link Source} should have when running in cluster mode.
 *
 * When the selected value is other than {@link SourceClusterSupport#NOT_SUPPORTED}, a parameter named
 * {@link ExtensionConstants#PRIMARY_NODE_ONLY_PARAMETER_NAME} is addeed automatically to allow the user to configure whether the
 * source should run in all nodes or just the primary ones.
 *
 * Notice that when this parameter is in fact added, it will be optional and its default value will depend on the
 * {@link SourceClusterSupport}
 *
 * @since 1.1
 */
@MinMuleVersion("4.1")
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface ClusterSupport {

  SourceClusterSupport value() default DEFAULT_ALL_NODES;

}
