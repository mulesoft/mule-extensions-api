/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.notification;

import org.mule.runtime.api.metadata.DataType;

/**
 * Identifies a notification action to be declared by extensions and must be implemented by an {@link Enum}.
 *
 * @param <E> Enum type
 * @since 1.1
 */
public interface NotificationActionDefinition<E extends Enum<E>> {

  /**
   * Indicates the {@link Class} of the data associated to the notification
   *
   * @return the type of the notification data.
   */
  DataType getDataType();

}
