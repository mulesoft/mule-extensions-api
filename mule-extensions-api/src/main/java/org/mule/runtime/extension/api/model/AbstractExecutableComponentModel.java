/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.model;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;

import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.meta.MuleVersion;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.ComponentVisibility;
import org.mule.runtime.api.meta.model.ConnectableComponentModel;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.OutputModel;
import org.mule.runtime.api.meta.model.data.sample.SampleDataProviderModel;
import org.mule.runtime.api.meta.model.deprecated.DeprecationModel;
import org.mule.runtime.api.meta.model.display.DisplayModel;
import org.mule.runtime.api.meta.model.error.ErrorModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.notification.HasNotifications;
import org.mule.runtime.api.meta.model.notification.NotificationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterGroupModel;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;


/**
 * Base class for immutable implementations of a {@link ComponentModel}
 *
 * @since 1.0
 */
public abstract class AbstractExecutableComponentModel extends AbstractComponentModel implements ConnectableComponentModel,
    HasNotifications {

  private final OutputModel output;
  private final OutputModel outputAttributes;
  private final boolean transactional;
  private final boolean requiresConnection;
  private final boolean supportsStreaming;
  private final Set<NotificationModel> notifications;
  private final SampleDataProviderModel sampleDataProviderModel;

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param output               an {@link OutputModel} which represents the component's output content
   * @param outputAttributes     an {@link OutputModel} which represents the component's attributes on the output {@link Message}
   * @param requiresConnection   whether this component requires connectivity
   * @param transactional        whether this component supports transactions
   * @param supportsStreaming    whether this component supports streaming
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param stereotype           the {@link StereotypeModel stereotype} of this component
   * @param visibility           the model's {@link ComponentVisibility}
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @throws IllegalArgumentException if {@code name} is blank
   * @deprecated This constructor is deprecated and will be removed in Mule 5. Use
   *             {@link #AbstractExecutableComponentModel(String, String, List, OutputModel, OutputModel, boolean, boolean, boolean, DisplayModel, Set, StereotypeModel, Set, List, Set)}
   *             instead
   */
  @Deprecated
  protected AbstractExecutableComponentModel(String name,
                                             String description,
                                             List<ParameterGroupModel> parameterGroupModels,
                                             OutputModel output,
                                             OutputModel outputAttributes,
                                             boolean requiresConnection,
                                             boolean transactional,
                                             boolean supportsStreaming,
                                             DisplayModel displayModel,
                                             Set<ErrorModel> errors,
                                             StereotypeModel stereotype,
                                             ComponentVisibility visibility,
                                             Set<ModelProperty> modelProperties,
                                             List<? extends NestableElementModel> nestedComponents) {
    this(name, description, parameterGroupModels, output, outputAttributes, requiresConnection, transactional, supportsStreaming,
         displayModel, errors, stereotype, visibility, modelProperties, nestedComponents, emptySet());
  }

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param output               an {@link OutputModel} which represents the component's output content
   * @param outputAttributes     an {@link OutputModel} which represents the component's attributes on the output {@link Message}
   * @param requiresConnection   whether this component requires connectivity
   * @param transactional        whether this component supports transactions
   * @param supportsStreaming    whether this component supports streaming
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param stereotype           the {@link StereotypeModel stereotype} of this component
   * @param visibility           the model's {@link ComponentVisibility}
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @param notifications        A {@link Set} of {@link NotificationModel} which describes the component's notifications
   * @throws IllegalArgumentException if {@code name} is blank
   */
  protected AbstractExecutableComponentModel(String name,
                                             String description,
                                             List<ParameterGroupModel> parameterGroupModels,
                                             OutputModel output,
                                             OutputModel outputAttributes,
                                             boolean requiresConnection,
                                             boolean transactional,
                                             boolean supportsStreaming,
                                             DisplayModel displayModel,
                                             Set<ErrorModel> errors,
                                             StereotypeModel stereotype,
                                             ComponentVisibility visibility,
                                             Set<ModelProperty> modelProperties,
                                             List<? extends NestableElementModel> nestedComponents,
                                             Set<NotificationModel> notifications) {
    this(name, description, parameterGroupModels, output, outputAttributes, requiresConnection, transactional,
         supportsStreaming, displayModel, errors, stereotype, visibility, modelProperties, nestedComponents, notifications,
         null);
  }

  /**
   * Creates a new instance
   *
   * @param name                 the model's name
   * @param description          the model's description
   * @param parameterGroupModels a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param output               an {@link OutputModel} which represents the component's output content
   * @param outputAttributes     an {@link OutputModel} which represents the component's attributes on the output {@link Message}
   * @param requiresConnection   whether this component requires connectivity
   * @param transactional        whether this component supports transactions
   * @param supportsStreaming    whether this component supports streaming
   * @param displayModel         a model which contains directive about how this component is displayed in the UI
   * @param stereotype           the {@link StereotypeModel stereotype} of this component
   * @param visibility           the model's {@link ComponentVisibility}
   * @param modelProperties      A {@link Set} of custom properties which extend this model
   * @param nestedComponents     a {@link List} with the components contained by this model
   * @param notifications        A {@link Set} of {@link NotificationModel} which describes the component's notifications
   * @param deprecationModel     a {@link DeprecationModel} describing if the component is deprecated. A null value means it is
   *                             not deprecated.
   * @throws IllegalArgumentException if {@code name} is blank
   */
  protected AbstractExecutableComponentModel(String name,
                                             String description,
                                             List<ParameterGroupModel> parameterGroupModels,
                                             OutputModel output,
                                             OutputModel outputAttributes,
                                             boolean requiresConnection,
                                             boolean transactional,
                                             boolean supportsStreaming,
                                             DisplayModel displayModel,
                                             Set<ErrorModel> errors,
                                             StereotypeModel stereotype,
                                             ComponentVisibility visibility,
                                             Set<ModelProperty> modelProperties,
                                             List<? extends NestableElementModel> nestedComponents,
                                             Set<NotificationModel> notifications,
                                             DeprecationModel deprecationModel) {
    this(name, description, parameterGroupModels, output, outputAttributes, requiresConnection, transactional,
         supportsStreaming, displayModel, errors, stereotype, visibility, modelProperties, nestedComponents, notifications,
         null, null);
  }

  /**
   * Creates a new instance
   *
   * @param name                    the model's name
   * @param description             the model's description
   * @param parameterGroupModels    a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param output                  an {@link OutputModel} which represents the component's output content
   * @param outputAttributes        an {@link OutputModel} which represents the component's attributes on the output
   *                                {@link Message}
   * @param requiresConnection      whether this component requires connectivity
   * @param transactional           whether this component supports transactions
   * @param supportsStreaming       whether this component supports streaming
   * @param displayModel            a model which contains directive about how this component is displayed in the UI
   * @param stereotype              the {@link StereotypeModel stereotype} of this component
   * @param visibility              the model's {@link ComponentVisibility}
   * @param modelProperties         A {@link Set} of custom properties which extend this model
   * @param nestedComponents        a {@link List} with the components contained by this model
   * @param notifications           A {@link Set} of {@link NotificationModel} which describes the component's notifications
   * @param deprecationModel        a {@link DeprecationModel} describing if the component is deprecated. A null value means it is
   *                                not deprecated.
   * @param sampleDataProviderModel A nullable {@link SampleDataProviderModel} which describes the component's sample data
   *                                capabilities
   * @throws IllegalArgumentException if {@code name} is blank
   * @since 1.4.0
   */
  protected AbstractExecutableComponentModel(String name,
                                             String description,
                                             List<ParameterGroupModel> parameterGroupModels,
                                             OutputModel output,
                                             OutputModel outputAttributes,
                                             boolean requiresConnection,
                                             boolean transactional,
                                             boolean supportsStreaming,
                                             DisplayModel displayModel,
                                             Set<ErrorModel> errors,
                                             StereotypeModel stereotype,
                                             ComponentVisibility visibility,
                                             Set<ModelProperty> modelProperties,
                                             List<? extends NestableElementModel> nestedComponents,
                                             Set<NotificationModel> notifications,
                                             DeprecationModel deprecationModel,
                                             SampleDataProviderModel sampleDataProviderModel) {
    this(name, description, parameterGroupModels, output, outputAttributes, requiresConnection, transactional,
         supportsStreaming, displayModel, errors, stereotype, visibility, modelProperties, nestedComponents, notifications,
         deprecationModel, sampleDataProviderModel, null);
  }

  /**
   * Creates a new instance
   *
   * @param name                    the model's name
   * @param description             the model's description
   * @param parameterGroupModels    a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param output                  an {@link OutputModel} which represents the component's output content
   * @param outputAttributes        an {@link OutputModel} which represents the component's attributes on the output
   *                                {@link Message}
   * @param requiresConnection      whether this component requires connectivity
   * @param transactional           whether this component supports transactions
   * @param supportsStreaming       whether this component supports streaming
   * @param displayModel            a model which contains directive about how this component is displayed in the UI
   * @param stereotype              the {@link StereotypeModel stereotype} of this component
   * @param visibility              the model's {@link ComponentVisibility}
   * @param modelProperties         A {@link Set} of custom properties which extend this model
   * @param nestedComponents        a {@link List} with the components contained by this model
   * @param notifications           A {@link Set} of {@link NotificationModel} which describes the component's notifications
   * @param deprecationModel        a {@link DeprecationModel} describing if the component is deprecated. A null value means it is
   *                                not deprecated.
   * @param sampleDataProviderModel A nullable {@link SampleDataProviderModel} which describes the component's sample data
   *                                capabilities
   * @param semanticTerms           a {@link Set} of semantic terms which describe the component's meaning and effect
   * @throws IllegalArgumentException if {@code name} is blank
   * @since 1.4.0
   */
  protected AbstractExecutableComponentModel(String name,
                                             String description,
                                             List<ParameterGroupModel> parameterGroupModels,
                                             OutputModel output,
                                             OutputModel outputAttributes,
                                             boolean requiresConnection,
                                             boolean transactional,
                                             boolean supportsStreaming,
                                             DisplayModel displayModel,
                                             Set<ErrorModel> errors,
                                             StereotypeModel stereotype,
                                             ComponentVisibility visibility,
                                             Set<ModelProperty> modelProperties,
                                             List<? extends NestableElementModel> nestedComponents,
                                             Set<NotificationModel> notifications,
                                             DeprecationModel deprecationModel,
                                             SampleDataProviderModel sampleDataProviderModel,
                                             Set<String> semanticTerms) {
    this(name, description, parameterGroupModels, output, outputAttributes, requiresConnection, transactional, supportsStreaming,
         displayModel, errors, stereotype,
         visibility, modelProperties, nestedComponents, notifications, deprecationModel, sampleDataProviderModel, semanticTerms,
         null);
  }

  /**
   * Creates a new instance
   *
   * @param name                    the model's name
   * @param description             the model's description
   * @param parameterGroupModels    a {@link List} with the source's {@link ParameterGroupModel parameter group models}
   * @param output                  an {@link OutputModel} which represents the component's output content
   * @param outputAttributes        an {@link OutputModel} which represents the component's attributes on the output
   *                                {@link Message}
   * @param requiresConnection      whether this component requires connectivity
   * @param transactional           whether this component supports transactions
   * @param supportsStreaming       whether this component supports streaming
   * @param displayModel            a model which contains directive about how this component is displayed in the UI
   * @param stereotype              the {@link StereotypeModel stereotype} of this component
   * @param visibility              the model's {@link ComponentVisibility}
   * @param modelProperties         A {@link Set} of custom properties which extend this model
   * @param nestedComponents        a {@link List} with the components contained by this model
   * @param notifications           A {@link Set} of {@link NotificationModel} which describes the component's notifications
   * @param deprecationModel        a {@link DeprecationModel} describing if the component is deprecated. A null value means it is
   *                                not deprecated.
   * @param sampleDataProviderModel A nullable {@link SampleDataProviderModel} which describes the component's sample data
   *                                capabilities
   * @param semanticTerms           a {@link Set} of semantic terms which describe the component's meaning and effect
   * @param minMuleVersion          the min mule version of the component
   * @throws IllegalArgumentException if {@code name} is blank
   * @since 1.5.0
   */
  protected AbstractExecutableComponentModel(String name,
                                             String description,
                                             List<ParameterGroupModel> parameterGroupModels,
                                             OutputModel output,
                                             OutputModel outputAttributes,
                                             boolean requiresConnection,
                                             boolean transactional,
                                             boolean supportsStreaming,
                                             DisplayModel displayModel,
                                             Set<ErrorModel> errors,
                                             StereotypeModel stereotype,
                                             ComponentVisibility visibility,
                                             Set<ModelProperty> modelProperties,
                                             List<? extends NestableElementModel> nestedComponents,
                                             Set<NotificationModel> notifications,
                                             DeprecationModel deprecationModel,
                                             SampleDataProviderModel sampleDataProviderModel,
                                             Set<String> semanticTerms,
                                             MuleVersion minMuleVersion) {
    super(name, description, parameterGroupModels, nestedComponents, displayModel, errors, stereotype, visibility,
          modelProperties,
          deprecationModel, semanticTerms, minMuleVersion);
    this.output = output;
    this.outputAttributes = outputAttributes;
    this.requiresConnection = requiresConnection;
    this.transactional = transactional;
    this.supportsStreaming = supportsStreaming;
    this.notifications = ImmutableSet.copyOf(notifications);
    this.sampleDataProviderModel = sampleDataProviderModel;
  }

  /**
   * {@inheritDoc}
   */
  public OutputModel getOutput() {
    return output;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public OutputModel getOutputAttributes() {
    return outputAttributes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTransactional() {
    return transactional;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean requiresConnection() {
    return requiresConnection;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supportsStreaming() {
    return supportsStreaming;
  }

  @Override
  public Set<NotificationModel> getNotificationModels() {
    return notifications;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<SampleDataProviderModel> getSampleDataProviderModel() {
    return ofNullable(sampleDataProviderModel);
  }

  @Override
  public String toString() {
    return "AbstractExecutableComponentModel{" +
        "output=" + output +
        ", outputAttributes=" + outputAttributes +
        ", transactional=" + transactional +
        ", requiresConnection=" + requiresConnection +
        ", supportsStreaming=" + supportsStreaming +
        ", notifications=" + notifications +
        ", sampleDataProviderModel=" + sampleDataProviderModel +
        "} " + super.toString();
  }
}
