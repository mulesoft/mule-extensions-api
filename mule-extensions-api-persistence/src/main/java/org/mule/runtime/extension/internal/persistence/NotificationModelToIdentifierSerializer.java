/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

import static java.util.Collections.emptyMap;
import static org.mule.runtime.api.component.ComponentIdentifier.buildFromStringRepresentation;
import static org.mule.runtime.extension.api.model.notification.NotificationModelBuilder.newNotification;

import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.api.meta.model.notification.NotificationModel;

import java.util.Map;

/**
 * JSON Serializer for {@link NotificationModel} instances as notification identifiers.
 *
 * @since 1.1
 */
public class NotificationModelToIdentifierSerializer {

  /**
   * Deserializes a notification model identifier into a {@link NotificationModel}. Uses a Notification Model Repository to be
   * able to rebuild the entire notification model.
   *
   * @param notificationIdentifier      Notification following the following structure {@code nameSpace:notificationIdentifier}
   * @param notificationModelRepository Repository of already built {@link NotificationModel notification models}
   * @return A {@link NotificationModel} representing the given notificationIdentifier
   */
  public static NotificationModel deserialize(String notificationIdentifier,
                                              Map<String, NotificationModel> notificationModelRepository) {
    return notificationModelRepository.get(notificationIdentifier);
  }

  /**
   * Deserializes a notification model identifier into an {@link NotificationModel}.
   *
   * @param notificationIdentifier Notification following the following structure {@code nameSpace:notificationIdentifier}
   * @return A {@link NotificationModel} representing the given notificationIdentifier
   */
  public static NotificationModel deserialize(String notificationIdentifier) {
    return deserialize(notificationIdentifier, emptyMap());
  }

  /**
   * Serializes a {@link NotificationModel} to the following structure: {@code nameSpace:notificationIdentifier}
   *
   * @param notificationModel Notification to serialize
   * @return Serialized notification
   */
  public static String serialize(NotificationModel notificationModel) {
    return notificationModel.getNamespace() + ":" + notificationModel.getIdentifier();
  }
}
