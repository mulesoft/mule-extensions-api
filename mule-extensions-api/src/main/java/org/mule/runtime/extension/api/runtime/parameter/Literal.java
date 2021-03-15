/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.parameter;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;

import java.util.Optional;

/**
 * A parameter type which allows to get values as they were provided in the DSL, preventing the runtime from resolving expressions
 * or applying any transformations. This is useful when such evaluation should be delegated into a third party.
 * <p>
 * In the particular case of configurations and connection providers, using literal parameters allows that configuration to be
 * treated as a static one instead of dynamic. This is so because the literal value is constant, even if that literal is actually
 * an expression. Because the runtime won't be evaluating it automatically, it's an static value, from the extension's point of
 * view.
 * <p>
 * If the value was provided as an expression, this class will provide such expression but will offer no way to actually evaluate
 * it. Use {@link ParameterResolver} for that.
 *
 * Finally, a super important consideration to have is the generic type. The {@link ParameterModel} derived from uses of this
 * interface will have its type set according to the generic type. The generic is not optional
 * <p>
 * 
 * @param <T> the generic type of the actual parameter type
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.runtime.parameter.Literal} instead.
 */
@NoImplement
@Deprecated
public interface Literal<T> extends org.mule.sdk.api.runtime.parameter.Literal<T> {
}
