/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_ABSTRACT_BYTE_STREAMING_STRATEGY_QNAME;
import static org.mule.runtime.extension.api.util.XmlModelUtils.MULE_ABSTRACT_OBJECT_STREAMING_STRATEGY_QNAME;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.addStreamingParameter;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.declaration.fluent.ExecutableComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.declaration.type.StreamingStrategyTypeBuilder;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.internal.property.PagedOperationModelProperty;

import java.io.InputStream;

import javax.xml.namespace.QName;

/**
 * Adds infrastructure parameters to sources and operations which returns {@link InputStream}
 * objects.
 *
 * @since 1.0
 */
public class StreamingDeclarationEnricher implements DeclarationEnricher {

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    new IdempotentDeclarationWalker() {

      @Override
      protected void onOperation(OperationDeclaration declaration) {
        enrich(declaration);
      }

      @Override
      protected void onSource(SourceDeclaration declaration) {
        enrich(declaration);
      }
    }.walk(extensionLoadingContext.getExtensionDeclarer().getDeclaration());
  }

  private void enrich(ExecutableComponentDeclaration declaration) {
    if (declaration.isSupportsStreaming()) {
      MetadataType type;
      QName qName;
      if (declaration.getModelProperty(PagedOperationModelProperty.class).isPresent()) {
        type = new StreamingStrategyTypeBuilder().getObjectStreamingStrategyType();
        qName = MULE_ABSTRACT_OBJECT_STREAMING_STRATEGY_QNAME;
      } else {
        type = new StreamingStrategyTypeBuilder().getByteStreamingStrategyType();
        qName = MULE_ABSTRACT_BYTE_STREAMING_STRATEGY_QNAME;
      }

      addStreamingParameter(declaration, type, qName);
    }
  }

}
