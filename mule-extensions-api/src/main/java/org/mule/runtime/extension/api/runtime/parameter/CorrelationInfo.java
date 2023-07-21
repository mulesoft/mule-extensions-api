/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.parameter;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.message.ItemSequenceInfo;
import org.mule.runtime.extension.api.annotation.execution.OnError;
import org.mule.runtime.extension.api.annotation.execution.OnSuccess;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.Optional;

/**
 * Provides message correlation information. Operations and source callbacks (such as methods annotated with {@link OnSuccess}
 * or @{@link OnError}) can obtain an instance in order to obtain information regarding how the message being processed correlates
 * to its context.
 *
 * @since 1.1
 */
@MinMuleVersion("4.1")
@NoImplement
public interface CorrelationInfo extends org.mule.sdk.api.runtime.parameter.CorrelationInfo {
}
