/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.extension.api.util.ExtensionDependencyUtils.validate;
import org.mule.runtime.api.meta.model.ExternalDependencyModel;

import org.junit.Test;

public class ExtensionDependencyUtilsTestCase {

  private static final String NAME = "Test dependency";
  private static final String GROUP_ID = "my.test.org";
  private static final String ARTIFACT_ID = "testArtifact";
  private static final String WRONG_ID = "wrong";
  private static final String VERSION = "1.0.0";

  @Test
  public void differentGroupId() {
    ExternalDependencyModel dependencyModel = ExternalDependencyModel.builder()
        .withName(NAME)
        .withArtifactId(ARTIFACT_ID)
        .withGroupId(GROUP_ID)
        .build();
    assertThat(validate(dependencyModel, WRONG_ID, ARTIFACT_ID, VERSION), is(false));
  }

  @Test
  public void differentArtifactId() {
    ExternalDependencyModel dependencyModel = ExternalDependencyModel.builder()
        .withName(NAME)
        .withArtifactId(ARTIFACT_ID)
        .withGroupId(GROUP_ID)
        .build();
    assertThat(validate(dependencyModel, GROUP_ID, WRONG_ID, VERSION), is(false));
  }

  @Test
  public void lowerVersion() {
    ExternalDependencyModel dependencyModel = ExternalDependencyModel.builder()
        .withName(NAME)
        .withArtifactId(ARTIFACT_ID)
        .withGroupId(GROUP_ID)
        .withMinVersion("2.0")
        .build();
    assertThat(validate(dependencyModel, GROUP_ID, ARTIFACT_ID, VERSION), is(false));
  }

  @Test
  public void newerVersion() {
    ExternalDependencyModel dependencyModel = ExternalDependencyModel.builder()
        .withName(NAME)
        .withArtifactId(ARTIFACT_ID)
        .withGroupId(GROUP_ID)
        .withMaxVersion("0.5")
        .build();
    assertThat(validate(dependencyModel, GROUP_ID, ARTIFACT_ID, VERSION), is(false));
  }

  @Test
  public void withoutVersion() {
    ExternalDependencyModel dependencyModel = ExternalDependencyModel.builder()
        .withName(NAME)
        .withArtifactId(ARTIFACT_ID)
        .withGroupId(GROUP_ID)
        .build();
    assertThat(validate(dependencyModel, GROUP_ID, ARTIFACT_ID, VERSION), is(true));
  }

  @Test
  public void versionInRange() {
    ExternalDependencyModel dependencyModel = ExternalDependencyModel.builder()
        .withName(NAME)
        .withArtifactId(ARTIFACT_ID)
        .withGroupId(GROUP_ID)
        .withMinVersion("0.1")
        .withMaxVersion(VERSION)
        .build();
    assertThat(validate(dependencyModel, GROUP_ID, ARTIFACT_ID, VERSION), is(true));
  }

  @Test
  public void exactVersion() {
    ExternalDependencyModel dependencyModel = ExternalDependencyModel.builder()
        .withName(NAME)
        .withArtifactId(ARTIFACT_ID)
        .withGroupId(GROUP_ID)
        .withMinVersion(VERSION)
        .withMaxVersion(VERSION)
        .build();
    assertThat(validate(dependencyModel, GROUP_ID, ARTIFACT_ID, VERSION), is(true));
  }

  @Test
  public void versionOutOfRange() {
    ExternalDependencyModel dependencyModel = ExternalDependencyModel.builder()
        .withName(NAME)
        .withArtifactId(ARTIFACT_ID)
        .withGroupId(GROUP_ID)
        .withMinVersion("0.1")
        .withMaxVersion("0.5")
        .build();
    assertThat(validate(dependencyModel, GROUP_ID, WRONG_ID, VERSION), is(false));
  }

}
