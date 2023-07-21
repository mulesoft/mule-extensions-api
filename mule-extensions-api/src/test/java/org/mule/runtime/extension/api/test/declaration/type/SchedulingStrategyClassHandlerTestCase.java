/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.declaration.type;

import static org.mule.runtime.internal.dsl.DslConstants.CORE_NAMESPACE;
import static org.mule.runtime.internal.dsl.DslConstants.CORE_PREFIX;
import static org.mule.runtime.internal.dsl.DslConstants.SCHEDULING_STRATEGY_ELEMENT_IDENTIFIER;

import static java.util.Collections.emptyList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataFormat;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.java.api.handler.TypeHandlerManager;
import org.mule.metadata.java.api.utils.ParsingContext;
import org.mule.runtime.api.scheduler.SchedulingStrategy;
import org.mule.runtime.extension.api.declaration.type.SchedulingStrategyClassHandler;
import org.mule.runtime.extension.api.declaration.type.annotation.QNameTypeAnnotation;

import javax.xml.namespace.QName;

import org.junit.Test;

import io.qameta.allure.Issue;

@Issue("MULE-19843")
public class SchedulingStrategyClassHandlerTestCase {

  @Test
  public void customQualifiedName() {
    BaseTypeBuilder builder = new BaseTypeBuilder(mock(MetadataFormat.class));
    new SchedulingStrategyClassHandler().handleClass(SchedulingStrategy.class,
                                                     emptyList(),
                                                     mock(TypeHandlerManager.class),
                                                     mock(ParsingContext.class),
                                                     builder);

    MetadataType builtType = builder.build();

    QName qname = builtType.getAnnotation(QNameTypeAnnotation.class).get().getValue();
    assertThat(qname.getNamespaceURI(), is(CORE_NAMESPACE));
    assertThat(qname.getPrefix(), is(CORE_PREFIX));
    assertThat(qname.getLocalPart(), is(SCHEDULING_STRATEGY_ELEMENT_IDENTIFIER));
  }
}
