/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
