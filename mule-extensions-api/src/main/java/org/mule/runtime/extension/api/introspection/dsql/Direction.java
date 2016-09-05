/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.dsql;

/**
 * The direction to sort the items returned from a query.
 *
 * @since 1.0
 */
public enum Direction {

  /**
   * Ascending sorting, from the smallest to the largest item.
   */
  ASC,

  /**
   * Descending sorting, from the largest to the smallest item.
   */
  DESC
}
