/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.dsl.model;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.NullSafe;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;

import java.util.List;

@Alias("complex-alias")
public class ComplexFieldsType {

  public static final String ALIAS = "complex-alias";

  @Parameter
  private List<ExtensibleType> extensibleTypeList;

  @Parameter
  @Optional
  @NullSafe
  private ComplexFieldsType recursiveChild;

  @Parameter
  private SimpleFieldsType simplePojo;

  @Parameter
  @Optional
  @NullSafe
  private NotGlobalType notGlobalType;

  @ParameterGroup(name = "ParameterGroupType")
  private ParameterGroupType parameterGroupType;

  @ParameterGroup(name = "Complex Type Field Group")
  private ComplexTypeFieldGroup complexTypeFieldGroup;

  @ParameterGroup(name = "Group With Inline Declaration", showInDsl = true)
  private ComplexTypeFieldGroup inlineGroup;

  public ComplexTypeFieldGroup getInlineGroup() {
    return inlineGroup;
  }

  public ComplexTypeFieldGroup getComplexTypeFieldGroup() {
    return complexTypeFieldGroup;
  }

  public void setComplexTypeFieldGroup(ComplexTypeFieldGroup complexTypeFieldGroup) {
    this.complexTypeFieldGroup = complexTypeFieldGroup;
  }

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
