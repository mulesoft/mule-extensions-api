/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.impl.DefaultAnyType;
import org.mule.metadata.api.model.impl.DefaultArrayType;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ExtensionObjectClassTypeHandlerTestCase {

  private final ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

  @Test
  public void listOfObjectsMetadataType() {
    List<Object> list = new ArrayList<>();
    MetadataType type = typeLoader.load(list.getClass());

    assertThat(type, is(instanceOf(DefaultArrayType.class)));
    assertThat(((DefaultArrayType) type).getType(), is(instanceOf(DefaultAnyType.class)));
  }

}
