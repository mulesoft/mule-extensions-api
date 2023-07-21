/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
