/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader.util;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;

import static org.mule.runtime.api.util.collection.Collectors.toImmutableMap;
import static org.mule.runtime.extension.api.ExtensionConstants.ERROR_MAPPINGS_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.SCHEDULING_STRATEGY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TLS_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TRANSACTIONAL_ACTION_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.TRANSACTIONAL_TYPE_PARAMETER_NAME;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;
import static org.mule.runtime.extension.internal.dsl.DslConstants.CORE_NAMESPACE;
import static org.mule.runtime.extension.internal.dsl.DslConstants.CORE_PREFIX;
import static org.mule.runtime.extension.internal.dsl.DslConstants.ERROR_MAPPING_ELEMENT_IDENTIFIER;
import static org.mule.runtime.extension.internal.dsl.DslConstants.SCHEDULING_STRATEGY_ELEMENT_IDENTIFIER;
import static org.mule.runtime.extension.internal.dsl.DslConstants.TLS_CONTEXT_ELEMENT_IDENTIFIER;
import static org.mule.runtime.extension.internal.dsl.DslConstants.TLS_CRL_FILE_ELEMENT_IDENTIFIER;
import static org.mule.runtime.extension.internal.dsl.DslConstants.TLS_CUSTOM_OCSP_RESPONDER_ELEMENT_IDENTIFIER;
import static org.mule.runtime.extension.internal.dsl.DslConstants.TLS_NAMESPACE_URI;
import static org.mule.runtime.extension.internal.dsl.DslConstants.TLS_PREFIX;
import static org.mule.runtime.extension.internal.dsl.DslConstants.TLS_STANDARD_REVOCATION_CHECK_ELEMENT_IDENTIFIER;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.scheduler.SchedulingStrategy;
import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.api.tx.TransactionType;
import org.mule.runtime.extension.api.error.ErrorMapping;
import org.mule.runtime.extension.api.property.QNameModelProperty;
import org.mule.runtime.extension.api.tx.OperationTransactionalAction;
import org.mule.runtime.extension.api.tx.SourceTransactionalAction;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.namespace.QName;

/**
 * Utilities for types considered of "Infrastructure".
 *
 * @since 4.8
 */
public class InfrastructureTypeUtils {

  private static final ParameterDslConfiguration transactionalActionParameterDslConfiguration =
      ParameterDslConfiguration.builder()
          .allowsInlineDefinition(false)
          .allowTopLevelDefinition(false)
          .allowsReferences(false)
          .build();

  private static final Collection<InfrastructureType> INFRASTRUCTURE_TYPES = asList(
                                                                                    new InfrastructureType(TlsContextFactory.class,
                                                                                                           TLS_PARAMETER_NAME,
                                                                                                           8,
                                                                                                           new QNameModelProperty(new QName(TLS_NAMESPACE_URI,
                                                                                                                                            TLS_CONTEXT_ELEMENT_IDENTIFIER,
                                                                                                                                            TLS_PREFIX)),
                                                                                                           ParameterDslConfiguration
                                                                                                               .builder()
                                                                                                               .allowsInlineDefinition(true)
                                                                                                               .allowTopLevelDefinition(true)
                                                                                                               .allowsReferences(true)
                                                                                                               .build()),
                                                                                    new InfrastructureType(TLS_CUSTOM_OCSP_RESPONDER_ELEMENT_IDENTIFIER,
                                                                                                           new QNameModelProperty(new QName(TLS_NAMESPACE_URI,
                                                                                                                                            TLS_CUSTOM_OCSP_RESPONDER_ELEMENT_IDENTIFIER,
                                                                                                                                            TLS_PREFIX))),
                                                                                    new InfrastructureType(TLS_STANDARD_REVOCATION_CHECK_ELEMENT_IDENTIFIER,
                                                                                                           new QNameModelProperty(new QName(TLS_NAMESPACE_URI,
                                                                                                                                            TLS_STANDARD_REVOCATION_CHECK_ELEMENT_IDENTIFIER,
                                                                                                                                            TLS_PREFIX))),
                                                                                    new InfrastructureType(TLS_CRL_FILE_ELEMENT_IDENTIFIER,
                                                                                                           new QNameModelProperty(new QName(TLS_NAMESPACE_URI,
                                                                                                                                            TLS_CRL_FILE_ELEMENT_IDENTIFIER,
                                                                                                                                            TLS_PREFIX))),
                                                                                    new InfrastructureType(SourceTransactionalAction.class,
                                                                                                           TRANSACTIONAL_ACTION_PARAMETER_NAME,
                                                                                                           6,
                                                                                                           transactionalActionParameterDslConfiguration),
                                                                                    new InfrastructureType(OperationTransactionalAction.class,
                                                                                                           TRANSACTIONAL_ACTION_PARAMETER_NAME,
                                                                                                           7,
                                                                                                           transactionalActionParameterDslConfiguration),
                                                                                    new InfrastructureType(org.mule.sdk.api.tx.OperationTransactionalAction.class,
                                                                                                           TRANSACTIONAL_ACTION_PARAMETER_NAME,
                                                                                                           7,
                                                                                                           transactionalActionParameterDslConfiguration),
                                                                                    new InfrastructureType(TransactionType.class,
                                                                                                           TRANSACTIONAL_TYPE_PARAMETER_NAME,
                                                                                                           9,
                                                                                                           ParameterDslConfiguration
                                                                                                               .builder()
                                                                                                               .allowsInlineDefinition(false)
                                                                                                               .allowTopLevelDefinition(false)
                                                                                                               .allowsReferences(false)
                                                                                                               .build()),
                                                                                    new InfrastructureType(SchedulingStrategy.class,
                                                                                                           SCHEDULING_STRATEGY_PARAMETER_NAME,
                                                                                                           10,
                                                                                                           new QNameModelProperty(new QName(CORE_NAMESPACE,
                                                                                                                                            SCHEDULING_STRATEGY_ELEMENT_IDENTIFIER,
                                                                                                                                            CORE_PREFIX)),
                                                                                                           ParameterDslConfiguration
                                                                                                               .builder()
                                                                                                               .allowsInlineDefinition(true)
                                                                                                               .allowTopLevelDefinition(false)
                                                                                                               .allowsReferences(false)
                                                                                                               .build()),
                                                                                    new InfrastructureType(ErrorMapping.class,
                                                                                                           ERROR_MAPPINGS_PARAMETER_NAME,
                                                                                                           11,
                                                                                                           new QNameModelProperty(new QName(CORE_NAMESPACE,
                                                                                                                                            ERROR_MAPPING_ELEMENT_IDENTIFIER,
                                                                                                                                            CORE_PREFIX)),
                                                                                                           ParameterDslConfiguration
                                                                                                               .builder()
                                                                                                               .allowsInlineDefinition(true)
                                                                                                               .allowTopLevelDefinition(false)
                                                                                                               .allowsReferences(false)
                                                                                                               .build()));

