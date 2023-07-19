/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.functional.Either;

/**
 * Factory object for instances of either {@link org.mule.sdk.api.runtime.source.Source} or {@link Source}.
 * <p>
 * Implementations are to be reusable and thread-safe
 *
 * @since 1.4.0
 */
@NoImplement
public interface SdkSourceFactory {

  /**
   * Creates a new instance of either {@link org.mule.sdk.api.runtime.source.Source} or {@link Source}.
   *
   * @return a new {@link org.mule.sdk.api.runtime.source.Source} or {@link Source}.
   */
  Either<org.mule.sdk.api.runtime.source.Source, Source> createMessageSource();
}
