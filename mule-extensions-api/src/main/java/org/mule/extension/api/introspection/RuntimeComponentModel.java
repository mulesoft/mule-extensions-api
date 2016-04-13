/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection;

import org.mule.extension.api.introspection.exception.ExceptionEnrichableModel;
import org.mule.extension.api.introspection.metadata.MetadataEnrichableModel;
import org.mule.extension.api.introspection.operation.RuntimeOperationModel;
import org.mule.extension.api.introspection.source.RuntimeSourceModel;

/**
 * A specialization of the {@link ComponentModel} that represents an extension made component in their runtime
 * specialization, like {@link RuntimeOperationModel} and {@link RuntimeSourceModel}, which adds
 * behavioural components that are relevant to the extension's functioning when in runtime.
 *
 * @see ComponentModel
 * @since 1.0
 */
public interface RuntimeComponentModel extends ComponentModel, InterceptableModel, ExceptionEnrichableModel, MetadataEnrichableModel
{

}
