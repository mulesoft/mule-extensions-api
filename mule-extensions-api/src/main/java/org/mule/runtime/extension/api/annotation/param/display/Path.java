/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param.display;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.mule.runtime.api.meta.model.display.PathModel.Type.ANY;

import org.mule.runtime.api.meta.model.display.PathModel;
import org.mule.runtime.api.meta.model.display.PathModel.Location;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a field or parameter annotated with {@link Parameter} as a path to a file or directory.
 * <p/>
 * This annotation should only be used with {@link String} parameters.
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.extension.annotation.param.display.Path} instead.
 */
@Target(value = {PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
@Deprecated
public @interface Path {

  /**
   * @return whether the path is to a directory or a file
   */
  PathModel.Type type() default ANY;

  /**
   * @return whether the path parameter also supports urls.
   */
  boolean acceptsUrls() default false;

  /**
   * @return a classifier for the path's generic {@link Location}
   */
  Location location() default Location.ANY;

  /**
   * @return the file extensions that this path handles.
   */
  String[] acceptedFileExtensions() default {};
}
