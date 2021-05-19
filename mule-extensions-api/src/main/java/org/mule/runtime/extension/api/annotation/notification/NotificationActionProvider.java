/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.notification;

import org.mule.runtime.extension.api.notification.NotificationActionDefinition;

import java.util.Set;

/**
 * A component responsible for providing an immutable {@link Set} of {@link NotificationActionDefinition}s.
 *
 * @since 1.1
 * @see Fires
 * @see NotificationActionDefinition
 */
public interface NotificationActionProvider {

  /**
   * @return a {@link Set} of {@link NotificationActionDefinition}.
   */
  Set<NotificationActionDefinition> getNotificationActions();

}
