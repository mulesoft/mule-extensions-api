/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.config;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.exception.MuleException;

import java.util.concurrent.locks.Lock;

/**
 * Handles the connectivity testing on phase start according to the mule context.
 * <p/>
 * This is because when in tooling client, connectivity should not be tested at start since component initialization is lazy.
 */
@NoImplement
public interface ConfigurationInstanceConnectivityTester {

  /**
   * Key under which the {@link ConfigurationInstanceConnectivityTester} can be found in the {@link org.mule.runtime.api.artifact.Registry}
   */
  String CONFIGURATION_INSTANCE_CONNECTIVITY_TESTER_KEY = "_configurationInstanceConnectivityTester";


  /**
   * Handles connectivity testing for ConfigurationInstance
   * @param instance
   * @param connectivityTestingLock
   * @throws MuleException
   */
  void testConnectivity(ConfigurationInstance instance, Lock connectivityTestingLock) throws MuleException;

}
