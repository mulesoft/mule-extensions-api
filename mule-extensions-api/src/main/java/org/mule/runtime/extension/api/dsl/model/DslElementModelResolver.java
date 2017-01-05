/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.dsl.model;

import org.mule.runtime.api.dsl.model.ApplicationElement;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.extension.internal.dsl.model.DefaultElementModelResolver;

import java.util.Optional;
import java.util.Set;

/**
 * //TODO
 */
public interface DslElementModelResolver {

  static DslElementModelResolver getDefault(Set<ExtensionModel> extensions) {
    return new DefaultElementModelResolver(extensions);
  }

  <T> Optional<DslElementModel<T>> resolve(ApplicationElement applicationElement);

}
