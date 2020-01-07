/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.route;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.component.location.ComponentLocation;

import java.util.Optional;

@NoImplement
public interface ChainContext {

  Optional<ChainState> getState(String extensionNamespace, String componentName);

}
