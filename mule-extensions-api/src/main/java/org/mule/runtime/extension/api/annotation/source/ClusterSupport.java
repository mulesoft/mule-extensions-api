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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated {@link Source} should only run on the primary node, when running in cluster mode.
 * <p>
 * A parameter of name {@link ExtensionConstants#PRIMARY_NODE_ONLY_PARAMETER_NAME} will not be added automatically and the
 * runtime will only start the source when the mule instance becomes the primary node.
 *
 * @since 1.1
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface ClusterSupport {

  SourceClusterSupport value() default DEFAULT_ALL_NODES;

}
