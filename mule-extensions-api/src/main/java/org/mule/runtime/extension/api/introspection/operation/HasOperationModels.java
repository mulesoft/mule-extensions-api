/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.operation;

import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.source.SourceModel;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;

import java.util.List;
import java.util.Optional;

/**
 * A model which contains {@link OperationModel}s
 *
 * @since 1.0
 */
public interface HasOperationModels
{
    /**
     * Returns a {@link List} of {@link OperationModel}s defined at the level
     * of the component implementing this interface.
     *
     * Each operation is guaranteed to have a unique name which will not
     * overlap with any {@link SourceModel} or {@link ConnectionProviderModel}
     * defined in the same {@link ExtensionModel}
     *
     * @return an immutable {@link List} of {@link OperationModel}
     */
    List<OperationModel> getOperationModels();

    /**
     * Returns the {@link OperationModel} that matches
     * the given name.
     *
     * @param name case sensitive operation name
     * @return an {@link Optional} {@link OperationModel}
     */
    Optional<OperationModel> getOperationModel(String name);
}
