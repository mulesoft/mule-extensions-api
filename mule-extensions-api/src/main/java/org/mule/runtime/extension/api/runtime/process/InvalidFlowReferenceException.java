/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.process;

import static org.mule.runtime.api.component.TypedComponentIdentifier.ComponentType.UNKNOWN;
import org.mule.runtime.api.component.TypedComponentIdentifier.ComponentType;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.i18n.I18nMessage;

/**
 * A {@link MuleException} that communicates when a Component was referenced when looking for a Flow,
 * but it's kind is of a different {@link ComponentType}
 *
 * @since 1.1
 */
public class InvalidFlowReferenceException extends MuleException {

  private ComponentType componentType;

  public InvalidFlowReferenceException(I18nMessage message, ComponentType componentType) {
    super(message);
    this.componentType = componentType;
  }

  public ComponentType getComponentType() {
    return componentType == null ? UNKNOWN : componentType;
  }

}
