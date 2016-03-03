/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field inside a mule extension as being a set of parameters that the user can set.
 * This annotation is intended to be applied into fields which type is a POJO which properties
 * are to be processed as attributes. For example:
 * <p/>
 * <pre>
 *     {@code
 *     @Extension
 *     public class MyExtension {
 *
 *         @ParameterGroup
 *         private Options options;
 *     }
 *
 *     public class Options {
 *
 *          @Parameter
 *          private String color;
 *
 *          @Parameter
 *          @Optional
 *          private String mode;
 *
 *          private String owner;
 *     }
 *     }
 * </pre>
 * <p/>
 * The outcome of the code above is a configuration with two parameters called 'color' and 'mode', one required and the other optional.
 * The configuration has no attribute called options. If the Options class were to have another field also annotated with
 * {@link ParameterGroup}, then such fields will be ignored.
 * <p/>
 * It can also be used to define a hierarchy of nested parameter classes:
 * <pre>
 *     {@code
 *
 *     public class Options {
 *
 *          @Parameter
 *          private String color;
 *
 *          @Parameter
 *          @Optional
 *          private String mode;
 *
 *          @ParameterGroup
 *          private MoreOptions moreOptions;
 *     }
 *     }
 * </pre>
 * In this other example, the configuration that is augmented with this extra parameters
 * will have the sum of Options and MoreOptions parameters. Those parameters will be flattened, meaning
 * that the model will contain no reference to the fact that the MoreOptions parameters were nested inside
 * Options. Each field annotated with this annotation must be a Java bean property
 * (i.e: it needs to have setters and getters matching the field name).
 * <p/>
 * Lastly, the annotation can be applied to a method which is defining an operation:
 * <pre>
 *     {@code
 *
 *     public class Operations {
 *
 *         public void hello(String message, @ParameterGroup Options options) {
 *             ...
 *         }
 *
 *         public void goodBye(String message, @ParameterGroup Options options) {
 *
 *         }
 *     }
 * </pre>
 * <p/>
 * In this case, both operations will have three parameters: message, color and mode.
 * <p/>
 * Another consideration is that no parameter (in either a configuration or operation)
 * obtained through this annotation can have a name which collides with a parameter
 * defined in the top level class or in a superior group of the parameter group hierarchy
 *
 * @since 1.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParameterGroup
{

}
