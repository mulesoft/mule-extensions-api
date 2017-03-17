/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api;

import static java.util.Arrays.asList;
import static org.mule.runtime.api.util.DataUnit.KB;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.api.util.DataUnit;
import org.mule.runtime.extension.internal.property.InfrastructureParameterModelProperty;

import java.util.List;

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
  public static final String TARGET_PARAMETER_NAME = "target";

  /**
   * Human friendly description for {@link ExtensionConstants#TARGET_PARAMETER_NAME}
   */
  public static final String TARGET_PARAMETER_DESCRIPTION =
      "The name of a variable on which the operation's output will be placed";

  /**
   * The display name for the target parameter
   */
  public static final String TARGET_PARAMETER_DISPLAY_NAME = "Target Variable";

  /**
   * The name of the parameter for configuring the streaming strategy parameter
   */
  public static final String STREAMING_STRATEGY_PARAMETER_NAME = "streamingStrategy";

  /**
   * The description of the parameter for configuring the streaming strategy parameter
   */
  public static final String STREAMING_STRATEGY_PARAMETER_DESCRIPTION =
      "Configure if repeatable streams should be used and their behaviour";

  /**
   * The name of the parameter for configuring the reconnection strategy parameter
   */
  public static final String RECONNECTION_STRATEGY_PARAMETER_NAME = "reconnectionStrategy";

  /**
   * The description of the parameter for configuring the reconnection strategy parameter
   */
  public static final String RECONNECTION_STRATEGY_PARAMETER_DESCRIPTION = "A retry strategy in case of connectivity errors";

  /**
   * The name of the parameter for configuring reconnection strategy parameter
   */
  public static final String REDELIVERY_POLICY_PARAMETER_NAME = "redeliveryPolicy";

  /**
   * The description of the parameter for configuring reconnection strategy parameter
   */
  public static final String REDELIVERY_POLICY_PARAMETER_DESCRIPTION =
      "Defines a policy for processing the redelivery of the same message";

  /**
   * The name of an attribute which allows referencing a {@link TlsContextFactory}
   */
  public static final String TLS_PARAMETER_NAME = "tlsContext";

  /**
   * The name of the parameter for configuring transactional actions
   */
  public static final String TRANSACTIONAL_ACTION_PARAMETER_NAME = "transactionalAction";

  /**
   * The name of the tab in which transaction parameters should appear
   */
  public static final String TRANSACTIONAL_TAB_NAME = "Transactions";

  /**
   * The name of the tab in which streaming parameters should appear
   */
  public static final String STREAMING_TAB_NAME = "Streaming";

  /**
   * The default size of the buffer that allows for repeatable streams
   */
  public static final int DEFAULT_STREAMING_BUFFER_SIZE = 256;

  /**
   * The default increment size of a streaming buffer which will expand its capacity
   */
  public static final int DEFAULT_STREAMING_BUFFER_INCREMENT_SIZE = 256;

  /**
   * The default maximum size that a streaming buffer is allowed to expand
   */
  public static final int DEFAULT_STREAMING_MAX_BUFFER_SIZE = 1024;

  /**
   * The default unit which qualifies {@link #DEFAULT_STREAMING_BUFFER_SIZE}
   */
  public static final DataUnit DEFAULT_STREAMING_BUFFER_DATA_UNIT = KB;

  /**
   * The description of the parameter for configuring transactional actions
   */
  public static final String TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION =
      "The type of joining action that operations can take regarding transactions.";

  /**
   * The name of the parameter which disables connection validation
   */
  public static final String DISABLE_CONNECTION_VALIDATION_PARAMETER_NAME = "disableValidation";

  /**
   * The description of the parameter which disables connection validation
   */
  public static final String DISABLE_CONNECTION_VALIDATION_PARAMETER_DESCRIPTION = "Disables connection validation";

  /**
   * The name of the parameter which configures pooling
   */
  public static final String POOLING_PROFILE_PARAMETER_NAME = "poolingProfile";

  /**
   * The description of the parameter which disables connection validation
   */
  public static final String POOLING_PROFILE_PARAMETER_DESCRIPTION = "Characteristics of the connection pool";

  /**
   * Contains all the names of the {@link InfrastructureParameterModelProperty infrastructure} parameters that
   * may appear in a {@link ParameterizedModel}
   */
  public static final List<String> INFRASTRUCTURE_PARAMETER_NAMES = asList(TLS_PARAMETER_NAME,
                                                                           POOLING_PROFILE_PARAMETER_NAME,
                                                                           DISABLE_CONNECTION_VALIDATION_PARAMETER_NAME,
                                                                           RECONNECTION_STRATEGY_PARAMETER_NAME,
                                                                           REDELIVERY_POLICY_PARAMETER_NAME,
                                                                           TARGET_PARAMETER_NAME,
                                                                           STREAMING_STRATEGY_PARAMETER_NAME,
                                                                           TRANSACTIONAL_ACTION_PARAMETER_NAME);

  private ExtensionConstants() {}
}
