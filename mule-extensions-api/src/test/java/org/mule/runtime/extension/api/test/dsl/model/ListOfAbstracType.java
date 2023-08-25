/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.dsl.model;

import java.util.List;

public class ListOfAbstracType {

  private List<AbstractType> abstractTypes;

  public List<AbstractType> getAbstractTypes() {
    return abstractTypes;
  }

  public void setAbstractTypes(List<AbstractType> abstractTypes) {
    this.abstractTypes = abstractTypes;
  }
}
