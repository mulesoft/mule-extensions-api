/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
 * @since 1.5.0
 */
@Experimental
@MinMuleVersion("4.5.0")
public interface SourceCallbackParameterizer extends Parameterizer<SourceCallbackParameterizer> {

}
