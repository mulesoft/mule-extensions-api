/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.stereotype;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.extension.api.stereotype.MuleStereotypes.OBJECT_STORE;

import org.mule.runtime.extension.api.stereotype.ObjectStoreStereotype;

import org.junit.Test;

public class MuleStereotypesTestCase {

  @Test
  public void objectStore() {
    ObjectStoreStereotype objectStoreStereotype = new ObjectStoreStereotype();
    assertThat(OBJECT_STORE.getNamespace(), equalTo(objectStoreStereotype.getNamespace()));
    assertThat(OBJECT_STORE.getType(), equalTo(objectStoreStereotype.getName()));
  }

}
