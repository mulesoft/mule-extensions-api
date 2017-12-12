/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.api.annotation.metadata.fixed;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ComponentModel;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declares the annotated {@link ComponentModel}'s attributes {@link MetadataType} to the type represented by the
 * provided element in the XSD Schema.
 *
 * @since 1.1
 */
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface AttributesXmlType {

  /**
   * @return the XSD schema file where the element to be loaded for the attributes type is defined.
   * The schema must live in the extension resources in order to be located.
   */
  String schema();

  /**
   * @return the qualified name used to reference the element to be loaded for the attributes
   * within the provided {@link AttributesXmlType#schema()}.
   */
  String qname();
}
