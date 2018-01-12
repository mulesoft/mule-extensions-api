/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
   * @param notificationIdentifier Notification following the following structure {@code nameSpace:notificationIdentifier}
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
