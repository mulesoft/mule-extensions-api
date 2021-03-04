/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import static org.mule.runtime.api.component.ComponentIdentifier.buildFromStringRepresentation;
import static org.mule.runtime.extension.api.model.notification.NotificationModelBuilder.newNotification;
import static org.mule.runtime.extension.internal.persistence.ExtensionModelTypeAdapter.NOTIFICATIONS;
import static org.mule.runtime.extension.internal.persistence.NotificationModelToIdentifierSerializer.serialize;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.notification.NotificationModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Helper class for {@link ExtensionModelTypeAdapter} which encapsulates the logic of serializing and deserializing
 * {@link NotificationModel}
 *
 * @since 1.1
 */
class NotificationModelSerializerDelegate {

  private static final String NOTIFICATION = "notification";
  private static final String METADATA_TYPE = "metadataType";
  private Map<String, NotificationModel> notificationModelRespository;
  private Gson gson;

  NotificationModelSerializerDelegate(Map<String, NotificationModel> notificationModelRespository, Gson gson) {
    this.notificationModelRespository = notificationModelRespository;
    this.gson = gson;
  }

  /**
   * Serializes a {@link Set} of {@link NotificationModel}. This Serializer will only serialize the identifier of the notification
   * model.
   *
   * @param notificationModels Notifications to serialize
   * @param out                json writer where the serialized set will be written
   * @throws IOException if an error occurs trying to serialize the notifications
   */
  void writeNotifications(Set<NotificationModel> notificationModels, JsonWriter out) throws IOException {

    TypeAdapter<MetadataType> metadataTypeTypeAdapter = gson.getAdapter(MetadataType.class);
    out.name(NOTIFICATIONS);
    out.beginArray();
    for (NotificationModel notificationModel : notificationModels) {
      writeNotification(out, notificationModel, metadataTypeTypeAdapter);
    }
    out.endArray();
  }

  private void writeNotification(JsonWriter out, NotificationModel notificationModel,
                                 TypeAdapter<MetadataType> metadataTypeTypeAdapter)
      throws IOException {
    out.beginObject();
    out.name(NOTIFICATION).value(serialize(notificationModel));
    out.name(METADATA_TYPE);
    metadataTypeTypeAdapter.write(out, notificationModel.getType());
    out.endObject();
  }

  /**
   * Given a {@link JsonArray} representing a {@link Set} of {@link NotificationModel}, it will deserialize them. Also contribute
   * with the given {@link this#notificationModelRespository}.
   *
   * @param notifications The json array
   * @return The {@link Map} with the Notification Identifier as key and the represented {@link NotificationModel}
   */
  Map<String, NotificationModel> parseNotifications(JsonArray notifications) {
    notifications.iterator().forEachRemaining(element -> {
      JsonObject notification = element.getAsJsonObject();
      String aNotification = notification.get(NOTIFICATION).getAsString();
      MetadataType metadataType = gson.fromJson(notification.get(METADATA_TYPE), MetadataType.class);
      notificationModelRespository.put(aNotification,
                                       newNotification(buildFromStringRepresentation(aNotification)).withType(metadataType)
                                           .build());
    });
    return notificationModelRespository;
  }

}
