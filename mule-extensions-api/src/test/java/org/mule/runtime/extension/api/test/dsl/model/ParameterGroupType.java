/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.test.dsl.model;

import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Text;

public class ParameterGroupType {

  @Parameter
  @Text
  private String groupedField;

  @Parameter
  @Content
  private String groupedContent;
}
