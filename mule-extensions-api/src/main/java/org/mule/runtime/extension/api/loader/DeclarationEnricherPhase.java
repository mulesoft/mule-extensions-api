/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader;

import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;

import java.util.Collection;


/**
 *
 * @since 1.0
 */
public enum DeclarationEnricherPhase {
  INITIALISE, PRE_STRUCTURE_DECLARATION, STRUCTURE_DECLARATION, POST_STRUCTURE_DECLARATION, DISPLAY_ELEMENTS_DECLARATION, FINALIZE
}
