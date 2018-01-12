/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.model.notification;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.api.builder.VoidTypeBuilder;
import org.mule.metadata.api.model.MetadataFormat;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.api.meta.model.notification.NotificationModel;

/**
 * Builder pattern implementation to build {@link NotificationModel} instances.
 *
 * @since 1.1
 */
public final class NotificationModelBuilder {

  private final String identifier;
  private MetadataType type;
  private String namespace;

  private NotificationModelBuilder(String name, String namespace) {
    this.identifier = name;
    this.namespace = namespace;
  }

  /**
   * Creates a builder to be able to create {@link NotificationModel} instances
   *
   * @param identifier The identifier of the {@link NotificationModel} to create.
   * @param namespace adds a namespace to the {@link NotificationModel} that is being built
   * @return An {@link NotificationModelBuilder} initialized with the {@code typeName}
   */
  public static NotificationModelBuilder newNotification(String identifier, String namespace) {
    return new NotificationModelBuilder(identifier, namespace);
  }

  /**
   * Creates a builder to be able to create {@link NotificationModel} instances from a notification {@link ComponentIdentifier}
   *
   * @param identifier The identifier of the notification to create.
   * @return An {@link NotificationModelBuilder} initialized with the identifiers name and namespace
   */
  public static NotificationModelBuilder newNotification(ComponentIdentifier identifier) {
    return newNotification(identifier.getName(), identifier.getNamespace());
  }

  /**
   * @param type the type of data the {@link NotificationModel} provides
   * @return the contributed {@link NotificationModelBuilder}
   */
  public NotificationModelBuilder withType(MetadataType type) {
    this.type = type;
    return this;
  }

  /**
   * @return a new {@link NotificationModel} instance
   */
  public NotificationModel build() {
    return new ImmutableNotificationModel(namespace, identifier,
                                          type == null ? new VoidTypeBuilder(MetadataFormat.JAVA).build() : type);
  }
}
