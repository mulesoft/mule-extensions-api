/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.dsl.model;

import org.mule.runtime.extension.api.annotation.dsl.xml.ParameterDsl;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Text;

public class SimpleFieldsType {

  @Parameter
  @ParameterDsl(allowReferences = false)
  private String sampleString;

  @Parameter
  private Integer otherNumber;

  @Parameter
  @Text
  private String textField;

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

  public String getTextField() {
    return textField;
  }

  public void setTextField(String textField) {
    this.textField = textField;
  }
}
