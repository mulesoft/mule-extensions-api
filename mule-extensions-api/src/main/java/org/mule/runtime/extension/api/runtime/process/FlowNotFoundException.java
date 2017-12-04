/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.process;

import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.i18n.I18nMessage;

/**
 * A {@link MuleException} that indicates that no Flow could be found in an application based on a given name or location.
 *
 * @since 1.1
 */
public class FlowNotFoundException extends MuleException {

  public FlowNotFoundException(I18nMessage message) {
    super(message);
  }

}
