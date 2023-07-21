/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
/**
 * Test module for mule-extensions-api
 *
 * @since 1.5
 */
module org.mule.runtime.extensions.api.test {

  requires org.mule.runtime.extensions.api;
  requires org.mule.runtime.metadata.model.api;
  requires org.mule.runtime.api.test;
  requires org.mule.runtime.metadata.model.java;
  requires org.mule.sdk.api;

  requires com.github.benmanes.caffeine;
  requires com.google.common;
  requires com.google.gson;
  requires org.apache.commons.io;
  requires org.apache.commons.lang3;
  requires semver4j;
  requires transitive junit;
  requires org.hamcrest;
  requires io.qameta.allure.commons;
  requires org.mockito;
  requires net.bytebuddy.agent;
  requires org.json;
  requires jsonassert;

  exports org.mule.runtime.extension.api.test;
  exports org.mule.runtime.extension.api.test.declaration;
  exports org.mule.runtime.extension.api.test.declaration.fluent;
  exports org.mule.runtime.extension.api.test.declaration.fluent.util;
  exports org.mule.runtime.extension.api.test.declaration.type;
  exports org.mule.runtime.extension.api.test.declaration.type.annotation;
  exports org.mule.runtime.extension.api.test.dsl;
  exports org.mule.runtime.extension.api.test.dsl.model;
  exports org.mule.runtime.extension.api.test.internal;
  exports org.mule.runtime.extension.api.test.internal.loader;
  exports org.mule.runtime.extension.api.test.internal.loader.declaration.type.annotation;
  exports org.mule.runtime.extension.api.test.internal.loader.enricher;
  exports org.mule.runtime.extension.api.test.internal.loader.util;
  exports org.mule.runtime.extension.api.test.internal.loader.validator;
  exports org.mule.runtime.extension.api.test.internal.semantic;
  exports org.mule.runtime.extension.api.test.runtime;
  exports org.mule.runtime.extension.api.test.runtime.operation;
  exports org.mule.runtime.extension.api.test.runtime.parameters;
  exports org.mule.runtime.extension.api.test.stereotype;
  exports org.mule.runtime.extension.api.test.util;

}