  private static final Map<Class<?>, InfrastructureType> MAPPING =
      INFRASTRUCTURE_TYPES.stream().filter(infrastructureType -> infrastructureType.getClazz().isPresent())
          .collect(toImmutableMap(infrastructureType -> infrastructureType.getClazz().get(), identity()));

  private static final Map<ComponentIdentifier, Class<?>> IDENTIFIER_TYPE_MAPPING = INFRASTRUCTURE_TYPES
      .stream()
      .filter(infrastructureType -> infrastructureType.getClazz().isPresent())
      .filter(infrastructureType -> infrastructureType.getQNameModelProperty().isPresent())
      .collect(toImmutableMap(infrastructureType -> {
        final QName qName = infrastructureType.getQNameModelProperty().get().getValue();
        return ComponentIdentifier.builder()
            .namespaceUri(qName.getNamespaceURI())
            .namespace(qName.getPrefix())
            .name(qName.getLocalPart())
            .build();
      },
                              infrastructureType -> infrastructureType.getClazz().get()));

  private static final Map<String, String> CLASS_NAME_MAP =
      MAPPING.entrySet().stream().collect(toImmutableMap(e -> e.getKey().getName(), e -> e.getValue().getName()));

  private static final Map<String, List<InfrastructureType>> NAME_MAP =
      INFRASTRUCTURE_TYPES.stream().collect(groupingBy(InfrastructureType::getName));

  /**
   * @return all the infrastructure types available.
   */
  public static Collection<InfrastructureType> getInfrastructureTypes() {
    return INFRASTRUCTURE_TYPES;
  }

  /**
   * @param infrastructureTypeClass {@link Class} of the infrastructure type to obtain.
   * @return the infrastructure type of the given {@link Class}.
   */
  public static InfrastructureType getInfrastructureType(Class<?> infrastructureTypeClass) {
    return MAPPING.get(infrastructureTypeClass);
  }

  /**
   * @param name infrastructure type name.
   * @return the infrastructure types with the given {@code name}.
   */
  public static Collection<InfrastructureType> getInfrastructureTypes(String name) {
    return NAME_MAP.get(name);
  }

  /**
   * @param fieldType {@link MetadataType} infrastructure type.
   * @return infrastructure type name if the given {@link MetadataType} corresponds to an infrastructure type.
   */
  public static Optional<String> getInfrastructureParameterName(MetadataType fieldType) {
    return getId(fieldType).map(CLASS_NAME_MAP::get);
  }

  /**
   * @param compId {@link ComponentIdentifier} of an infrastructure type.
   * @return the {@link Class} of the infrastructure type identified by the given {@link ComponentIdentifier}.
   */
  public static Optional<Class<?>> getTypeFor(ComponentIdentifier compId) {
    return ofNullable(IDENTIFIER_TYPE_MAPPING.get(compId));
  }

  private InfrastructureTypeUtils() {}

  /**
   * Representation of an infrastructure type.
   */
  public static class InfrastructureType {

    private final Class<?> clazz;
    private final String name;
    private final int sequence;
    private final QNameModelProperty qNameModelProperty;
    private final ParameterDslConfiguration parameterDslConfiguration;

    InfrastructureType(Class<?> clazz,
                       String name,
                       int sequence,
                       QNameModelProperty qNameModelProperty,
                       ParameterDslConfiguration parameterDslConfiguration) {
      this.clazz = clazz;
      this.name = name;
      this.sequence = sequence;
      this.qNameModelProperty = qNameModelProperty;
      this.parameterDslConfiguration = parameterDslConfiguration;
    }

    public InfrastructureType(Class<?> clazz,
                              String name,
                              int sequence,
                              ParameterDslConfiguration parameterDslConfiguration) {
      this(clazz, name, sequence, null, parameterDslConfiguration);
    }

    public InfrastructureType(String name, QNameModelProperty qNameModelProperty) {
      this(null, name, -1, qNameModelProperty, null);
    }

    public Optional<Class<?>> getClazz() {
      return ofNullable(clazz);
    }

    public String getName() {
      return name;
    }

    public int getSequence() {
      return sequence;
    }

    public Optional<QNameModelProperty> getQNameModelProperty() {
      return ofNullable(qNameModelProperty);
    }

    public Optional<ParameterDslConfiguration> getDslConfiguration() {
      return ofNullable(parameterDslConfiguration);
    }

  }

}
