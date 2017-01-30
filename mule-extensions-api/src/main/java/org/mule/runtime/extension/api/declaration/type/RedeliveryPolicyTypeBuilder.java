/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeAliasAnnotation;

/**
 * Creates instances of {@link MetadataType} which represent a redelivery policy
 *
 * @since 1.0
 */
public final class RedeliveryPolicyTypeBuilder extends InfrastructureTypeBuilder {

  public static final String MAX_REDELIVERY_COUNT = "maxRedeliveryCount";
  public static final String USE_SECURE_HASH = "useSecureHash";
  public static final String MESSAGE_DIGEST_ALGORITHM = "messageDigestAlgorithm";
  public static final String ID_EXPRESSION = "idExpression";
  public static final String OBJECT_STORE_REF = "object-store-ref";
  public static final String REDELIVERY_POLICY = "RedeliveryPolicy";

  /**
   * @return a {@link MetadataType} representation of a redelivery policy
   */
  public MetadataType buildRedeliveryPolicyType() {
    ObjectTypeBuilder objectType = create(JAVA).objectType()
        .id(Object.class.getName())
        .with(new TypeAliasAnnotation(REDELIVERY_POLICY));
    BaseTypeBuilder typeBuilder = create(JAVA);

    addIntField(objectType, typeBuilder,
                MAX_REDELIVERY_COUNT,
                "The maximum number of times a message can be redelivered and processed unsuccessfully before triggering "
                    + "process-failed-message",
                5);

    addBooleanField(objectType, typeBuilder,
                    USE_SECURE_HASH,
                    "Whether to use a secure hash algorithm to identify a redelivered message",
                    true);
    addStringField(objectType, typeBuilder,
                   MESSAGE_DIGEST_ALGORITHM,
                   "The secure hashing algorithm to use. If not set, the default is SHA-256.",
                   null);

    addStringField(objectType, typeBuilder,
                   ID_EXPRESSION,
                   "Defines one or more expressions to use to determine when a message has been redelivered. "
                       + "This property may only be set if useSecureHash is false.",
                   null);

    addStringField(objectType, typeBuilder,
                   OBJECT_STORE_REF,
                   "The object store where the redelivery counter for each message is going to be stored.",
                   null);

    return objectType.build();
  }
}
