/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.runtime.extension.api.runtime.operation.Result;

import java.io.Serializable;
import java.util.Comparator;

public interface WatermarkHandler<T, A, W extends Serializable> {

  W getWatermark(Result<T, A> result);

  Comparator<W> getComparator();

  W getInitialWatermark();
}
