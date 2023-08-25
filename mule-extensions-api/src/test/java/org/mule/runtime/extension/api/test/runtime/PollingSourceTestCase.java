/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.runtime;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mule.runtime.extension.api.runtime.source.PollingSource.IDS_ON_UPDATED_WATERMARK_OS_NAME_SUFFIX;
import static org.mule.runtime.extension.api.runtime.source.PollingSource.OS_NAME_MASK;
import static org.mule.runtime.extension.api.runtime.source.PollingSource.RECENTLY_PROCESSED_IDS_OS_NAME_SUFFIX;
import static org.mule.runtime.extension.api.runtime.source.PollingSource.WATERMARK_OS_NAME_SUFFIX;

import org.junit.Test;

public class PollingSourceTestCase {

  public static final String TEST_FLOW_NAME = "myFlow";
  public static final String EXPECTED_WATERMARK_OS = "_pollingSource_myFlow/watermark";
  public static final String EXPECTED_RECENT_IDS_OS = "_pollingSource_myFlow/recently-processed-ids";
  public static final String EXPECTED_IDS_UPDATED_WATERMARK_OS = "_pollingSource_myFlow/ids-on-updated-watermark";

  @Test
  public void watermarkObjectStoreNameIsBackwardsCompatible() {
    final String watermarkOs = format(OS_NAME_MASK, TEST_FLOW_NAME, WATERMARK_OS_NAME_SUFFIX);
    assertThat(watermarkOs, is(EXPECTED_WATERMARK_OS));
  }

  @Test
  public void recentlyProcessedIdsObjectStoreNameIsBackwardsCompatible() {
    final String watermarkOs = format(OS_NAME_MASK, TEST_FLOW_NAME, RECENTLY_PROCESSED_IDS_OS_NAME_SUFFIX);
    assertThat(watermarkOs, is(EXPECTED_RECENT_IDS_OS));
  }

  @Test
  public void idsUpdatedWatermarkObjectStoreNameIsBackwardsCompatible() {
    final String watermarkOs = format(OS_NAME_MASK, TEST_FLOW_NAME, IDS_ON_UPDATED_WATERMARK_OS_NAME_SUFFIX);
    assertThat(watermarkOs, is(EXPECTED_IDS_UPDATED_WATERMARK_OS));
  }
}
