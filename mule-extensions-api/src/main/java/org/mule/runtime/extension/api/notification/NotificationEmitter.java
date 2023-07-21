/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.notification;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.notification.ExtensionNotification;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.function.Supplier;

/**
 * This component allows firing {@link ExtensionNotification ExtensionNotifications} with both custom data and default one.
 *
 * @since 1.1
 */
@MinMuleVersion("4.1")
@NoImplement
public interface NotificationEmitter {

  /**
   * Fires an {@link ExtensionNotification} with the desired information.
   *
   * @param action the {@link NotificationActionDefinition} to use.
   * @param data   the {@link TypedValue} data to use.
   */
  void fire(NotificationActionDefinition action, TypedValue<?> data);

  /**
   * Fires an {@link ExtensionNotification} with the desired information.
   *
   * @param action    the {@link NotificationActionDefinition} to use.
   * @param dataValue a supplier for the the {@link Object} to use as value of the generated {@link DataType}.
   * @param dataType  the type of the data returned by the provided supplier.
   *
   * @since 4.2.0
   */
  default void fireLazy(NotificationActionDefinition action, Supplier<?> dataValue, DataType dataType) {
    fire(action, new TypedValue<>(dataValue.get(), dataType));
  }

}
