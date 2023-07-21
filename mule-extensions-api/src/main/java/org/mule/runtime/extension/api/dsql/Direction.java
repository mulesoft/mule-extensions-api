/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.dsql;

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
