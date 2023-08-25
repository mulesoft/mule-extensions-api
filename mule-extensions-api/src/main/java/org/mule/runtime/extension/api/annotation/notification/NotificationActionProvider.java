/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.notification;

import org.mule.runtime.extension.api.notification.NotificationActionDefinition;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.Set;

/**
 * A component responsible for providing an immutable {@link Set} of {@link NotificationActionDefinition}s.
 *
 * @since 1.1
 * @see Fires
 * @see NotificationActionDefinition
 */
@MinMuleVersion("4.1")
public interface NotificationActionProvider {

  /**
   * @return a {@link Set} of {@link NotificationActionDefinition}.
   */
  Set<NotificationActionDefinition> getNotificationActions();

}
