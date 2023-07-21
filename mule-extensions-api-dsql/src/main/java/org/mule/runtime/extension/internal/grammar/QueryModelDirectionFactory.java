/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.grammar;


import org.mule.runtime.extension.api.dsql.Direction;

import java.util.HashMap;

/**
 * Singleton class that returns a {@link Direction} expression depending on the token
 * that is passed as parameter in the {@link QueryModelDirectionFactory#getDirection(String)}
 *
 * @since 1.0
 */
final class QueryModelDirectionFactory {

  private static QueryModelDirectionFactory INSTANCE = new QueryModelDirectionFactory();

  private HashMap<String, Direction> directions = new HashMap<>();

  private QueryModelDirectionFactory() {
    directions.put("asc", Direction.ASC);
    directions.put("ascending", Direction.ASC);
    directions.put("desc", Direction.DESC);
    directions.put("descending", Direction.DESC);
  }

  static QueryModelDirectionFactory getInstance() {
    return INSTANCE;
  }

  Direction getDirection(String direction) {
    return directions.get(direction);
  }
}
