/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.dsl.model;

public class NonDefaultConstructor {

  private String superField;

  private NonDefaultConstructor() {}

  public String getSuperField() {
    return superField;
  }
}
