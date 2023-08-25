/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.declaration.type;


import org.mule.metadata.java.api.handler.TypeHandlerManager;
import org.mule.metadata.java.api.handler.TypeHandlerManagerFactory;

/**
 * An implementation of {@link TypeHandlerManagerFactory} which creates instances of {@link TypeHandlerManager} properly
 * configured to be used under the rules of the Extensions API.
 * <p>
 * The extension's runtime should not use {@link TypeHandlerManager} instances which were not created through implementations of
 * this interface
 *
 * @since 1.0
 */
public final class ExtensionsTypeHandlerManagerFactory implements TypeHandlerManagerFactory {

  /**
   * Creates a {@link TypeHandlerManager} which uses a {@link ExtensionsObjectFieldHandler}
   *
   * @return a {@link TypeHandlerManager}
   */
  @Override
  public TypeHandlerManager createTypeHandlerManager() {
    return TypeHandlerManager.create(new CursorProviderTypeHandler(),
                                     new TlsContextClassHandler(),
                                     new ObjectStoreClassHandler(),
                                     new SchedulingStrategyClassHandler(),
                                     new ExtensionObjectTypeHandler(new ExtensionsObjectFieldHandler()));
  }
}
