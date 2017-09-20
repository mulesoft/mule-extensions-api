/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.extension.api.ExtensionConstants.DYNAMIC_CONFIG_EXPIRATION_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.DYNAMIC_CONFIG_EXPIRATION_FREQUENCY;
import static org.mule.runtime.extension.api.ExtensionConstants.EXPIRATION_POLICY_DESCRIPTION;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.declaration.type.annotation.ExpressionSupportAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.InfrastructureTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeDslAnnotation;
import org.mule.runtime.extension.api.runtime.ExpirationPolicy;

import java.util.concurrent.TimeUnit;

/**
 * Creates instances of {@link MetadataType} which represent an {@link ExpirationPolicy}.
 *
 * Notice that this representation of the type matches how the expiration policy is exposed
 * through the tooling and the DSL. It's not an exact match of what you'd get running the
 * {@link ExpirationPolicy} class through a {@link ClassTypeLoader}
 *
 * @since 1.0
 */
public class DynamicConfigExpirationTypeBuilder extends InfrastructureTypeBuilder {

  public MetadataType buildDynamicConfigExpirationType() {
    final ObjectTypeBuilder type = create(JAVA).objectType().id("DynamicConfigExpiration")
        .description(DYNAMIC_CONFIG_EXPIRATION_DESCRIPTION)
        .with(new TypeDslAnnotation(true, false, null, null));

    BaseTypeBuilder typeBuilder = create(JAVA);

    type.addField()
        .key("expirationPolicy")
        .value(buildExpirationPolicyType())
        .required(false)
        .with(new ExpressionSupportAnnotation(NOT_SUPPORTED));

    addLongField(type, typeBuilder, "frequency",
                 "A scalar time value for how often should the platform check for expirable dynamic configs",
                 DYNAMIC_CONFIG_EXPIRATION_FREQUENCY.getTime())
                     .with(new ExpressionSupportAnnotation(NOT_SUPPORTED));

    addEnumField(type, typeBuilder, "timeUnit", "A time unit that qualifies the frequency attribute",
                 DYNAMIC_CONFIG_EXPIRATION_FREQUENCY.getUnit(), TimeUnit.values())
                     .with(new ExpressionSupportAnnotation(NOT_SUPPORTED));

    return type.build();
  }

  public MetadataType buildExpirationPolicyType() {
    final ObjectTypeBuilder type = create(JAVA).objectType().id(ExpirationPolicy.class.getName())
        .with(new InfrastructureTypeAnnotation())
        .description(EXPIRATION_POLICY_DESCRIPTION)
        .with(new TypeDslAnnotation(true, false, null, null));

    BaseTypeBuilder typeBuilder = create(JAVA);

    addLongField(type, typeBuilder, "maxIdleTime", "A scalar time value for the maximum amount of time a dynamic "
        + "configuration instance should be allowed to be idle before it's considered eligible for expiration",
                 DYNAMIC_CONFIG_EXPIRATION_FREQUENCY.getTime())
                     .with(new ExpressionSupportAnnotation(NOT_SUPPORTED));

    addEnumField(type, typeBuilder, "timeUnit", "A time unit that qualifies the maxIdleTime attribute",
                 DYNAMIC_CONFIG_EXPIRATION_FREQUENCY.getUnit(), TimeUnit.values())
                     .with(new ExpressionSupportAnnotation(NOT_SUPPORTED));


    return type.build();
  }
}


