/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.dsl.model;

import org.mule.runtime.extension.api.annotation.param.Parameter;

public class ImportedContainerType {

  @Parameter
  private NotGlobalType notGlobal;

  public NotGlobalType getNotGlobal() {
    return notGlobal;
  }

  public void setNotGlobal(NotGlobalType notGlobal) {
    this.notGlobal = notGlobal;
  }

}
