/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.dsl.xml;

import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows the customization of the schema attributes. If absent then the runtime will calculate default values for each attribute.
 * If present you can add all the attributes or just the ones you need.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Xml {

  /**
   * Extension's prefix. If left empty it will create a default one based on the extension's name, removing the words "extension",
   * "module" or "connector" at the end if they are present and hyphenating the resulting name.
   *
   * <pre>
   * Mulesoft Extension           = mulesoft
   * Cloud Service Connector      = cloud-service
   * Extension                    = extension
   * </pre>
   */
  String prefix() default "";

  /**
   * Location URI for the namespace. If left empty it will default to
   * &quot;http://www.mulesoft.org/schema/mule/&lt;&lt;extension_namespace&gt;&gt;
   */
  String namespace() default "";


}
