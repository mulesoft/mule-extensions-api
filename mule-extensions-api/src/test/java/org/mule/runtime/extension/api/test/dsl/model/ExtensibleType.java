/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.dsl.model;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Extensible;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.util.List;

@Extensible
@Alias("aliasedExtensible")
public class ExtensibleType {

  public static final String ALIAS = "aliasedExtensible";

  @Parameter
  private String sampleString;

  @Parameter
  private Integer otherNumber;

  @Parameter
  private List<Integer> childNumbers;

  public List<Integer> getChildNumbers() {
    return childNumbers;
  }

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
