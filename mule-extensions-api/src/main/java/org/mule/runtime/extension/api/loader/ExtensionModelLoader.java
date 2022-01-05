/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;


import static java.lang.Thread.currentThread;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.deployment.meta.MulePluginModel;
import org.mule.runtime.api.dsl.DslResolvingContext;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.extension.internal.loader.DefaultExtensionLoadingContext;
import org.mule.runtime.extension.internal.loader.ExtensionModelFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Loader of an {@link ExtensionModel} for a Mule plugin artifact from a set of attributes read by the {@link MulePluginModel}.
 *
 * @since 1.0
 */
@NoImplement
public abstract class ExtensionModelLoader {

  private final ExtensionModelFactory factory = new ExtensionModelFactory();

  /**
   * @return an identifier of this {@link ExtensionModelLoader}. Non null neither blank.
   *         <p/>
   *         The ID must be unique among all {@link ExtensionModelLoader }.
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
   * {@link ExtensionDeclaration}. That declaration is then transformed into an actual {@link ExtensionModel}. While loading the
   * extension, a default set of {@link DeclarationEnricher} and {@link ExtensionModelValidator} will be applied. The
   * {@link ExtensionLoadingContext} received in {@link #declareExtension(ExtensionLoadingContext)} might additionally contain
   * extra ones. The {@link #configureContextBeforeDeclaration(ExtensionLoadingContext)} allows to add custom configurations into
   * the context before the declaration begins.
   *
   * @param pluginClassLoader   context {@link ClassLoader} that holds all the needed classes and resources to properly generate
   *                            an {@link ExtensionModel}.
   * @param dslResolvingContext context with all the {@link ExtensionModel}s already loaded that are mandatory to execute the
   *                            method properly.
   * @param attributes          a set of attributes to work with in each concrete implementation of {@link ExtensionModelLoader},
   *                            which will be responsible of extracting the mandatory parameters (while casting, if needed).
   * @return an {@link ExtensionModel} that represents the plugin being described
   * @throws IllegalArgumentException if there are missing entries in {@code attributes} or the type of any of them does not apply
   *                                  to the expected one.
   */
  public final ExtensionModel loadExtensionModel(ClassLoader pluginClassLoader, DslResolvingContext dslResolvingContext,
                                                 Map<String, Object> attributes) {
    ExtensionLoadingContext ctx = new DefaultExtensionLoadingContext(pluginClassLoader, dslResolvingContext);
    ctx.addParameters(attributes);
    configureContextBeforeDeclaration(ctx);

    ClassLoader currentClassLoader = currentThread().getContextClassLoader();
    currentThread().setContextClassLoader(pluginClassLoader);
    try {
      declareExtension(ctx);
      return factory.create(ctx);
    } finally {
      currentThread().setContextClassLoader(currentClassLoader);
    }
  }

  /**
   * Allows adding pre-configured the given {@code context} before it's fed into
   * {@link #declareExtension(ExtensionLoadingContext)}. This is the ideal place to register custom parameters, enrichers,
   * validators, etc.
   *
   * @param context the context that will be used for the declaration
   */
  protected void configureContextBeforeDeclaration(ExtensionLoadingContext context) {
    // empty by default
  }

  /**
   * This method uses the {@link ExtensionDeclarer} found through {@link ExtensionLoadingContext#getExtensionDeclarer()} to define
   * the {@link ExtensionModel} to be loaded.
   * <p>
   * <b>IMPORTANT</b>: When this method is executed, the runtime automatically sets the current thread's context classloader to
   * {@link ExtensionLoadingContext#getExtensionClassLoader()}. The previous TCCL is restored after this method's execution.
   * <p>
   * This method should only be invoked by the runtime. Do not invoke manually.
   *
   * @param context the context that will be used for the declaration
   */
  protected abstract void declareExtension(ExtensionLoadingContext context);

  public static final class LoadRequest {


  }

  public static final class LoadRequestBuilder {

    private LoadRequest product = new LoadRequest();



    /**
     * Adds a custom parameter registered under {@code key}
     *
     * @param key   the key under which the {@code value} is to be registered
     * @param value the custom parameter value
     * @throws IllegalArgumentException if {@code key} or {@code value} are {@code null}
     */
    void addParameter(String key, Object value);

    /**
     * Adds the contents of the given map as custom parameters
     *
     * @param parameters a map with custom parameters
     */
    void addParameters(Map<String, Object> parameters);

    /**
     * Obtains the custom parameter registered under {@code key}.
     *
     * @param key the key under which the wanted value is registered
     * @param <T> generic type of the expected value
     * @return an {@link Optional} value
     */
    <T> Optional<T> getParameter(String key);

    /**
     * Registers a custom {@link ExtensionModelValidator} to be executed on top of the ones which the runtime applies by default.
     * <p>
     * Custom validators will not apply globally but just for the model being loaded with this context.
     *
     * @param extensionModelValidator the custom validator
     * @return {@code this} instance
     */
    ExtensionLoadingContext addCustomValidator(ExtensionModelValidator extensionModelValidator);

    /**
     * Registers custom {@link ExtensionModelValidator} to be executed on top of the ones which the runtime applies by default.
     * <p>
     * These custom validators will not apply globaly but just for the model being loaded with this context.
     *
     * @param extensionModelValidators the custom validators
     * @return {@code this} instance
     */
    ExtensionLoadingContext addCustomValidators(Collection<ExtensionModelValidator> extensionModelValidators);

    /**
     * Registers a custom {@link DeclarationEnricher} which is executed <b>before</b> the ones that the runtime automatically
     * applies.
     *
     * @param enricher the custom enricher
     * @return {@code this} instance
     */
    ExtensionLoadingContext addCustomDeclarationEnricher(DeclarationEnricher enricher);

    /**
     * Registers custom {@link DeclarationEnricher} which are executed <b>before</b> the ones that the runtime automatically
     * applies.
     *
     * @param enrichers the custom enrichers
     * @return {@code this} instance
     */
    ExtensionLoadingContext addCustomDeclarationEnrichers(Collection<DeclarationEnricher> enrichers);

  }
}
