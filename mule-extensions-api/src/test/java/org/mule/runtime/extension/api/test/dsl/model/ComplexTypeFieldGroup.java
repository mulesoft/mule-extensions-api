/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.dsl.model;

import org.mule.runtime.extension.api.annotation.param.Parameter;

public class ComplexTypeFieldGroup {

  @Parameter
  private ComplexFieldsType complexFieldsType;

  public ComplexFieldsType getComplexFieldsType() {
    return complexFieldsType;
  }

}
