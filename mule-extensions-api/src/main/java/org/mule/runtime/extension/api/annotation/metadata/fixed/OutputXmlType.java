/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.metadata.fixed;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declares the annotated {@link ComponentModel}'s output {@link MetadataType} to the type represented by the provided element in
 * the XSD Schema.
 *
 * @since 1.1
 */
@MinMuleVersion("4.1")
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface OutputXmlType {

  /**
   * @return the XSD schema file where the element to be loaded for the output type is defined. The schema must live in the
   *         extension resources in order to be located.
   */
  String schema();

  /**
   * @return the qualified name used to reference the element to be loaded for the output within the provided
   *         {@link OutputXmlType#schema()} ()}.
   */
  String qname();
}
