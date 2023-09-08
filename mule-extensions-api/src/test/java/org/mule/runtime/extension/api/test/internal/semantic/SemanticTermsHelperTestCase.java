/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.internal.semantic;

import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.API_KEY_AUTH_CONNECTION;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.DOMAIN;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.ENDPOINT;
import static org.mule.runtime.extension.internal.semantic.ConnectivityVocabulary.OAUTH_AUTHORIZATION_CODE_CONNECTION;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import org.mule.runtime.extension.internal.semantic.SemanticTermsHelper;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

public class SemanticTermsHelperTestCase {

  @Test
  public void getConnectionTermsFromNullSet() {
    assertThat(SemanticTermsHelper.getConnectionTerms(null), hasSize(0));
  }

  @Test
  public void getConnectionTerms() {
    Set<String> inputTerms = new LinkedHashSet<>();
    inputTerms.add(API_KEY_AUTH_CONNECTION);
    inputTerms.add(OAUTH_AUTHORIZATION_CODE_CONNECTION);
    inputTerms.add(ENDPOINT);

    Set<String> resolvedTerms = SemanticTermsHelper.getConnectionTerms(inputTerms);
    assertThat(resolvedTerms, hasSize(2));
    assertThat(resolvedTerms, contains(API_KEY_AUTH_CONNECTION, OAUTH_AUTHORIZATION_CODE_CONNECTION));
  }

  @Test
  public void getParameterTermsFromNullSet() {
    assertThat(SemanticTermsHelper.getParameterTerms(null), hasSize(0));
  }

  @Test
  public void getParameterTerms() {
    Set<String> inputTerms = new LinkedHashSet<>();
    inputTerms.add(DOMAIN);
    inputTerms.add(ENDPOINT);
    inputTerms.add("peperoni");

    Set<String> resolvedTerms = SemanticTermsHelper.getParameterTerms(inputTerms);
    assertThat(resolvedTerms, hasSize(2));
    assertThat(resolvedTerms, contains(DOMAIN, ENDPOINT));
  }
}
