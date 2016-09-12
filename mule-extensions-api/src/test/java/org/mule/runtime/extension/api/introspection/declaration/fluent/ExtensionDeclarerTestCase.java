/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.fluent;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mule.metadata.java.api.utils.JavaTypeUtils.getType;
import org.mule.NonRuntimeTestType;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.declaration.type.ExtensionsTypeLoaderFactory;

import com.mulesoft.mule.runtime.TestEeType;

import java.util.Set;

import org.junit.Test;

public class ExtensionDeclarerTestCase {

  private ExtensionDeclarer declarer = new ExtensionDeclarer();
  private ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

  @Test
  public void withTypes() {
    declarer.withType((ObjectType) typeLoader.load(NonRuntimeTestType.class));
    Set<ObjectType> types = declarer.getDeclaration().getTypes();
    assertThat(types, hasSize(1));
    ObjectType registeredType = types.iterator().next();
    assertThat(getType(registeredType), equalTo(NonRuntimeTestType.class));
  }

  @Test
  public void doNotDeclareObject() {
    assertNotDeclared(Object.class);
  }

  @Test
  public void doNotDeclareRuntimeClass() {
    assertNotDeclared(ExtensionModel.class);
  }

  @Test
  public void doNotDeclareEERuntimeClass() {
    assertNotDeclared(TestEeType.class);
  }

  private void assertNotDeclared(Class<?> type) {
    declarer.withType((ObjectType) typeLoader.load(type));
    assertThat(declarer.getDeclaration().getTypes(), hasSize(0));
  }

}
