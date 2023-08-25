/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.dsl.model;

import org.mule.runtime.extension.api.annotation.dsl.xml.TypeDsl;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.util.List;

@TypeDsl(allowTopLevelDefinition = true)
public class RecursiveChainB {

  @Parameter
  String fieldB;

  @Parameter
  RecursiveChainA chainA;

  @Parameter
  List<RecursiveChainA> aChains;
}
