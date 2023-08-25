/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
