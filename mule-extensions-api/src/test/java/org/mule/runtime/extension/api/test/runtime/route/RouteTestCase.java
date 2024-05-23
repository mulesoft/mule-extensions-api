/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.runtime.route;

import static org.mockito.Mockito.mock;

import org.mule.runtime.extension.api.runtime.route.Chain;
import org.mule.runtime.extension.api.runtime.route.Route;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import io.qameta.allure.Issue;

public class RouteTestCase {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Issue("W-15836498")
  public void processorsSetOnlyOnce() {
    final Route route = new Route() {

      @Override
      public String toString() {
        return "testRoute";
      }

    };

    route.setProcessors(mock(Chain.class));

    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("'processors' had been already set in route 'testRoute'");

    route.setProcessors(mock(Chain.class));
  }
}
