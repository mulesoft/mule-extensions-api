/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.notification;

import org.mule.runtime.api.notification.Notification.Action;

/**
 * {@link Action} produced by extensions.
 */
public class ExtensionAction implements Action {

  private final String namespace;
  private final String id;

  public ExtensionAction(String namespace, String id) {
    this.namespace = namespace;
    this.id = id;
  }

  @Override
  public String getNamespace() {
    return namespace;
  }

  @Override
  public String getIdentifier() {
    return id;
  }

}
