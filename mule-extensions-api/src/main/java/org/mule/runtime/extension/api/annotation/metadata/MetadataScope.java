/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.metadata;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.resolving.AttributesTypeResolver;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;
import org.mule.runtime.api.metadata.resolving.TypeKeysResolver;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.metadata.NullMetadataResolver;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Associates the annotated Component to a {@link TypeKeysResolver}, and an {@link OutputTypeResolver}
 * that will be used to resolve the Component's {@link MetadataType} dynamically
 *
 * This annotation can be used at Operation or {@link Extension} level.
 * When used at {@link Extension} level this resolvers will serve as the default, and will be used to resolve the
 * dynamic metadata of all its the operations and sources.
 * If this annotation is instead used at Operation level, it will override any default {@link Extension}
 * declaration and use the resolvers referenced at that level to resolve the metadata of that method. This will only affect
 * the annotated element, while all the other operations will still respond to the {@link Extension} default.
 *
 * {@link MetadataScope} overriding works on all the declared resolvers, once the new scope is declared at
 * a lower level than {@link Extension} then none of the top level resolvers will work anymore as defaults, and
 * will have to be re-defined in the new declaration.
 *
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.annotation.metadata.MetadataScope} instead.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Documented
@Deprecated
public @interface MetadataScope {

  /**
   * @return the associated {@link TypeKeysResolver} for the annotated Component
   */
  Class<? extends TypeKeysResolver> keysResolver() default NullMetadataResolver.class;

  /**
   * @return the associated {@link OutputTypeResolver} for the annotated Component
   */
  Class<? extends OutputTypeResolver> outputResolver() default NullMetadataResolver.class;

  /**
   * @return the associated {@link AttributesTypeResolver} for the annotated Component
   */
  Class<? extends AttributesTypeResolver> attributesResolver() default NullMetadataResolver.class;

}
