/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.function;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.FunctionParameter;

/**
 * Factory definition for creating {@link FunctionParameter.DefaultValueResolver}s
 *
 * @since 1.10.0
 */
@FunctionalInterface
@NoImplement
public interface FunctionParameterDefaultValueResolverFactory {

  FunctionParameter.DefaultValueResolver create(Object defaultValue, DataType type);
}
