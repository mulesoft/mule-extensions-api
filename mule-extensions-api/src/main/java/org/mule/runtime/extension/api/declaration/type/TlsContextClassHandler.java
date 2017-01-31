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
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.api.builder.UnionTypeBuilder;
import org.mule.metadata.java.api.handler.ClassHandler;
import org.mule.metadata.java.api.handler.TypeHandlerManager;
import org.mule.metadata.java.api.utils.ParsingContext;
import org.mule.runtime.api.tls.TlsContextFactory;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Implementation of {@link ClassHandler} for the {@link TlsContextFactory} class
 *
 * @since 1.0
 */
final class TlsContextClassHandler extends InfrastructureTypeBuilder implements ClassHandler {

  /**
   * {@inheritDoc}
   * @return {@code true} if {@code clazz} equals {@link TlsContextFactory}
   */
  @Override
  public boolean handles(Class<?> clazz) {
    return TlsContextFactory.class.equals(clazz);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TypeBuilder<?> handleClass(Class<?> clazz,
                                    List<Type> genericTypes,
                                    TypeHandlerManager typeHandlerManager,
                                    ParsingContext context,
                                    BaseTypeBuilder typeBuilder) {

    ObjectTypeBuilder type = objectType(typeBuilder, TlsContextFactory.class, context);
    typeBuilder = create(JAVA);
    addStringField(type, typeBuilder, "enabledProtocols",
                   "A comma separated list of protocols enabled for this context.", null);
    addStringField(type, typeBuilder, "enabledCipherSuites",
                   "A comma separated list of cipher suites enabled for this context.",
                   null);

    addTrustStoreField(typeBuilder, type);
    addKeyStoreField(typeBuilder, type);

    return type;
  }

  private void addTrustStoreField(BaseTypeBuilder typeBuilder, ObjectTypeBuilder type) {
    ObjectTypeBuilder trustStoreType = typeBuilder.objectType().id(Object.class.getName())
        .description("Trust store configuration. If used client side, the trust store contains the certificates of the "
            + "trusted servers. If used server side, it contains the certificates of the trusted clients.");
    typeBuilder = create(JAVA);
    addStringField(trustStoreType, typeBuilder, "path", "The location (which will be resolved relative to the current "
        + "classpath and file system, if possible) of the trust store.", null);

    addStringField(trustStoreType, typeBuilder, "password", "The password used to protect the trust store.", null);
    addField(trustStoreType, getStoreMetadataType(typeBuilder), "type", "The type of store used.");

    addStringField(trustStoreType, typeBuilder, "algorithm", "The algorithm used by the trust store.", null);

    addBooleanField(trustStoreType, typeBuilder, "insecure", "If true, no certificate validations will be performed.", false);

    type.addField().key("trust-store").required(false).value(trustStoreType);
  }

  private UnionTypeBuilder getStoreMetadataType(BaseTypeBuilder typeBuilder) {
    return typeBuilder.unionType()
        .of(getEnumType(create(JAVA), null, "jks", "jceks", "pkcs12"))
        .of(create(JAVA).stringType());
  }

  private void addKeyStoreField(BaseTypeBuilder typeBuilder, ObjectTypeBuilder type) {
    ObjectTypeBuilder keyStoreType = typeBuilder.objectType().id(Object.class.getName())
        .description("Key store configuration. The key store contains the keys of this server/client.");

    addStringField(keyStoreType, typeBuilder, "path", "The location (which will be resolved relative to the current "
        + "classpath and file system, if possible) of the key store.", null);

    addField(keyStoreType, getStoreMetadataType(typeBuilder), "type", "The type of store used.");
    addStringField(keyStoreType, typeBuilder, "alias",
                   "When the key store contains many private keys, this attribute indicates the alias of the key that "
                       + "should be used. If not defined, the first key in the file will be used by default.",
                   null);

    addStringField(keyStoreType, typeBuilder, "keyPassword", "The password used to protect the private key.", null);
    addStringField(keyStoreType, typeBuilder, "password", "The password used to protect the key store.", null);
    addStringField(keyStoreType, typeBuilder, "algorithm", "The algorithm used by the key store.", null);

    type.addField().key("key-store").required(false).value(keyStoreType);
  }
}
