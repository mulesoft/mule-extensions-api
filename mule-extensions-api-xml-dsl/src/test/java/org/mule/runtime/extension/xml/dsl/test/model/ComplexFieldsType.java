/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.test.model;

import org.mule.runtime.extension.api.annotation.Parameter;
import org.mule.runtime.extension.api.annotation.ParameterGroup;

import java.util.List;

public class ComplexFieldsType {

  @Parameter
  private List<ExtensibleType> extensibleTypeList;

  @Parameter
  private ComplexFieldsType recursiveChild;

  @Parameter
  private SimpleFieldsType simplePojo;

  @ParameterGroup
  private ParameterGroupType parameterGroupType;

  public List<ExtensibleType> getExtensibleTypeList() {
    return extensibleTypeList;
  }

  public void setExtensibleTypeList(List<ExtensibleType> extensibleTypeList) {
    this.extensibleTypeList = extensibleTypeList;
  }

  public ComplexFieldsType getRecursiveChild() {
    return recursiveChild;
  }

  public void setRecursiveChild(ComplexFieldsType recursiveChild) {
    this.recursiveChild = recursiveChild;
  }

  public SimpleFieldsType getSimplePojo() {
    return simplePojo;
  }

  public void setSimplePojo(SimpleFieldsType simplePojo) {
    this.simplePojo = simplePojo;
  }
}
