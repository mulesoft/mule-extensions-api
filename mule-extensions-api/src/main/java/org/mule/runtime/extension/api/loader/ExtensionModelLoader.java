/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;


import static java.lang.Thread.currentThread;
import org.mule.runtime.api.deployment.meta.MulePluginModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.extension.internal.loader.DefaultExtensionLoadingContext;
import org.mule.runtime.extension.internal.loader.ExtensionModelFactory;

import java.util.Map;

/**
 * Loader of an {@link ExtensionModel} for a Mule plugin artifact from a set of attributes read by the {@link MulePluginModel}.
 *
 * @since 1.0
 */
public abstract class ExtensionModelLoader {

  private final ExtensionModelFactory factory = new ExtensionModelFactory();

  /**
   * @return an identifier of this {@link ExtensionModelLoader}. Non null neither blank.
   * <p/>
   * The ID must be unique among all {@link ExtensionModelLoader }.
   */
  public abstract String getId();

  /**
   * Creates an {@link ExtensionModel} from the {@code pluginClassLoader} using any attribute it needs from the {@code attributes}
   * parameter.
   * <p/>
   * Bear in mind that, is up to each implementation of this method to validate the {@code attributes} parameter has the needed
   * elements and that each of them is also of the expected Java class. This is by design, as the invokers of this method knows
   * how to parse descriptor files (see {@link MulePluginModel}) for a plugin in a generic way.
   * <p>
   * This method delegates into {@link #declareExtension(ExtensionLoadingContext)} in order to obtain the
   * {@link ExtensionDeclaration}. That declaration is then transformed into an actual {@link ExtensionModel}.
   * While loading the extension, a default set of {@link DeclarationEnricher} and {@link ExtensionModelValidator} will be applied.
   * The {@link ExtensionLoadingContext} received in {@link #declareExtension(ExtensionLoadingContext)} might additionally
   * contain extra ones. The {@link #configureContextBeforeDeclaration(ExtensionLoadingContext)} allows to add custom
   * configurations into the context before the declaration begins.
   *
   * @param pluginClassLoader context {@link ClassLoader} that holds all the needed classes and resources to properly generate
   *                          an {@link ExtensionModel}.
   * @param attributes        a set of attributes to work with in each concrete implementation of {@link ExtensionModelLoader}, which will
   *                          be responsible of extracting the mandatory parameters (while casting, if needed).
   * @return an {@link ExtensionModel} that represents the plugin being described
   * @throws IllegalArgumentException if there are missing entries in {@code attributes} or the type of any of them does not apply
   *                                  to the expected one.
   */
  public final ExtensionModel loadExtensionModel(ClassLoader pluginClassLoader, Map<String, Object> attributes) {
    ExtensionLoadingContext ctx = new DefaultExtensionLoadingContext(pluginClassLoader);
    ctx.addParameters(attributes);
    configureContextBeforeDeclaration(ctx);

    ClassLoader currentClassLoader = currentThread().getContextClassLoader();
    currentThread().setContextClassLoader(pluginClassLoader);
    try {
      declareExtension(ctx);
    } finally {
      currentThread().setContextClassLoader(currentClassLoader);
    }

    return factory.create(ctx);
  }

  /**
   * Allows to add pre configured the given {@code context} before it's fed into {@link #declareExtension(ExtensionLoadingContext)}.
   * This is the ideal place to register custom parameters, enrichers, validators, etc.
   *
   * @param context the context that will be used for the declaration
   */
  protected void configureContextBeforeDeclaration(ExtensionLoadingContext context) {
    // empty by default
  }

  /**
   * This method uses the {@link ExtensionDeclarer} found through {@link ExtensionLoadingContext#getExtensionDeclarer()}
   * to define the {@link ExtensionModel} to be loaded.
   * <p>
   * <b>IMPORTANT</b>: When this method is executed, the runtime automatically sets the current thread's context classloader
   * to {@link ExtensionLoadingContext#getExtensionClassLoader()}. The previous TCCL is restored after this method's execution.
   * <p>
   * This method should only be invoked by the runtime. Do not invoke manually.
   *
   * @param context the context taht will be used for the declaration
   */
  protected abstract void declareExtension(ExtensionLoadingContext context);
}
