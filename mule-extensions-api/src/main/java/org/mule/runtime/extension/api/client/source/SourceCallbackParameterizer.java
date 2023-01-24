/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.client.source;

import org.mule.api.annotation.Experimental;
import org.mule.runtime.extension.api.client.params.Parameterizer;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Parameterizes a success/error callback from a message source
 * <p>
 * <b>NOTE:</b> Experimental feature. Backwards compatibility not guaranteed.
 *
 * @since 1.6.0
 */
@Experimental
@MinMuleVersion("4.6.0")
public interface SourceCallbackParameterizer extends Parameterizer<SourceCallbackParameterizer> {

}
