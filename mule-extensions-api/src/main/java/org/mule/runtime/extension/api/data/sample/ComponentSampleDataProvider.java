/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.data.sample;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.message.Message;
import org.mule.sdk.api.data.sample.SampleDataException;

/**
 * Contract for components capable of providing sample data.
 *
 * @since 1.4.0
 */
@NoImplement
public interface ComponentSampleDataProvider {

  /**
   * Retrieves sample data based on the component's current configuration
   *
   * @return a {@link Message} carrying sample data
   * @throws SampleDataException if resolution fails
   */
  Message getSampleData() throws SampleDataException;
}
