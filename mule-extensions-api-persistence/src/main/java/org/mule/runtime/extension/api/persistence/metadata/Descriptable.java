/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.api.persistence.metadata;


import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.List;
import java.util.Optional;

/**
 * Contract for all data transfer objects that represent any metadata descriptor.
 *
 * @since 1.0
 */
interface Descriptable<T> {

  MetadataResult<T> toDescriptorResult(List<Failure> failures);

  default Optional<Failure> getComponentFailure(List<Failure> failures, String component) {
    return failures.stream().filter(f -> f.getFailureComponent().equals(component)).findFirst();
  }
}
