/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.metadata;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.resolving.QueryEntityResolver;
import org.mule.runtime.api.metadata.resolving.TypeKeysResolver;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Query;
import org.mule.runtime.extension.api.introspection.metadata.NullMetadataResolver;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks one of the parameters of an Operation or Source as the key for the metadata lookup.
 * This lookup is resolved by the {@link TypeKeysResolver} referenced in the {@link MetadataScope}, at Operation, Source
 * or Extension level, which will return the list of available the {@link MetadataKey} or also, the lookup can be resolved
 * by the {@link QueryEntityResolver#getEntityKeys(MetadataContext)} for {@link Query} annotated operations, this time the
 * key will represent the query (DSQL or Native) which is used to calculate the output metadata.
 * <br/>
 * At design time, the selected {@link MetadataKey} of the annotated parameter will be taken as the key to provide metadata
 * for the parameter annotated with {@link Content} in a Operation and/or for output metadata of an Operation or Source.
 * <p>
 * <b>Annotation Usage:</b>
 * <ul>
 * <li><b>Operations: </b> The annotation must be applied to an Operation parameter to mark it as the metadata key parameter.
 * The usage of this annotation in multiple parameters of the same operation or fields of the operation class is not allowed.</li>
 * <li><b>Sources: </b> The annotation must be applied to a Source field annotated with {@link Parameter} to mark it as
 * the metadata key parameter. The usage of this annotation in a non {@link Parameter} field, multiple fields or in a method
 * parameter, is not allowed.</li>
 * </ul>
 * The illegal usage of this annotation could produce extension compilation errors
 *
 * @since 1.0
 */
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface MetadataKeyId {

  /**
   * @return the associated {@link TypeKeysResolver} for the annotated Component
   */
  Class<? extends TypeKeysResolver> value() default NullMetadataResolver.class;

}
