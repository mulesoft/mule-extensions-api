/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.internal.loader.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.mule.runtime.extension.internal.loader.util.JavaParserUtils;
import org.mule.sdk.api.annotation.param.ConfigOverride;
import org.mule.sdk.api.annotation.param.ExclusiveOptionals;

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

  @Test
  public void isExclusiveOptional() throws Exception {
    Field pojoWithSdkExclusiveOptional = MyParameterGroup.class.getDeclaredField("pojoWithSdkExclusiveOptional");
    Field pojoWithLegacyExclusiveOptional = MyParameterGroup.class.getDeclaredField("pojoWithLegacyExclusiveOptional");

    assertThat(JavaParserUtils.getExclusiveOptionalsIsOneRequired(pojoWithSdkExclusiveOptional.getType()).get(), is(true));
    assertThat(JavaParserUtils.getExclusiveOptionalsIsOneRequired(pojoWithLegacyExclusiveOptional.getType()).get(), is(true));
  }

  private static class WithConfigOverride {

    @ConfigOverride
    private Object fieldWithSdkConfigOverride;

    @org.mule.runtime.extension.api.annotation.param.ConfigOverride
    private Object fieldWithLegacyConfigOverride;

    private Object noConfigOverride;
  }

  @ExclusiveOptionals(isOneRequired = true)
  private static class PojoWithSdkExclusiveOptional {
  }

  @org.mule.runtime.extension.api.annotation.param.ExclusiveOptionals(isOneRequired = true)
  private static class PojoWithLegacyExclusiveOptional {
  }

  private static class MyParameterGroup {

    PojoWithSdkExclusiveOptional pojoWithSdkExclusiveOptional;
    PojoWithLegacyExclusiveOptional pojoWithLegacyExclusiveOptional;
  }
}
