/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.model;

import org.mule.runtime.extension.api.annotation.dsl.xml.TypeXmlHints;
import org.mule.runtime.extension.api.annotation.param.Parameter;

@TypeXmlHints(substitutionGroup = "someprefix:some-element")
public class SubstitutionGroupReferencingType {

  @Parameter
  private String sampleString;

  public String getSampleString() {
    return sampleString;
  }

  public void setSampleString(String sampleString) {
    this.sampleString = sampleString;
  }

}
