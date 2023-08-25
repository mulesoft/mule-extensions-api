/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.param;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.extension.api.exception.IllegalParameterModelDefinitionException;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Map;

/**
 * Indicates that the annotated parameter should never be assigned a {@code null} value, even though such value is a valid
 * assignment on the mule DSL.
 * <p>
 * This annotation is intended to be used alongside {@link Optional} (the concept itself doesn't make sense for required
 * parameters). When the annotated parameter is resolved to a {@code null} value, the runtime will create a default instance of
 * such parameter to prevent a null variable.
 * <p>
 * This behaviour is implemented slightly different depending on the parameter type:
 * <ul>
 * <li>{@link Collection} and {@link Map}: an empty collection/map is created</li>
 * <li>A generic Pojo: A default instance is created (default constructor required)</li>
 * <li>An non-instantiable type: A default instance is created using the type provided as parameter in
 * {@code defaultImplementingType}.</li> Any {@link Optional} {@link Parameter} fields with a default value will be assigned to
 * such default. E.g.:
 * 
 * <pre>
 *      public class HelloWorld {
 *      &#64;Parameter
 *      &#64;Optional(defaultValue="Hello world!"
 *      private String greeting;
 *      }
 * </pre>
 * <p>
 * A null safe parameter of type HelloWorld would be created with the greeting field assigned to {@code Hello world!}</li>
 * <li>Basic types: String, numbers, dates, etc, are not supported. A {@link IllegalParameterModelDefinitionException} will be
 * thrown by the runtime if used in parameters of such type</li>
 * </ul>
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface NullSafe {

  /**
   * Declares the default {@code Type} that will be instantiated if the {@code NullSafe} mechanism is triggered on a
   * non-instantiable type
   *
   * @return the default {@code Type} to be used when creating the default instance
   */
  Class<?> defaultImplementingType() default Object.class;
}
