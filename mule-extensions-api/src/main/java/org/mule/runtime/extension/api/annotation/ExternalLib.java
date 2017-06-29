/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.mule.runtime.api.meta.LibraryType.LIBRARY;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.meta.LibraryType;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Specifies that the annotated element depends on a library which is not packaged with the extension.
 * <p>
 * This annotation is intended to be used in classes which represent the extension, a configuration or a
 * connection provider.
 * <p>
 * When the dependency is needed extension wide, then this annotation should be used at the extension level. If it's only
 * needed when using a particular configuration, then it should be used at the config level. Finally, if it's needed only
 * for establishing connections, then it should be used on a connection provider. Notice that you can have a mix of any of these
 * options, since an extension can depend on many external libraries in different places. An example would be the Database connector,
 * which has different {@link ConnectionProvider} for connecting to different types of databases, each one requiring a different
 * JDBC driver.
 * <p>
 * This annotation is repeatable, which means that any annotated component can depend on many external libraries.
 *
 * @since 1.0
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Repeatable(ExternalLibs.class)
public @interface ExternalLib {

  /**
   * @return The library's name
   */
  String name();

  /**
   * @return The library's description
   */
  String description() default "";

  /**
   * @return A regexp to match the name of the library's file.
   */
  String nameRegexpMatcher() default "";

  /**
   * @return If provided, the library should contain a class of the given name
   */
  String requiredClassName() default "";

  /**
   * @return The type of library needed
   */
  LibraryType type() default LIBRARY;
}
