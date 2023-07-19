/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.persistence.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.api.metadata.MetadataKeyBuilder.newKey;
import static org.mule.runtime.api.metadata.MetadataKeysContainerBuilder.getInstance;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataKeysContainerBuilder;
import org.mule.runtime.api.metadata.MetadataAttributes;
import org.mule.runtime.api.metadata.resolving.NamedTypeResolver;

import java.io.IOException;

import org.junit.Before;

public class AbstractMetadataPersistenceTestCase extends BasePersistenceTestCase {

  protected static final String CATEGORY_NAME = "categoryName";
  protected static final String RESOLVER_NAME = "resolverName";
  protected static final String OUTPUT_RESOLVER_NAME = "outputResolverName";

  protected NamedTypeResolver resolver;
  protected MetadataKeysContainerBuilder builder;
  protected MetadataAttributes attributes;
  protected MetadataKey key;

  @Before
  public void setUp() throws IOException {
    super.setUp();
    resolver = mock(NamedTypeResolver.class);
    when(resolver.getResolverName()).thenReturn(RESOLVER_NAME);
    when(resolver.getCategoryName()).thenReturn(CATEGORY_NAME);
    key = newKey("Key ID").withDisplayName("Key Name").build();
    builder = getInstance();
    attributes = getMetadataAttributes();
  }


  protected MetadataAttributes getMetadataAttributes() {
    return MetadataAttributes.builder()
        .withParameterResolver(CAR_NAME_PARAMETER_NAME, RESOLVER_NAME)
        .withCategoryName(CATEGORY_NAME)
        .withKey(key)
        .withOutputResolver(OUTPUT_RESOLVER_NAME)
        .withOutputAttributesResolver(OUTPUT_RESOLVER_NAME)
        .build();
  }

  protected void assertMetadataAttributes(MetadataAttributes metadataAttributes, MetadataAttributes expectedAttributes) {
    assertThat(metadataAttributes.getCategoryName(), is(expectedAttributes.getCategoryName()));
    assertThat(metadataAttributes.getKey().isPresent(), is(expectedAttributes.getKey().isPresent()));
    assertThat(metadataAttributes.getOutputAttributesResolverName(), is(expectedAttributes.getOutputAttributesResolverName()));
    assertThat(metadataAttributes.getOutputResolverName(), is(expectedAttributes.getOutputResolverName()));
    assertThat(metadataAttributes.getParameterResolverName(CAR_NAME_PARAMETER_NAME), is(RESOLVER_NAME));
  }
}
