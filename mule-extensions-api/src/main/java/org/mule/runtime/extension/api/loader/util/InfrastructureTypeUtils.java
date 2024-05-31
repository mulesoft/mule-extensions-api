/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader.util;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;

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
                                                                                    new InfrastructureType(TLS_PARAMETER_NAME,
                                                                                                           new QNameModelProperty(new QName(TLS_NAMESPACE_URI,
                                                                                                                                            TLS_CONTEXT_ELEMENT_IDENTIFIER,
                                                                                                                                            TLS_PREFIX)),
                                                                                                           ParameterDslConfiguration
                                                                                                               .builder()
                                                                                                               .allowsInlineDefinition(true)
                                                                                                               .allowTopLevelDefinition(true)
                                                                                                               .allowsReferences(true)
                                                                                                               .build(),
                                                                                                           singletonList(new MetadataTypeBasedInfrastructureTypeComponent(TlsContextFactory.class,
                                                                                                                                                                          8))),
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
                                                                                    new InfrastructureType(TRANSACTIONAL_ACTION_PARAMETER_NAME,
                                                                                                           transactionalActionParameterDslConfiguration,
                                                                                                           asList(new MetadataTypeBasedInfrastructureTypeComponent(SourceTransactionalAction.class,
                                                                                                                                                                   6),
                                                                                                                  new MetadataTypeBasedInfrastructureTypeComponent(OperationTransactionalAction.class,
                                                                                                                                                                   7),
                                                                                                                  new MetadataTypeBasedInfrastructureTypeComponent(org.mule.sdk.api.tx.OperationTransactionalAction.class,
                                                                                                                                                                   7))),
                                                                                    new InfrastructureType(TRANSACTIONAL_TYPE_PARAMETER_NAME,
                                                                                                           ParameterDslConfiguration
                                                                                                               .builder()
                                                                                                               .allowsInlineDefinition(false)
                                                                                                               .allowTopLevelDefinition(false)
                                                                                                               .allowsReferences(false)
                                                                                                               .build(),
                                                                                                           singletonList(new MetadataTypeBasedInfrastructureTypeComponent(TransactionType.class,
                                                                                                                                                                          9))),
                                                                                    new InfrastructureType(SCHEDULING_STRATEGY_PARAMETER_NAME,
                                                                                                           new QNameModelProperty(new QName(CORE_NAMESPACE,
                                                                                                                                            SCHEDULING_STRATEGY_ELEMENT_IDENTIFIER,
                                                                                                                                            CORE_PREFIX)),
                                                                                                           ParameterDslConfiguration
                                                                                                               .builder()
                                                                                                               .allowsInlineDefinition(true)
                                                                                                               .allowTopLevelDefinition(false)
                                                                                                               .allowsReferences(false)
                                                                                                               .build(),
                                                                                                           singletonList(new MetadataTypeBasedInfrastructureTypeComponent(SchedulingStrategy.class,
                                                                                                                                                                          10))),
                                                                                    new InfrastructureType(ERROR_MAPPINGS_PARAMETER_NAME,
                                                                                                           new QNameModelProperty(new QName(CORE_NAMESPACE,
                                                                                                                                            ERROR_MAPPING_ELEMENT_IDENTIFIER,
                                                                                                                                            CORE_PREFIX)),
                                                                                                           ParameterDslConfiguration
                                                                                                               .builder()
                                                                                                               .allowsInlineDefinition(true)
                                                                                                               .allowTopLevelDefinition(false)
                                                                                                               .allowsReferences(false)
                                                                                                               .build(),
                                                                                                           singletonList(new MetadataTypeBasedInfrastructureTypeComponent(ErrorMapping.class,
                                                                                                                                                                          11))));

  private static final Collection<MetadataTypeBasedInfrastructureType> METADATA_TYPE_BASED_INFRASTRUCTURE_TYPES =
      INFRASTRUCTURE_TYPES.stream()
          .flatMap(infrastructureType -> infrastructureType.getMetadataTypeBasedInfrastructureTypes().stream()).collect(toList());

  private static final Map<Class<?>, MetadataTypeBasedInfrastructureType> METADATA_TYPE_BASED_INFRASTRUCTURE_TYPES_CLASS_MAPPING =
      METADATA_TYPE_BASED_INFRASTRUCTURE_TYPES.stream()
          .collect(toImmutableMap(MetadataTypeBasedInfrastructureType::getClazz, identity()));

  private static final Map<ComponentIdentifier, Class<?>> IDENTIFIER_TYPE_MAPPING = METADATA_TYPE_BASED_INFRASTRUCTURE_TYPES
      .stream()
      .filter(infrastructureType -> infrastructureType.getQNameModelProperty().isPresent())
      .collect(toImmutableMap(infrastructureType -> {
        final QName qName = infrastructureType.getQNameModelProperty().get().getValue();
        return ComponentIdentifier.builder()
            .namespaceUri(qName.getNamespaceURI())
            .namespace(qName.getPrefix())
            .name(qName.getLocalPart())
            .build();
      },
                              MetadataTypeBasedInfrastructureType::getClazz));

  private static final Map<String, String> CLASS_NAME_MAP =
      METADATA_TYPE_BASED_INFRASTRUCTURE_TYPES_CLASS_MAPPING.entrySet().stream()
          .collect(toImmutableMap(e -> e.getKey().getName(), e -> e.getValue().getName()));

  private static final Map<String, InfrastructureType> NAME_MAP =
      INFRASTRUCTURE_TYPES.stream().collect(toImmutableMap(InfrastructureType::getName, identity()));

  /**
   * @return all the infrastructure types available.
   */
  public static Collection<MetadataTypeBasedInfrastructureType> getActionableInfrastructureTypes() {
    return METADATA_TYPE_BASED_INFRASTRUCTURE_TYPES;
  }

  /**
   * @param infrastructureTypeClass {@link Class} of the infrastructure type to obtain.
   * @return the infrastructure type of the given {@link Class}.
   */
  public static MetadataTypeBasedInfrastructureType getMetadataTypeBasedInfrastructureType(Class<?> infrastructureTypeClass) {
    return METADATA_TYPE_BASED_INFRASTRUCTURE_TYPES_CLASS_MAPPING.get(infrastructureTypeClass);
  }

  /**
   * @param name infrastructure type name.
   * @return the infrastructure type with the given {@code name}.
   */
  public static InfrastructureType getInfrastructureType(String name) {
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

  private static class MetadataTypeBasedInfrastructureTypeComponent {

    public final Class<?> clazz;
    public final int sequence;

    MetadataTypeBasedInfrastructureTypeComponent(Class<?> clazz,
                                                 int sequence) {
      this.clazz = clazz;
      this.sequence = sequence;
    }

  }

  /**
   * Representation of an actionable infrastructure type.
   */
  public static class MetadataTypeBasedInfrastructureType {

    private final String name;
    private final Class<?> clazz;
    private final int sequence;
    private final InfrastructureType parentType;

    MetadataTypeBasedInfrastructureType(String name,
                                        Class<?> clazz,
                                        int sequence,
                                        InfrastructureType parentType) {
      this.clazz = clazz;
      this.name = name;
      this.sequence = sequence;
      this.parentType = parentType;
    }

    public Class<?> getClazz() {
      return clazz;
    }

    public String getName() {
      return name;
    }

    public int getSequence() {
      return sequence;
    }

    public Optional<QNameModelProperty> getQNameModelProperty() {
      return parentType.getQNameModelProperty();
    }

    public Optional<ParameterDslConfiguration> getDslConfiguration() {
      return parentType.getDslConfiguration();
    }

  }

  /**
   * Representation of an infrastructure type.
   */
  public static class InfrastructureType {

    private final String name;
    private final QNameModelProperty qNameModelProperty;
    private final ParameterDslConfiguration parameterDslConfiguration;
    private final Collection<MetadataTypeBasedInfrastructureType> metadataTypeBasedInfrastructureTypes;

    InfrastructureType(String name,
                       QNameModelProperty qNameModelProperty,
                       ParameterDslConfiguration parameterDslConfiguration,
                       Collection<MetadataTypeBasedInfrastructureTypeComponent> actionableInfrastructureTypesComponents) {
      this.name = name;
      this.qNameModelProperty = qNameModelProperty;
      this.parameterDslConfiguration = parameterDslConfiguration;
      this.metadataTypeBasedInfrastructureTypes = actionableInfrastructureTypesComponents.stream()
          .map(components -> new MetadataTypeBasedInfrastructureType(name, components.clazz, components.sequence, this))
          .collect(toList());
    }

    InfrastructureType(String name,
                       ParameterDslConfiguration parameterDslConfiguration,
                       Collection<MetadataTypeBasedInfrastructureTypeComponent> actionableInfrastructureTypesComponents) {
      this(name, null, parameterDslConfiguration, actionableInfrastructureTypesComponents);
    }

    InfrastructureType(String name, QNameModelProperty qNameModelProperty) {
      this(name, qNameModelProperty, null, emptyList());
    }

    public String getName() {
      return name;
    }

    public Optional<QNameModelProperty> getQNameModelProperty() {
      return ofNullable(qNameModelProperty);
    }

    public Optional<ParameterDslConfiguration> getDslConfiguration() {
      return ofNullable(parameterDslConfiguration);
    }

    public Collection<MetadataTypeBasedInfrastructureType> getMetadataTypeBasedInfrastructureTypes() {
      return metadataTypeBasedInfrastructureTypes;
    }

  }

}
