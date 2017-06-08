/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.api.annotation.privileged;

import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.ExtensionModelLoader;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * DO NOT USE THIS ANNOTATION. THIS ANNOTATION SHOULD <strong>ONLY</strong> BE USED BY PRIVILEGED EXTENSIONS.
 * <p>
 * This annotation allows privileged {@link Extension}s to contribute with {@link DeclarationEnricher}s for enriching
 * the {@link ExtensionModel} AFTER the SDK run all the internal {@link DeclarationEnricher}s declared in the
 * {@link ExtensionModelLoader}.
 * <p>
 * This annotation is part of the Privileged API and only selected {@link Extension}s should be able to use it,
 * if a NON PRIVILEGED {@link Extension} uses this annotation there will have no effect on the final extension model used for
 * runtime but generated resources may end up corrupted.
 *
 * @since 1.0
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface DeclarationEnrichers {

  /**
   * @return an array of {@link DeclarationEnricher}s implementations that will be executed AFTER the SDK finished the execution
   * of the internal ones.
   */
  Class<? extends DeclarationEnricher>[] value();
}
