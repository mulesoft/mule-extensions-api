/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api;

import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.tls.TlsContextFactory;

/**
 * Extensions API constants
 *
 * @since 1.0
 */
public final class ExtensionConstants {

  /**
   * The name of a synthetic parameter that's automatically added to all non void operations. The meaning of it is requesting the
   * runtime to place the resulting {@link Message} on a flowVar pointed by this parameter instead of replacing the message
   * flowing through the pipeline
   */
  public static final String TARGET_ATTRIBUTE = "target";

  /**
   * The name of an attribute which allows referencing a {@link TlsContextFactory}
   */
  public static final String TLS_ATTRIBUTE_NAME = "tlsContext";

  /**
   * The name of the parameter for configuring transactional actions
   */
  public static final String TRANSACTIONAL_ACTION_PARAMETER_NAME = "transactionalAction";

  /**
   * The name of the tab in which transaction parameters should appear
   */
  public static final String TRANSACTIONAL_TAB_NAME = "Transaction";

  /**
   * The description of the parameter for configuring transactional actions
   */
  public static final String TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION =
      "The type of joining action that operations can take regarding transactions.";

  private ExtensionConstants() {}
}
