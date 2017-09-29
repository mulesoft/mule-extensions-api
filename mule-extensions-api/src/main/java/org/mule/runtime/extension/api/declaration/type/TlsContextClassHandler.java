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
import static org.mule.runtime.api.meta.model.display.PathModel.Type.FILE;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_CONTEXT_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_KEY_STORE_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_PREFIX;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_TRUST_STORE_ELEMENT_IDENTIFIER;
import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.api.model.StringType;
import org.mule.metadata.java.api.handler.ClassHandler;
import org.mule.metadata.java.api.handler.TypeHandlerManager;
import org.mule.metadata.java.api.utils.ParsingContext;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.PathModel;
import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.extension.api.declaration.type.annotation.DisplayTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ExpressionSupportAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.InfrastructureTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterDslAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.QNameTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeDslAnnotation;

import java.lang.reflect.Type;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * Implementation of {@link ClassHandler} for the {@link TlsContextFactory} class
 *
 * @since 1.0
 */
final class TlsContextClassHandler extends InfrastructureTypeBuilder implements ClassHandler {

  /**
   * {@inheritDoc}
   *
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
    type.with(new TypeAliasAnnotation("Tls"));
    type.with(new InfrastructureTypeAnnotation());
    type.with(new TypeDslAnnotation(true, true, null, null));
    type.with(new QNameTypeAnnotation(new QName("http://www.mulesoft.org/schema/mule/tls",
                                                TLS_CONTEXT_ELEMENT_IDENTIFIER,
                                                TLS_PREFIX)));
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
    ObjectTypeBuilder trustStoreType = typeBuilder.objectType().id("TrustStore")
        .with(new InfrastructureTypeAnnotation())
        .description("Trust store configuration. If used client side, the trust store contains the certificates of the "
            + "trusted servers. If used server side, it contains the certificates of the trusted clients.");
    typeBuilder = create(JAVA);
    addStringField(trustStoreType, typeBuilder, "path", "The location (which will be resolved relative to the current "
        + "classpath and file system, if possible) of the trust store.", null)
            .with(filePathDisplayModel());

    addPasswordField(trustStoreType, typeBuilder, "password", "The password used to protect the trust store.", null);
    addField(trustStoreType, getStoreMetadataType(typeBuilder), "type", "The type of store used.");

    addStringField(trustStoreType, typeBuilder, "algorithm", "The algorithm used by the trust store.", null);

    addBooleanField(trustStoreType, typeBuilder, "insecure",
                    "If true, no certificate validations will be performed, rendering connections vulnerable "
                        + "to attacks. Use at your own risk.",
                    false);

    type.addField()
        .with(new ParameterDslAnnotation(true, false))
        .with(new ExpressionSupportAnnotation(NOT_SUPPORTED))
        .key(TLS_TRUST_STORE_ELEMENT_IDENTIFIER)
        .required(false)
        .value(trustStoreType);
  }

  private TypeBuilder<StringType> getStoreMetadataType(BaseTypeBuilder typeBuilder) {
    return typeBuilder.stringType()
        .with(new DisplayTypeAnnotation(DisplayModel.builder().example("jks, jceks, pkcs12 or other store type").build()));
  }

  private void addKeyStoreField(BaseTypeBuilder typeBuilder, ObjectTypeBuilder type) {
    ObjectTypeBuilder keyStoreType = typeBuilder.objectType().id("KeyStore")
        .with(new InfrastructureTypeAnnotation())
        .description("Key store configuration. The key store contains the keys of this server/client.");

    addStringField(keyStoreType, typeBuilder, "path", "The location (which will be resolved relative to the current "
        + "classpath and file system, if possible) of the key store.", null).with(filePathDisplayModel());;

    addField(keyStoreType, getStoreMetadataType(typeBuilder), "type", "The type of store used.");
    addStringField(keyStoreType, typeBuilder, "alias",
                   "When the key store contains many private keys, this attribute indicates the alias of the key that "
                       + "should be used. If not defined, the first key in the file will be used by default.",
                   null);

    addPasswordField(keyStoreType, typeBuilder, "keyPassword", "The password used to protect the private key.", null);
    addPasswordField(keyStoreType, typeBuilder, "password", "The password used to protect the key store.", null);
    addStringField(keyStoreType, typeBuilder, "algorithm", "The algorithm used by the key store.", null);

    type.addField()
        .with(new ParameterDslAnnotation(true, false))
        .with(new ExpressionSupportAnnotation(NOT_SUPPORTED))
        .key(TLS_KEY_STORE_ELEMENT_IDENTIFIER)
        .required(false)
        .value(keyStoreType);
  }

  private DisplayTypeAnnotation filePathDisplayModel() {
    return new DisplayTypeAnnotation(DisplayModel.builder().path(new PathModel(FILE, false, new String[] {})).build());
  }

}
