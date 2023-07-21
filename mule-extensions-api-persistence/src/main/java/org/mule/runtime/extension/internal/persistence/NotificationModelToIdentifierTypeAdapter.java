/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;

import static org.mule.runtime.extension.internal.persistence.NotificationModelToIdentifierSerializer.deserialize;
import static org.mule.runtime.extension.internal.persistence.NotificationModelToIdentifierSerializer.serialize;

import org.mule.runtime.api.component.ComponentIdentifier;
import org.mule.runtime.api.meta.model.notification.NotificationModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * {@link TypeAdapter} implementation for {@link NotificationModel}, which serializes it as notification identifiers. A
 * notification identifier follows the {@link ComponentIdentifier} structure,
 * {@code notificationNamespace:notificationIdentifier}.
 *
 * @since 1.1
 */
public class NotificationModelToIdentifierTypeAdapter extends TypeAdapter<NotificationModel> {

  private Map<String, NotificationModel> notificationModelRepository = new HashMap<>();

  public NotificationModelToIdentifierTypeAdapter(Map<String, NotificationModel> notificationModelMap) {
    notificationModelRepository = notificationModelMap;
  }

  @Override
  public void write(JsonWriter out, NotificationModel value) throws IOException {
    out.value(serialize(value));
  }

  @Override
  public NotificationModel read(JsonReader in) throws IOException {
    return deserialize(in.nextString(), notificationModelRepository);
  }
}
