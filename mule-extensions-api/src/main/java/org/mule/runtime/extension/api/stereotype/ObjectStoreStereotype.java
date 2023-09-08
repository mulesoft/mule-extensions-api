/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.stereotype;

import static org.mule.runtime.extension.api.ExtensionConstants.OBJECT_STORE_ELEMENT_NAMESPACE;

import org.mule.runtime.api.store.ObjectStore;

/**
 * {@link StereotypeDefinition} for a generic {@link ObjectStore} definition
 *
 * @since 1.0
 */
public final class ObjectStoreStereotype implements StereotypeDefinition {

  private static final String OBJECT_STORE_NAMESPACE = OBJECT_STORE_ELEMENT_NAMESPACE.toUpperCase();

  /**
   * In order to fix an inconsistency between this stereotype and the definition on {@link MuleStereotypes#OBJECT_STORE} without
   * breaking backwards compatibility we have to change the hierarchy and set the namespace {@code OS}.
   *
   * @return
   */
  @Override
  public String getNamespace() {
    return OBJECT_STORE_NAMESPACE;
  }

  @Override
  public String getName() {
    return "OBJECT_STORE";
  }
}
