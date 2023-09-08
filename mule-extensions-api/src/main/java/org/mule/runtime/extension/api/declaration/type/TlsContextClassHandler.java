/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static java.util.Arrays.asList;
import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.display.PathModel.Location.EMBEDDED;
import static org.mule.runtime.api.meta.model.display.PathModel.Type.FILE;
import static org.mule.runtime.api.meta.model.stereotype.StereotypeModelBuilder.newStereotype;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureTypeMapping.TLS_NAMESPACE_URI;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_CONTEXT_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_CRL_FILE_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_CUSTOM_OCSP_RESPONDER_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_KEY_STORE_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_PREFIX;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_REVOCATION_CHECK_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_STANDARD_REVOCATION_CHECK_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_TRUST_STORE_ELEMENT_IDENTIFIER;

import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.api.builder.UnionTypeBuilder;
import org.mule.metadata.api.model.StringType;
import org.mule.metadata.java.api.handler.ClassHandler;
import org.mule.metadata.java.api.handler.TypeHandlerManager;
import org.mule.metadata.java.api.utils.ParsingContext;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.display.PathModel;
import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.extension.api.declaration.type.annotation.DisplayTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ExpressionSupportAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.InfrastructureTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.LayoutTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.ParameterDslAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.QNameTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.StereotypeTypeAnnotation;
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
    return TlsContextFactory.class.isAssignableFrom(clazz);
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
    type.with(new QNameTypeAnnotation(new QName(TLS_NAMESPACE_URI,
                                                TLS_CONTEXT_ELEMENT_IDENTIFIER,
                                                TLS_PREFIX)));
    type.with(new StereotypeTypeAnnotation(asList(newStereotype(TLS_CONTEXT_ELEMENT_IDENTIFIER, TLS_PREFIX).build())));
    addStringField(type, typeBuilder, "enabledProtocols",
                   "A comma separated list of protocols enabled for this context.", null);
    addStringField(type, typeBuilder, "enabledCipherSuites",
                   "A comma separated list of cipher suites enabled for this context.",
                   null);

    addTrustStoreField(typeBuilder, type);
    addKeyStoreField(typeBuilder, type);
    addRevocationCheckField(typeBuilder, type);

    return type;
  }

  private void addTrustStoreField(BaseTypeBuilder typeBuilder, ObjectTypeBuilder type) {
    ObjectTypeBuilder trustStoreType = typeBuilder.objectType().id("TrustStore")
        .with(new InfrastructureTypeAnnotation())
        .with(new QNameTypeAnnotation(new QName(TLS_NAMESPACE_URI, TLS_TRUST_STORE_ELEMENT_IDENTIFIER, TLS_PREFIX)))
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
        .with(new LayoutTypeAnnotation(LayoutModel.builder().order(0).build()))
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
        .with(new QNameTypeAnnotation(new QName(TLS_NAMESPACE_URI, TLS_KEY_STORE_ELEMENT_IDENTIFIER, TLS_PREFIX)))
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
        .with(new LayoutTypeAnnotation(LayoutModel.builder().order(1).build()))
        .key(TLS_KEY_STORE_ELEMENT_IDENTIFIER)
        .required(false)
        .value(keyStoreType);
  }

  private DisplayTypeAnnotation filePathDisplayModel() {
    return new DisplayTypeAnnotation(DisplayModel.builder().path(new PathModel(FILE, false, EMBEDDED, new String[] {})).build());
  }

  private void addRevocationCheckField(BaseTypeBuilder typeBuilder, ObjectTypeBuilder type) {
    ObjectTypeBuilder standardRevocationCheck =
        typeBuilder.objectType().id(TLS_STANDARD_REVOCATION_CHECK_ELEMENT_IDENTIFIER)
            .with(new InfrastructureTypeAnnotation())
            .with(new QNameTypeAnnotation(new QName(TLS_NAMESPACE_URI, TLS_STANDARD_REVOCATION_CHECK_ELEMENT_IDENTIFIER,
                                                    TLS_PREFIX)))
            .description("Uses the standard JVM certificate revocation checks, which depend on the certificate having the "
                + "corresponding extension points (additional tags for CRLDP or OCSP), and the availability "
                + "of revocation servers.");

    addBooleanField(standardRevocationCheck, typeBuilder, "onlyEndEntities",
                    "Only verify the last element of the certificate chain.", false);
    addBooleanField(standardRevocationCheck, typeBuilder, "preferCrls", "Try CRL instead of OCSP first.", false);
    addBooleanField(standardRevocationCheck, typeBuilder, "noFallback",
                    "Do not use the secondary checking method (the one not selected before).", false);
    addBooleanField(standardRevocationCheck, typeBuilder, "softFail",
                    "Avoid verification failure when the revocation server can not be reached or is busy.", false);

    ObjectTypeBuilder customOcspResponder = typeBuilder.objectType().id(TLS_CUSTOM_OCSP_RESPONDER_ELEMENT_IDENTIFIER)
        .with(new InfrastructureTypeAnnotation())
        .with(new QNameTypeAnnotation(new QName(TLS_NAMESPACE_URI, TLS_CUSTOM_OCSP_RESPONDER_ELEMENT_IDENTIFIER, TLS_PREFIX)))
        .description("Uses a custom OCSP responder for certificate revocation checks, with a specific trusted certificate for "
            + "revocating other keys. This ignores extension points (additional tags for CRLDP or OCSP) present in the "
            + "certificate, if any.");

    addStringField(customOcspResponder, typeBuilder, "url", "The URL of the OCSP responder.", null);
    addStringField(customOcspResponder, typeBuilder, "certAlias",
                   "Alias of the signing certificate for the OCSP response (must be in the trust store), if present.", null);

    ObjectTypeBuilder crlFile = typeBuilder.objectType().id(TLS_CRL_FILE_ELEMENT_IDENTIFIER)
        .with(new InfrastructureTypeAnnotation())
        .with(new QNameTypeAnnotation(new QName(TLS_NAMESPACE_URI, TLS_CRL_FILE_ELEMENT_IDENTIFIER, TLS_PREFIX)))
        .description("Local file based certificate revocation checker, which requires a CRL file to be accessible and ignores "
            + "extension points (additional tags for CRLDP and OCSP) in the certificate.");

    addStringField(crlFile, typeBuilder, "path", "The path to the CRL file.", null);

    UnionTypeBuilder revocationCheck = typeBuilder.unionType().id("RevocationCheck")
        .with(new InfrastructureTypeAnnotation())
        .with(new ExtensibleTypeAnnotation())
        .of(standardRevocationCheck)
        .of(customOcspResponder)
        .of(crlFile);

    type.addField()
        .with(new ParameterDslAnnotation(true, false))
        .with(new ExpressionSupportAnnotation(NOT_SUPPORTED))
        .with(new LayoutTypeAnnotation(LayoutModel.builder().order(2).build()))
        .key(TLS_REVOCATION_CHECK_ELEMENT_IDENTIFIER)
        .required(false)
        .value(revocationCheck);
  }

}
