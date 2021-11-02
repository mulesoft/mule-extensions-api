/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.stereotype;

import static org.mule.runtime.api.meta.model.stereotype.StereotypeModelBuilder.newStereotype;
import static org.mule.runtime.extension.api.ExtensionConstants.OBJECT_STORE_ELEMENT_NAMESPACE;
import static org.mule.runtime.internal.dsl.DslConstants.CORE_PREFIX;

import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;

/**
 * Provides constants for all known implementations of {@link MuleStereotypes}.
 *
 * @since 1.0
 * @deprecated since 4.5.0, use {@link org.mule.sdk.api.stereotype.MuleStereotypes} instead
 */
@Deprecated
public final class MuleStereotypes {

  private static final String STEREOTYPE_NAMESPACE = CORE_PREFIX.toUpperCase();

  public static final StereotypeDefinition CONFIG_DEFINITION = new ModuleConfigStereotype();
  public static final StereotypeDefinition APP_CONFIG_DEFINITION = new AppConfigStereotype();
  public static final StereotypeDefinition CONNECTION_DEFINITION = new ConnectionStereotype();
  public static final StereotypeDefinition PROCESSOR_DEFINITION = new ProcessorStereotype();
  public static final StereotypeDefinition SOURCE_DEFINITION = new SourceStereotype();
  public static final StereotypeDefinition VALIDATOR_DEFINITION = new ValidatorStereotype();
  public static final StereotypeDefinition OBJECT_STORE_DEFINITION = new ObjectStoreStereotype();
  public static final StereotypeDefinition FLOW_DEFINITION = new FlowStereotype();
  public static final StereotypeDefinition SUB_FLOW_DEFINITION = new SubFlowStereotype();
  public static final StereotypeDefinition ERROR_HANDLER_DEFINITION = new ErrorHandlerStereotype();
  public static final StereotypeDefinition ON_ERROR_DEFINITION = new OnErrorStereotype();
  public static final StereotypeDefinition SERIALIZER_DEFINITION = new SerializerStereotype();
  public static final StereotypeDefinition CHAIN_DEFINITION = new ChainStereotype();

  public static final StereotypeModel CONFIG = newStereotype(CONFIG_DEFINITION.getName(), STEREOTYPE_NAMESPACE).build();
  public static final StereotypeModel APP_CONFIG = newStereotype(APP_CONFIG_DEFINITION.getName(), STEREOTYPE_NAMESPACE).build();
  public static final StereotypeModel CONNECTION = newStereotype(CONNECTION_DEFINITION.getName(), STEREOTYPE_NAMESPACE).build();
  public static final StereotypeModel PROCESSOR = newStereotype(PROCESSOR_DEFINITION.getName(), STEREOTYPE_NAMESPACE).build();
  public static final StereotypeModel SOURCE = newStereotype(SOURCE_DEFINITION.getName(), STEREOTYPE_NAMESPACE).build();
  public static final StereotypeModel VALIDATOR = newStereotype(VALIDATOR_DEFINITION.getName(), STEREOTYPE_NAMESPACE)
      .withParent(PROCESSOR).build();
  public static final StereotypeModel OBJECT_STORE =
      newStereotype(OBJECT_STORE_DEFINITION.getName(), OBJECT_STORE_ELEMENT_NAMESPACE).build();
  public static final StereotypeModel FLOW = newStereotype(FLOW_DEFINITION.getName(), STEREOTYPE_NAMESPACE).build();
  public static final StereotypeModel SUB_FLOW = newStereotype(SUB_FLOW_DEFINITION.getName(), STEREOTYPE_NAMESPACE).build();
  public static final StereotypeModel ERROR_HANDLER =
      newStereotype(ERROR_HANDLER_DEFINITION.getName(), STEREOTYPE_NAMESPACE).build();
  public static final StereotypeModel ON_ERROR =
      newStereotype(ON_ERROR_DEFINITION.getName(), STEREOTYPE_NAMESPACE).build();
  public static final StereotypeModel SERIALIZER =
      newStereotype(SERIALIZER_DEFINITION.getName(), STEREOTYPE_NAMESPACE).build();
  public static final StereotypeModel CHAIN = newStereotype(CHAIN_DEFINITION.getName(), STEREOTYPE_NAMESPACE).build();

  private MuleStereotypes() {}

}
