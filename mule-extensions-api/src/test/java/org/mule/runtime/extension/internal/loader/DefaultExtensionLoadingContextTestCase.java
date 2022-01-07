/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader;

import static java.util.Collections.emptySet;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.api.dsl.DslResolvingContext.getDefault;
import static org.mule.runtime.extension.api.loader.ExtensionLoadingRequest.builder;

import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExtensionLoadingContextTestCase {

  private static final String KEY = "key";
  private static final String VALUE = "value";
  private ExtensionDeclarer descriptor;

  private ExtensionLoadingContext context;

  @Before
  public void before() {
    descriptor = new ExtensionDeclarer();
    context =
        new DefaultExtensionLoadingContext(descriptor, builder(getClass().getClassLoader(), getDefault(emptySet())).build());
    context.addParameter(KEY, VALUE);
  }

  @Test
  public void getDeclarationDescriptor() {
    assertThat(descriptor, is(sameInstance(context.getExtensionDeclarer())));
  }

  @Test
  public void getParameter() {
    assertThat(context.getParameter(KEY).get(), is(sameInstance(VALUE)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullParameterKey() {
    context.addParameter(null, VALUE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullParameterValue() {
    context.addParameter(KEY, null);
  }

  @Test
  public void getClassLoader() {
    assertThat(context.getExtensionClassLoader(), is(sameInstance(getClass().getClassLoader())));
  }
}
