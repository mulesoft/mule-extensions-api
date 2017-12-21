/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.mule.runtime.api.util.DataUnit.KB;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.time.Time;
import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.api.util.DataUnit;
import org.mule.runtime.extension.api.property.InfrastructureParameterModelProperty;

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
   * The name of a synthetic parameter that's automatically added to all non void operations. The meaning of it is to provide
   * an expression to be evaluated over the operation's output, so that can be stored in the variable pointed by the target
   * parameter.
   */
  public static final String TARGET_VALUE_PARAMETER_NAME = "targetValue";

  /**
   * Human friendly description for {@link ExtensionConstants#TARGET_VALUE_PARAMETER_NAME}
   */
  public static final String TARGET_VALUE_PARAMETER_DESCRIPTION =
      "An expression that will be evaluated against the operation's output and the outcome of that expression will be stored in the target variable";

  /**
   * The display name for the target value parameter
   */
  public static final String TARGET_VALUE_PARAMETER_DISPLAY_NAME = "Target Value";

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
   * The name of the parameter for configuring the reconnection configuration
   */
  public static final String RECONNECTION_CONFIG_PARAMETER_NAME = "reconnection";

  /**
   * The description of the parameter which configures reconnection
   */
  public static final String RECONNECTION_CONFIG_PARAMETER_DESCRIPTION = "When the application is deployed, a connectivity test "
      + "is performed on all connectors. If set to true, deployment will fail if the test doesn't pass after exhausting the "
      + "associated reconnection strategy";

  /**
   * The name of the parameter for configuring the reconnection strategy parameter
   */
  public static final String RECONNECTION_STRATEGY_PARAMETER_NAME = "reconnectionStrategy";

  /**
   * The name of the parameter for configuring the expiration policy of a dynamic configuration
   */
  public static final String EXPIRATION_POLICY_PARAMETER_NAME = "expirationPolicy";

  /**
   * The description of the expiration policy of a dynamic configuration
   */
  public static final String EXPIRATION_POLICY_DESCRIPTION = "Configures the minimum amount of time that a dynamic "
      + "configuration instance can remain idle before the runtime considers it eligible for expiration. This does not mean "
      + "that the platform will expire the instance at the exact moment that it becomes eligible. The runtime will actually "
      + "purge the instances when it sees it fit.";

  /**
   * Description of the {@code <dynamic-config-expiration>} tag
   */
  public static final String DYNAMIC_CONFIG_EXPIRATION_DESCRIPTION =
      "Configuration about how should the runtime handle the expiration of dynamic configurations";

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
   * The name of the tab in which transaction parameters should appear
   */
  public static final String REDELIVERY_TAB_NAME = "Redelivery";

  /**
   * The name of an attribute which allows referencing a {@link TlsContextFactory}
   */
  public static final String TLS_PARAMETER_NAME = "tlsContext";

  /**
   * The name of the parameter for configuring transactional actions
   */
  public static final String TRANSACTIONAL_ACTION_PARAMETER_NAME = "transactionalAction";

  /**
   * The name of the parameter for configuring the transaction type
   */
  public static final String TRANSACTIONAL_TYPE_PARAMETER_NAME = "transactionType";

  /**
   * The name of the parameter for configuring the backpressure strategy
   * @since 1.1
   */
  public static final String BACK_PRESSURE_STRATEGY_PARAMETER_NAME = "backPressureStrategy";

  /**
   * The description of the parameter for configuring the backpressure strategy
   * @since 1.1
   */
  public static final String BACK_PRESSURE_STRATEGY_PARAMETER_DESCRIPTION =
      "The strategy that the runtime will use to apply back pressure";

  /**
   * The default size of the buffer that allows for repeatable streams
   */
  public static final int DEFAULT_BYTE_STREAMING_BUFFER_SIZE = 512;

  /**
   * The default increment size of a bytes streaming buffer which will expand its capacity
   */
  public static final int DEFAULT_BYTE_STREAMING_BUFFER_INCREMENT_SIZE = 512;

  /**
   * The default maximum size that a bytes streaming buffer is allowed to expand to
   */
  public static final int DEFAULT_BYTES_STREAMING_MAX_BUFFER_SIZE = 1024;

  /**
   * The default unit which qualifies {@link #DEFAULT_BYTE_STREAMING_BUFFER_SIZE}
   */
  public static final DataUnit DEFAULT_BYTE_STREAMING_BUFFER_DATA_UNIT = KB;

  /**
   * The default size of the buffer that allows for repeatable objects streaming
   */
  public static final int DEFAULT_OBJECT_STREAMING_BUFFER_SIZE = 100;

  /**
   * The default increment size of an object streaming buffer which will expand its capacity
   */
  public static final int DEFAULT_OBJECT_STREAMING_BUFFER_INCREMENT_SIZE = 100;

  /**
   * The default maximum size that an objects streaming buffer is allowed to expand to
   */
  public static final int DEFAULT_OBJECT_STREAMING_MAX_BUFFER_SIZE = 500;

  /**
   * The description of the parameter for configuring transactional actions
   */
  public static final String OPERATION_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION =
      "The type of joining action that operations can take regarding transactions.";

  /**
   * The description of the parameter for configuring transactional actions
   */
  public static final String SOURCE_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION =
      "The type of beginning action that sources can take regarding transactions.";

  /**
   * The description of the parameter for configuring transaction type
   */
  public static final String TRANSACTION_TYPE_PARAMETER_DESCRIPTION =
      "The type of transaction to create. Availability will depend on the runtime version.";

  /**
   * The name of the parameter which configures pooling
   */
  public static final String POOLING_PROFILE_PARAMETER_NAME = "poolingProfile";

  /**
   * The description of the parameter which disables connection validation
   */
  public static final String POOLING_PROFILE_PARAMETER_DESCRIPTION = "Characteristics of the connection pool";

  /**
   * The default frequency at which the runtime checks for dynamic configs which became eligible for expiration
   */
  public static final Time DYNAMIC_CONFIG_EXPIRATION_FREQUENCY = new Time(5, MINUTES);

  /**
   * Contains all the names of the {@link InfrastructureParameterModelProperty infrastructure} parameters that
   * may appear in a {@link ParameterizedModel}
   */
  public static final List<String> INFRASTRUCTURE_PARAMETER_NAMES = asList(TLS_PARAMETER_NAME,
                                                                           POOLING_PROFILE_PARAMETER_NAME,
                                                                           RECONNECTION_STRATEGY_PARAMETER_NAME,
                                                                           REDELIVERY_POLICY_PARAMETER_NAME,
                                                                           TARGET_PARAMETER_NAME,
                                                                           STREAMING_STRATEGY_PARAMETER_NAME,
                                                                           TRANSACTIONAL_ACTION_PARAMETER_NAME);
  /**
   * Namespace prefis for object store type
   */
  public static final String OBJECT_STORE_ELEMENT_NAMESPACE = "os";

  private ExtensionConstants() {}
}
