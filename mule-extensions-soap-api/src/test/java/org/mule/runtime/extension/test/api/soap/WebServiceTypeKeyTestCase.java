/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.test.api.soap;

import org.mule.runtime.extension.api.soap.WebServiceTypeKey;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.qameta.allure.Issue;

@Issue("W-15836498")
public class WebServiceTypeKeyTestCase {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void serviceSetOnlyOnce() {
    final WebServiceTypeKey wstk = new WebServiceTypeKey();

    wstk.setService("someService");
    wstk.setOperation("someOperation");

    expectedException.expect(IllegalStateException.class);
    expectedException
        .expectMessage("'service' had been already set in WebServiceTypeKey 'WebServiceTypeKey{service=someService;operation=someOperation}'");

    wstk.setService("otherService");
  }

  @Test
  public void operationSetOnlyOnce() {
    final WebServiceTypeKey wstk = new WebServiceTypeKey();

    wstk.setService("someService");
    wstk.setOperation("someOperation");

    expectedException.expect(IllegalStateException.class);
    expectedException
        .expectMessage("'operation' had been already set in WebServiceTypeKey 'WebServiceTypeKey{service=someService;operation=someOperation}'");

    wstk.setOperation("otherOperation");
  }

}
