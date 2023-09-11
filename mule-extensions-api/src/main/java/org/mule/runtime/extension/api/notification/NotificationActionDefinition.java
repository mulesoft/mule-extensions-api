/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.notification;

import org.mule.runtime.api.metadata.DataType;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Identifies a notification action to be declared by extensions and must be implemented by an {@link Enum}.
 *
 * @param <E> Enum type
 * @since 1.1
 */
@MinMuleVersion("4.1")
public interface NotificationActionDefinition<E extends Enum<E>>
    extends org.mule.sdk.api.notification.NotificationActionDefinition<E> {

}
