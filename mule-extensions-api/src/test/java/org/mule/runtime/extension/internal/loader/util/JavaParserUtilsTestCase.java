/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.mule.sdk.api.annotation.param.ConfigOverride;

import java.lang.reflect.Field;

import org.junit.Test;

public class JavaParserUtilsTestCase {

  @Test
  public void isConfigOverride() throws Exception {
    Field fieldWithSdkConfigOverride = WithConfigOverride.class.getDeclaredField("fieldWithSdkConfigOverride");
    Field fieldWithLegacyConfigOverride = WithConfigOverride.class.getDeclaredField("fieldWithLegacyConfigOverride");
    Field noConfigOverride = WithConfigOverride.class.getDeclaredField("noConfigOverride");

    assertThat(JavaParserUtils.isConfigOverride(fieldWithSdkConfigOverride), is(true));
    assertThat(JavaParserUtils.isConfigOverride(fieldWithLegacyConfigOverride), is(true));
    assertThat(JavaParserUtils.isConfigOverride(noConfigOverride), is(false));
  }

  private static class WithConfigOverride {

    @ConfigOverride
    private Object fieldWithSdkConfigOverride;

    @org.mule.runtime.extension.api.annotation.param.ConfigOverride
    private Object fieldWithLegacyConfigOverride;

    private Object noConfigOverride;
  }
}
