/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api;

import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.RuntimeExtensionModel;
import org.mule.runtime.extension.api.introspection.config.ConfigurationModel;
import org.mule.runtime.extension.api.manifest.ExtensionManifest;
import org.mule.runtime.extension.api.runtime.ConfigurationInstance;
import org.mule.runtime.extension.api.runtime.ConfigurationProvider;
import org.mule.runtime.extension.api.runtime.ConfigurationStats;

import java.util.Optional;
import java.util.Set;

/**
 * Manages the {@link ExtensionModel extension models} available in the current context and their state.
 * <p/>
 * This class is also the access point to obtaining {@link ConfigurationInstance configuration instances}
 * of the extensions in use.
 *
 * For an extension to be usable, it has to be registered in this manager through the
 * {@link #registerExtension(ExtensionManifest, ClassLoader)} method
 *
 * @since 1.0
 */
public interface ExtensionManager {

  void registerExtension(ExtensionManifest manifest, ClassLoader classLoader);

  /**
   * Returns an immutable {@link Set} listing all the discovered
   * {@link ExtensionModel extensionModels}.
   *
   * @return an immutable {@link Set}. Will not be {@code null} but might be empty
   */
  Set<RuntimeExtensionModel> getExtensions();

  /**
   * Returns an {@link Optional} {@link RuntimeExtensionModel} which
   * name equals {@code extensionName}.
   *
   * @param extensionName the name of the extensions you want.
   * @return an {@link Optional}. It will be empty if no such extension is registered
   */
  Optional<RuntimeExtensionModel> getExtension(String extensionName);

  /**
   * Returns a {@link ConfigurationInstance} obtained through a previously registered
   * {@link ConfigurationProvider} named as {@code configurationProvider}
   * <p/>
   * After the {@link ConfigurationProvider} has been located, an instance is returned by
   * invoking its {@link ConfigurationProvider#get(Object)} with the {@code muleEvent}
   * as the argument.
   * <p/>
   * By the mere fact of this configuration being returned, the value of
   * {@link ConfigurationStats#getLastUsedMillis()} will be updated for the returned
   * {@link ConfigurationInstance}
   *
   * @param configurationProviderName the name of a previously registered {@link ConfigurationProvider}
   * @param muleEvent                 the current MuleEvent
   * @param <C>                       the generic type of the configuration instance to be returned
   * @return a {@link ConfigurationInstance}
   */
  //TODO: MULE-8946
  <C> ConfigurationInstance<C> getConfiguration(String configurationProviderName, Object muleEvent);


  /**
   * Returns a {@link ConfigurationInstance} for the given {@code extensionModel}.
   * <p/>
   * Because no {@link ConfigurationProvider} is specified, the following algorithm will
   * be applied to try and determine the instance to be returned:
   * <ul>
   * <li>If <b>one</b> {@link ConfigurationProvider} is registered, capable of handing configurations
   * of the given {@code extensionModel}, then that provider is used</li>
   * <li>If more than one {@link ConfigurationProvider} meeting the criteria above is found, then
   * a {@link IllegalStateException} is thrown</li>
   * <li>If no such {@link ConfigurationProvider} is registered, then an attempt will be made
   * to locate a {@link ConfigurationModel} for which an implicit configuration can be inferred. A model
   * can be considered implicit if all its parameters are either optional or provide a default value. If such
   * a model is found, then a {@link ConfigurationProvider} is created and registered for that model.
   * </li>
   * <li>If none of the above conditions is met, then an {@link IllegalStateException} is thrown</li>
   * </ul>
   * <p/>
   * By the mere fact of this configuration being returned, the value of
   * {@link ConfigurationStats#getLastUsedMillis()} will be updated for the returned
   * {@link ConfigurationInstance}
   *
   * @param extensionModel the {@link ExtensionModel} for which a configuration is wanted
   * @param muleEvent      the current MuleEvent
   * @param <C>            the generic type of the configuration instance to be returned
   * @return a {@link ConfigurationInstance}
   * @throws IllegalStateException if none or too many {@link ConfigurationProvider} are found to be suitable
   */
  //TODO: MULE-8946
  <C> ConfigurationInstance<C> getConfiguration(ExtensionModel extensionModel, Object muleEvent);
}
