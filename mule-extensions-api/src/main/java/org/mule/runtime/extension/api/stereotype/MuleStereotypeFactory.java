/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.stereotype;

import static org.mule.runtime.api.meta.model.stereotype.StereotypeModelBuilder.newStereotype;
import static org.mule.runtime.internal.dsl.DslConstants.CORE_PREFIX;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;

/**
 * //TODO
 */
public final class MuleStereotypeFactory {

  private static final String STEREOTYPE_NAMESPACE = CORE_PREFIX.toUpperCase();

  private MuleStereotypeFactory() {}

  public static StereotypeDefinition processorDefinition() {
    return new ProcessorStereotype();
  }

  public static StereotypeModel processor() {
    return newStereotype(processorDefinition().getName(), STEREOTYPE_NAMESPACE).build();
  }

  public static StereotypeDefinition sourceDefinition() {
    return new SourceStereotype();
  }

  public static StereotypeModel source() {
    return newStereotype(sourceDefinition().getName(), STEREOTYPE_NAMESPACE).build();
  }

  public static StereotypeDefinition validatorDefinition() {
    return new ValidatorStereotype();
  }

  public static StereotypeModel validator() {
    return newStereotype(validatorDefinition().getName(), STEREOTYPE_NAMESPACE).build();
  }

  public static StereotypeDefinition flowDefinition() {
    return new FlowStereotype();
  }

  public static StereotypeModel flow() {
    return newStereotype(flowDefinition().getName(), STEREOTYPE_NAMESPACE).build();
  }

  public static StereotypeDefinition errorHandlerDefinition() {
    return new ErrorHandlerStereotype();
  }

  public static StereotypeModel errorHandler() {
    return newStereotype(errorHandlerDefinition().getName(), STEREOTYPE_NAMESPACE).build();
  }

  public static StereotypeDefinition configurationDefinition() {
    return new ConfigurationStereotype();
  }

  public static StereotypeModel configuration() {
    return newStereotype(configurationDefinition().getName(), STEREOTYPE_NAMESPACE).build();
  }

}
