/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
