/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.streaming;

import java.io.Closeable;
import java.util.List;
import java.util.Optional;

/**
 * This interface provides functionality for consuming a data feed in pages.
 * <p>
 * Instances should not be reused. Each execution of a paging operation should return
 * different instances.
 * <p>
 * Implementing this interface does not guarantee thread safeness.
 *
 * @param <C> connection type expected to handle the operations.
 * @param <T> the type of the returned pages.
 * @since 1.0
 */
public interface PagingProvider<C, T> extends Closeable {

  /**
   * Returns the next page of items. If the return value is an empty {@link List} then it means no more items are available
   *
   * @param connection The connection to be used to do the query.
   * @return a populated {@link List} of elements of type {@code Type}, An empty {@link List} if no more items are available
   */
  List<T> getPage(C connection);

  /**
   * returns the total amount of items in the non-paged result set. In some scenarios,
   * it might not be possible/convenient to actually retrieve this value, in such a cases an
   * {@link Optional#empty()} value is returned.
   *
   * @param connection The connection to be used to do the query.
   */
  Optional<Integer> getTotalResults(C connection);

  /**
   * Some systems require the same connection that obtained the first page to be used to fetch
   * the subsequent ones. Although this is not the case of most APIs, this method allows to instruct
   * the runtime to always feed the same connection into the {@link #getPage(Object)} method.
   * <p>
   * Keep in mind that if the operation is participating in a transaction, then the connection
   * <b>will</b> become sticky no matter what this method says.
   * <p>
   * This method is optional and defaults to {@code false}
   *
   * @return Whether all pages should be fetch using the same connection
   */
  default boolean useStickyConnections() {
    return false;
  }

}
