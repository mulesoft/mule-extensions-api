/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.dsl.model;


import org.mule.runtime.extension.api.annotation.dsl.xml.TypeDsl;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.util.List;
import java.util.Map;

@TypeDsl(allowTopLevelDefinition = true)
public class RecursivePojo {

  @Parameter
  private RecursivePojo next;

  @Parameter
  private List<RecursivePojo> childs;

  @Parameter
  private Map<String, RecursivePojo> mappedChilds;
}
