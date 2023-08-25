/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.dsl.model;

import org.mule.runtime.extension.api.annotation.param.Parameter;

public class ChildOfAbstractType extends AbstractType {

  @Parameter
  private String sampleString;

  @Parameter
  private Integer otherNumber;

  public String getSampleString() {
    return sampleString;
  }

  public void setSampleString(String sampleString) {
    this.sampleString = sampleString;
  }

  public Integer getOtherNumber() {
    return otherNumber;
  }

  public void setOtherNumber(Integer otherNumber) {
    this.otherNumber = otherNumber;
  }
}
