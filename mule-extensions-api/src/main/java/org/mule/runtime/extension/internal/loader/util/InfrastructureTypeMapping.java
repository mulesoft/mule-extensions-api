/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.util;

import static java.util.Optional.ofNullable;
import static org.mule.runtime.api.util.collection.Collectors.toImmutableMap;
import static org.mule.runtime.extension.api.ExtensionConstants.ERROR_MAPPINGS_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.SCHEDULING_STRATEGY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TLS_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TRANSACTIONAL_ACTION_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TRANSACTIONAL_TYPE_PARAMETER_NAME;
import static org.mule.runtime.internal.dsl.DslConstants.CORE_NAMESPACE;
import static org.mule.runtime.internal.dsl.DslConstants.CORE_PREFIX;
import static org.mule.runtime.internal.dsl.DslConstants.ERROR_MAPPING_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.SCHEDULING_STRATEGY_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_CONTEXT_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_CRL_FILE_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_CUSTOM_OCSP_RESPONDER_ELEMENT_IDENTIFIER;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_PREFIX;
import static org.mule.runtime.internal.dsl.DslConstants.TLS_STANDARD_REVOCATION_CHECK_ELEMENT_IDENTIFIER;

import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.scheduler.SchedulingStrategy;
import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.api.tx.TransactionType;
import org.mule.runtime.extension.api.error.ErrorMapping;
import org.mule.runtime.extension.api.property.QNameModelProperty;
import org.mule.runtime.extension.api.tx.OperationTransactionalAction;
import org.mule.runtime.extension.api.tx.SourceTransactionalAction;

import java.util.Map;
import java.util.Optional;

import javax.xml.namespace.QName;

import com.google.common.collect.ImmutableMap;

/**
 * Mapping for types considered of "Infrastructure", of the {@link Class} of the infrastructure type and the {@link String} name
 * of it.
 *
 * @since 4.0
 */
public final class InfrastructureTypeMapping {

  public static final String TLS_NAMESPACE_URI = "http://www.mulesoft.org/schema/mule/tls";

  private static final Map<Class<?>, InfrastructureType> MAPPING = ImmutableMap.<Class<?>, InfrastructureType>builder()
      .put(TlsContextFactory.class, new InfrastructureType(TLS_PARAMETER_NAME, 8))
      .put(SourceTransactionalAction.class, new InfrastructureType(TRANSACTIONAL_ACTION_PARAMETER_NAME, 6))
      .put(OperationTransactionalAction.class, new InfrastructureType(TRANSACTIONAL_ACTION_PARAMETER_NAME, 7))
      .put(org.mule.sdk.api.tx.OperationTransactionalAction.class, new InfrastructureType(TRANSACTIONAL_ACTION_PARAMETER_NAME, 7))
      .put(TransactionType.class, new InfrastructureType(TRANSACTIONAL_TYPE_PARAMETER_NAME, 9))
      .put(SchedulingStrategy.class, new InfrastructureType(SCHEDULING_STRATEGY_PARAMETER_NAME, 10))
      .put(ErrorMapping.class, new InfrastructureType(ERROR_MAPPINGS_PARAMETER_NAME, 11))
      .build();

