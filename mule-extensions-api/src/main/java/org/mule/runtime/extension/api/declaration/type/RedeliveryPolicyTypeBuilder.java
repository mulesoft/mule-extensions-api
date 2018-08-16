/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.singletonList;
import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.OBJECT_STORE;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.store.ObjectStore;
import org.mule.runtime.extension.api.declaration.type.annotation.ExclusiveOptionalsTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.FlattenedTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.InfrastructureTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.StereotypeTypeAnnotation;

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
  public static final String OBJECT_STORE_REF = "objectStore";
  public static final String REDELIVERY_POLICY = "RedeliveryPolicy";
  public static final String MESSAGE_IDENTIFIER = "MessageIdentifier";

  /**
   * @return a {@link MetadataType} representation of a redelivery policy
   */
  public MetadataType buildRedeliveryPolicyType() {
    ObjectTypeBuilder objectType = create(JAVA).objectType()
        .id(REDELIVERY_POLICY);
    BaseTypeBuilder typeBuilder = create(JAVA);

    objectType.with(new InfrastructureTypeAnnotation());

    addIntField(objectType, typeBuilder,
                MAX_REDELIVERY_COUNT,
                "The maximum number of times a message can be redelivered and processed unsuccessfully before triggering "
                    + "process-failed-message",
                5);

    addStringField(objectType, typeBuilder,
                   MESSAGE_DIGEST_ALGORITHM,
                   "The secure hashing algorithm to use. If not set, the default is SHA-256.",
                   null);

    objectType.addField()
        .with(new FlattenedTypeAnnotation())
        .description("Defines which strategy is used to identify the messages.")
        .key(MESSAGE_IDENTIFIER)
        .value(buildMessageFilterType());

    objectType.addField()
        .with(new StereotypeTypeAnnotation(singletonList(OBJECT_STORE)))
        .description("The object store where the redelivery counter for each message is going to be stored.")
        .key(OBJECT_STORE_REF)
        .value(ExtensionsTypeLoaderFactory.getDefault().createTypeLoader().load(ObjectStore.class));

    return objectType.build();
  }

  private MetadataType buildMessageFilterType() {
    final ObjectTypeBuilder objectTypeBuilder = create(JAVA).objectType().id("RedeliveryPolicyMessageIdentifier")
        .with(new ExclusiveOptionalsTypeAnnotation(newHashSet(USE_SECURE_HASH, ID_EXPRESSION), true));

    BaseTypeBuilder typeBuilder = create(JAVA);

    addBooleanField(objectTypeBuilder, typeBuilder,
                    USE_SECURE_HASH,
                    "Whether to use a secure hash algorithm to identify a redelivered message",
                    true);

    addStringField(objectTypeBuilder, typeBuilder,
                   ID_EXPRESSION,
                   "Defines one or more expressions to use to determine when a message has been redelivered. "
                       + "This property may only be set if useSecureHash is false.",
                   null);

    return objectTypeBuilder.build();
  }

}
