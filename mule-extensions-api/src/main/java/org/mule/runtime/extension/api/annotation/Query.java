/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.api.metadata.resolving.MetadataOutputResolver;
import org.mule.runtime.api.metadata.resolving.QueryEntityResolver;
import org.mule.runtime.extension.api.introspection.ComponentModel;
import org.mule.runtime.extension.api.introspection.dsql.QueryTranslator;
import org.mule.runtime.extension.api.introspection.metadata.NullQueryOutputMetadataResolver;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation is meant to be applied to support easy query building by using DataSense Query Language, <strong>DSQL</strong>.
 * <p>
 * When an {@link ComponentModel} parameter is annotated with this annotation, you can assume your query parameter has been
 * translated to native query language using the specified {@link QueryTranslator}.
 *
 * @since 1.0
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface Query {

  /**
   * A {@link QueryTranslator} implementation used to translate a query from DSQL
   * to the Native Query Language
   */
  Class<? extends QueryTranslator> translator();

  /**
   * A {@link QueryEntityResolver} implementation used to resolve metadata about the
   * entities that can be queried using DSQL.
   */
  Class<? extends QueryEntityResolver> entityResolver();

  /**
   * A {@link MetadataOutputResolver} that receives a {@link String} with the query when it's written in
   * the Native Query Language. This way the developer can resolved metadata for complex cases when DSQL is
   * not used.
   * <p>
   * For default the {@link NullQueryOutputMetadataResolver} implementation is used, returning an empty set of entities
   * and null metadata type as the entity structure.
   */
  Class<? extends MetadataOutputResolver<String>> nativeOutputResolver() default NullQueryOutputMetadataResolver.class;
}
