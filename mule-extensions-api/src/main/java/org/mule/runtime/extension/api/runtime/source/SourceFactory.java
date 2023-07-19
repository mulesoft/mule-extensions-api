/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.api.annotation.NoImplement;

/**
 * Factory object for instances of {@link Source}.
 * <p>
 * Implementations are to be reusable and thread-safe
 *
 * @since 1.0
 * @deprecated since 1.4.0. Use {@link SdkSourceFactory} instead
 */
@NoImplement
@Deprecated
public interface SourceFactory {

  /**
   * Creates a new instance of {@link Source}
   *
   * @return a new {@link Source}
   */
  Source createSource();
}