  private static final Map<String, QNameModelProperty> QNAMES = ImmutableMap.<String, QNameModelProperty>builder()
      .put(SCHEDULING_STRATEGY_PARAMETER_NAME,
           new QNameModelProperty(new QName(CORE_NAMESPACE, SCHEDULING_STRATEGY_ELEMENT_IDENTIFIER, CORE_PREFIX)))
      .put(ERROR_MAPPINGS_PARAMETER_NAME,
           new QNameModelProperty(new QName(CORE_NAMESPACE, ERROR_MAPPING_ELEMENT_IDENTIFIER, CORE_PREFIX)))
      .put(TLS_PARAMETER_NAME,
           new QNameModelProperty(new QName(TLS_NAMESPACE_URI,
                                            TLS_CONTEXT_ELEMENT_IDENTIFIER,
                                            TLS_PREFIX)))
      .put(TLS_CUSTOM_OCSP_RESPONDER_ELEMENT_IDENTIFIER,
           new QNameModelProperty(new QName(TLS_NAMESPACE_URI,
                                            TLS_CUSTOM_OCSP_RESPONDER_ELEMENT_IDENTIFIER,
                                            TLS_PREFIX)))
      .put(TLS_STANDARD_REVOCATION_CHECK_ELEMENT_IDENTIFIER,
           new QNameModelProperty(new QName(TLS_NAMESPACE_URI,
                                            TLS_STANDARD_REVOCATION_CHECK_ELEMENT_IDENTIFIER,
                                            TLS_PREFIX)))
      .put(TLS_CRL_FILE_ELEMENT_IDENTIFIER,
           new QNameModelProperty(new QName(TLS_NAMESPACE_URI,
                                            TLS_CRL_FILE_ELEMENT_IDENTIFIER,
                                            TLS_PREFIX)))
      .build();

  private static final Map<ComponentIdentifier, Class<?>> IDENTIFIER_TYPE_MAPPING = MAPPING.entrySet()
      .stream()
      .filter(entry -> getQName(entry.getValue().getName()).isPresent())
      .collect(toImmutableMap(entry -> {
        final QName qName = getQName(entry.getValue().getName()).get().getValue();
        return ComponentIdentifier.builder()
            .namespaceUri(qName.getNamespaceURI())
            .namespace(qName.getPrefix())
            .name(qName.getLocalPart())
            .build();
      },
                              Map.Entry::getKey));

  private static final Map<String, ParameterDslConfiguration> DSL_CONFIGURATIONS =
      ImmutableMap.<String, ParameterDslConfiguration>builder()
          .put(SCHEDULING_STRATEGY_PARAMETER_NAME,
               ParameterDslConfiguration.builder()
                   .allowsInlineDefinition(true)
                   .allowTopLevelDefinition(false)
                   .allowsReferences(false)
                   .build())
          .put(ERROR_MAPPINGS_PARAMETER_NAME,
               ParameterDslConfiguration.builder()
                   .allowsInlineDefinition(true)
                   .allowTopLevelDefinition(false)
                   .allowsReferences(false)
                   .build())
          .put(TLS_PARAMETER_NAME,
               ParameterDslConfiguration.builder()
                   .allowsInlineDefinition(true)
                   .allowTopLevelDefinition(true)
                   .allowsReferences(true)
                   .build())
          .put(TRANSACTIONAL_ACTION_PARAMETER_NAME,
               ParameterDslConfiguration.builder()
                   .allowsInlineDefinition(false)
                   .allowTopLevelDefinition(false)
                   .allowsReferences(false)
                   .build())
          .put(TRANSACTIONAL_TYPE_PARAMETER_NAME,
               ParameterDslConfiguration.builder()
                   .allowsInlineDefinition(false)
                   .allowTopLevelDefinition(false)
                   .allowsReferences(false)
                   .build())
          .build();

  private static Map<String, String> nameMap =
      MAPPING.entrySet().stream().collect(toImmutableMap(e -> e.getKey().getName(), e -> e.getValue().getName()));

  public static Map<Class<?>, InfrastructureType> getMap() {
    return MAPPING;
  }

  public static Map<String, String> getNameMap() {
    return nameMap;
  }

  public static Optional<QNameModelProperty> getQName(String name) {
    return ofNullable(QNAMES.get(name));
  }

  public static Optional<ParameterDslConfiguration> getDslConfiguration(String name) {
    return ofNullable(DSL_CONFIGURATIONS.get(name));
  }

  public static Optional<Class<?>> getTypeFor(ComponentIdentifier compId) {
    return ofNullable(IDENTIFIER_TYPE_MAPPING.get(compId));
  }

  private InfrastructureTypeMapping() {}

  public static class InfrastructureType {

    private final String name;
    private final int sequence;

    InfrastructureType(String name, int sequence) {
      this.name = name;
      this.sequence = sequence;
    }

    public String getName() {
      return name;
    }

    public int getSequence() {
      return sequence;
    }
  }
}
