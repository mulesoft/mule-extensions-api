/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.stereotype;

import org.mule.runtime.api.store.ObjectStore;

/**
 * {@link StereotypeDefinition} for a generic {@link ObjectStore} definition
 *
 * @since 1.0
 */
public class ObjectStoreStereotype extends MuleStereotypeDefinition {

  @Override
  public String getName() {
    return "OBJECT_STORE";
  }
}
