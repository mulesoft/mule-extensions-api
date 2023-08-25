/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.param;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Allows to define a group of parameters which share some kind of special relationship and thus makes sense for them to belong to
 * the same group. This grouping is done by placing these parameters as fields of the same Java class, and then use that class
 * alongside this annotation.
 * <p>
 * Unlike a regular pojo, the parameters defined in this class will be flattened and the owning {@link ParameterizedModel} will
 * not contain any reference to the defining class.
 * <p>
 * For example:
 * 
 * <pre>
 * 
 * {
 *   &#64;code
 *   &#64;Extension
 *   public class MyExtension {
 *
 *     &#64;ParameterGroup("some group name")
 *     private Options options;
 *   }
 *
 *   public class Options {
 *
 *     &#64;Parameter
 *     private String color;
 *
 *     &#64;Parameter
 *     &#64;Optional
 *     private String mode;
 *
 *     private String owner;
 *   }
 * }
 * </pre>
 * <p/>
 * The outcome of the code above is a configuration with two parameters called 'color' and 'mode', one required and the other
 * optional. The configuration has no attribute called options. If the Options class were to have another field also annotated
 * with {@link ParameterGroup}, then such fields will be ignored.
 * <p/>
 * <p>
 * In this other example, the configuration that is augmented with this extra parameters will have the sum of Options and
 * MoreOptions parameters. Those parameters will be flattened, meaning that the model will contain no reference to the fact that
 * the MoreOptions parameters were nested inside Options. Each field annotated with this annotation must be a Java bean property
 * (i.e: it needs to have setters and getters matching the field name).
 * <p/>
 * Lastly, the annotation can be applied to a method which is defining an operation:
 * 
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
 * Another consideration is that no parameter (in either a configuration or operation) obtained through this annotation can have a
 * name which collides with a parameter defined in the top level class or in a superior group of the parameter group hierarchy
 *
 * @since 1.0
 *
 */
@MinMuleVersion("4.1")
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface ParameterGroup {

  /**
   * Group name for parameters that are considered for advanced usage.
   */
  String ADVANCED = ParameterGroupModel.ADVANCED;
  /**
   * Group name for parameters that are considered to be part of a connection configuration.
   */
  String CONNECTION = ParameterGroupModel.CONNECTION;

  /**
   * The name of the group being defined. This name cannot be equivalent to {@link ParameterGroupModel#DEFAULT_GROUP_NAME}
   */
  String name();

  /**
   * If {@code true}, the Group will be shown as an inline element of the DSL
   */
  boolean showInDsl() default false;

}
