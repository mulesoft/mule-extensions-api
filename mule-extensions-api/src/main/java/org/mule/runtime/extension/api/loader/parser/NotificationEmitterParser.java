/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader.parser;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.meta.model.notification.NotificationModel;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @since 1.10.0
 */
@NoImplement
public interface NotificationEmitterParser {

  /**
   * Gets a {@link Stream} with the parsed emitted {@link NotificationModel}s.
   *
   * @param notificationMapper If the parser can only obtain a {@link String} from the DSL, it should apply this function to
   *                           convert them to the corresponding {@link NotificationModel}. If the parser can get the
   *                           corresponding {@link NotificationModel} by itself, this function shouldn't be applied.
   * @return a {@link Stream} with the parsed emitted {@link NotificationModel}s.
   */
  Stream<NotificationModel> getEmittedNotificationsStream(Function<String, Optional<NotificationModel>> notificationMapper);

}
