/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.internal.loader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.internal.loader.CachedClassTypeLoader;

import org.junit.Before;
import org.junit.Test;

public class CachedClassTypeLoaderTestCase {

  private ClassTypeLoader cachedLoader;

  @Before
  public void setUp() {
    cachedLoader = new CachedClassTypeLoader(ExtensionsTypeLoaderFactory.getDefault().createTypeLoader());
  }

  @Test
  public void cacheByType() {
    final MetadataType metadataType = cachedLoader.load(TestingClass.class);

    assertThat(cachedLoader.load(TestingClass.class), sameInstance(metadataType));
  }

  @Test
  public void cacheByIdentifier() {
    final MetadataType metadataType = cachedLoader.load(TestingClass.class.getName()).get();

    assertThat(cachedLoader.load(TestingClass.class.getName()).get(), sameInstance(metadataType));
  }

  @Test
  public void cacheByTypeQueryByIdentifier() {
    final MetadataType metadataType = cachedLoader.load(TestingClass.class);

    assertThat(cachedLoader.load(TestingClass.class.getName()).get(), sameInstance(metadataType));
  }

  @Test
  public void cacheByIdentifierQueryByType() {
    final MetadataType metadataType = cachedLoader.load(TestingClass.class.getName()).get();

    assertThat(cachedLoader.load(TestingClass.class), sameInstance(metadataType));
  }
}
