/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.internal.loader.declaration.type.annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.mule.runtime.extension.api.declaration.type.annotation.InfrastructureTypeAnnotation;

import io.qameta.allure.Issue;
import org.junit.Test;

@Issue("MULE-19726")
public class InfrastructureTypeAnnotationTestCase {

  @Test
  public void infrastructureTypeAnnotationIsPublic() {
    InfrastructureTypeAnnotation infrastructureTypeAnnotation = new InfrastructureTypeAnnotation();
    assertThat(infrastructureTypeAnnotation.isPublic(), is(true));
  }

}
