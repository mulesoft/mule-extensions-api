/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.notification;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.notification.ExtensionNotification;

/**
 * This component allows firing {@link ExtensionNotification ExtensionNotifications} with both custom data and
 * default one.
 *
 * @since 1.1
 */
@NoImplement
public interface NotificationEmitter {

  /**
   * Fires an {@link ExtensionNotification} with the desired information.
   *
   * @param action the {@link NotificationActionDefinition} to use.
   * @param data the {@link TypedValue} data to use.
   */
  void fire(NotificationActionDefinition action, TypedValue<?> data);

}
