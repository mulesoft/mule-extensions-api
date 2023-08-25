/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.param;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;
import org.mule.runtime.api.metadata.resolving.QueryEntityResolver;
import org.mule.runtime.extension.api.dsql.QueryTranslator;
import org.mule.runtime.extension.api.metadata.NullQueryMetadataResolver;

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
   * A {@link QueryTranslator} implementation used to translate a query from DSQL to the Native Query Language
   */
  Class<? extends QueryTranslator> translator();

  /**
   * A {@link QueryEntityResolver} implementation used to resolve metadata about the entities that can be queried using DSQL.
   */
  Class<? extends QueryEntityResolver> entityResolver();

  /**
   * A {@link OutputTypeResolver} that receives a {@link String} with the query when it's written in the Native Query Language.
   * This way the developer can resolved metadata for complex cases when DSQL is not used.
   * <p>
   * For default the {@link NullQueryMetadataResolver} implementation is used, returning an empty set of entities and a void
   * metadata type as the entity structure.
   */
  Class<? extends OutputTypeResolver<String>> nativeOutputResolver() default NullQueryMetadataResolver.class;
}
