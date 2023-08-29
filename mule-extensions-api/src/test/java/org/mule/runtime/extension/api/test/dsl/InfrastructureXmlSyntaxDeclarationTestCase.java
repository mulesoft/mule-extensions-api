/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.dsl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeThat;
import static org.mockito.Mockito.when;
import static org.mule.runtime.api.meta.model.parameter.ParameterRole.BEHAVIOUR;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.isContent;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_CONTEXT_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_PREFIX;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.dsl.syntax.DslElementSyntax;
import org.mule.runtime.extension.api.property.InfrastructureParameterModelProperty;
import org.mule.runtime.extension.api.property.QNameModelProperty;

import com.google.common.collect.ImmutableSet;

import java.util.Optional;

import io.qameta.allure.Issue;
import javax.xml.namespace.QName;

import org.junit.Test;

public class InfrastructureXmlSyntaxDeclarationTestCase extends BaseXmlDeclarationTestCase {

  public InfrastructureXmlSyntaxDeclarationTestCase() {
    super(BEHAVIOUR);
  }

  @Test
  public void infrastructureTypeAsFieldDslSyntax() {
    MetadataType type = TYPE_LOADER.load(PojoWithInfraField.class);
    DslElementSyntax container = getSyntaxResolver().resolve(type).get();
    DslElementSyntax tls = container.getContainedElement("tls").get();
    assertTlsContextDsl(tls);
    assertAttributeName("tls", tls);
  }

  @Issue("MULE-19726")
  @Test
  public void infrastructureTypeAsFieldDslSyntaxSupportsTopLevelDeclaration() {
    MetadataType type = TYPE_LOADER.load(PojoWithInfraField.class);
    DslElementSyntax container = getSyntaxResolver().resolve(type).get();
    DslElementSyntax tls = container.getContainedElement("tls").get();
    assertTopLevelDeclarationSupportIs(true, tls);
  }

  @Issue("MULE-19726")
  @Test
  public void infrastructureTypeDslSyntaxSupportsTopLevelDeclaration() {
    MetadataType type = TYPE_LOADER.load(TlsContextFactory.class);
    DslElementSyntax result = getSyntaxResolver().resolve(type).get();
    assertTopLevelDeclarationSupportIs(true, result);
  }

  @Test
  public void infrastructureTypeDslSyntax() {
    MetadataType type = TYPE_LOADER.load(TlsContextFactory.class);
    assertTlsContextDsl(getSyntaxResolver().resolve(type).get());
  }

  @Test
  public void infrastructureParameterDslSyntax() {
    assumeThat(isContent(parameterModel), is(false));

    InfrastructureParameterModelProperty infraMP = new InfrastructureParameterModelProperty(1);
    QNameModelProperty qNameMP = new QNameModelProperty(new QName("http://www.mulesoft.org/schema/mule/tls",
                                                                  TLS_CONTEXT_ELEMENT_IDENTIFIER,
                                                                  TLS_PREFIX));
    when(parameterModel.getModelProperties()).thenReturn(ImmutableSet.of(infraMP, qNameMP));
    when(parameterModel.getModelProperty(InfrastructureParameterModelProperty.class)).thenReturn(Optional.of(infraMP));
    when(parameterModel.getModelProperty(QNameModelProperty.class)).thenReturn(Optional.of(qNameMP));
    when(parameterModel.getType()).thenReturn(TYPE_LOADER.load(TlsContextFactory.class));
    DslElementSyntax result = getSyntaxResolver().resolve(parameterModel);
    assertTlsContextDsl(result);

    assertAttributeName(PARAMETER_NAME, result);
  }

  private void assertTlsContextDsl(DslElementSyntax tls) {
    assertElementName(TLS_CONTEXT_ELEMENT_IDENTIFIER, tls);
    assertElementPrefix(TLS_PREFIX, tls);
    assertParameterChildElementDeclaration(true, tls);
    assertIsWrappedElement(false, tls);

    assertAttributeOnly("enabledProtocols", tls);
    assertAttributeOnly("enabledCipherSuites", tls);
    assertChildOnly("key-store", "key-store", tls);
    assertChildOnly("trust-store", "trust-store", tls);

    DslElementSyntax trustStore = tls.getContainedElement("trust-store").get();
    assertAttributeOnly("password", trustStore);
    assertAttributeOnly("path", trustStore);
    assertAttributeOnly("insecure", trustStore);
    assertAttributeOnly("algorithm", trustStore);

    DslElementSyntax keyStore = tls.getContainedElement("key-store").get();
    assertAttributeOnly("path", keyStore);
    assertAttributeOnly("alias", keyStore);
    assertAttributeOnly("keyPassword", keyStore);
    assertAttributeOnly("password", keyStore);
    assertAttributeOnly("algorithm", keyStore);
  }

  private void assertChildOnly(String name, String elementName, DslElementSyntax parent) {
    DslElementSyntax child = parent.getContainedElement(name).get();
    assertElementName(elementName, child);
    assertElementPrefix(parent.getPrefix(), child);
    assertAttributeDeclaration(false, child);
    assertParameterChildElementDeclaration(true, child);
    assertElementName(elementName, child);

  }

  private void assertAttributeOnly(String name, DslElementSyntax parent) {
    DslElementSyntax child = parent.getContainedElement(name).get();
    assertAttributeDeclaration(true, child);
    assertAttributeName(name, child);
    assertParameterChildElementDeclaration(false, child);
  }

  public static final class PojoWithInfraField {

    @Parameter
    private TlsContextFactory tls;

    public TlsContextFactory getTls() {
      return tls;
    }

    public void setTls(TlsContextFactory tls) {
      this.tls = tls;
    }

  }
}
