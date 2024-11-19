/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
import org.mule.api.annotation.jpms.PrivilegedApi;

/**
 * API for Mule Extensions to integrate with the Mule Runtime in a decoupled way.
 * 
 * @moduleGraph
 * @since 1.5
 */
@PrivilegedApi(
    privilegedPackages = {
        "org.mule.runtime.extension.api.annotation.privileged"
    },
    privilegedArtifactIds = {
        "org.mule.modules:mule-aggregators-module",
        "org.mule.modules:mule-scripting-module",
        "org.mule.modules:mule-soapkit-module",
        "org.mule.modules:mule-tracing-module",
        "org.mule.modules:mule-validation-module",

        "org.mule.tests:test-components"
    })
module org.mule.runtime.extensions.api {

  requires org.mule.runtime.metadata.model.api;
  requires org.mule.runtime.metadata.model.java;
  requires org.mule.runtime.metadata.model.message;
  requires org.mule.runtime.api;
  requires org.mule.sdk.api;

  requires java.compiler;
  // Required for using java.beans.Introspector.
  requires java.desktop;

  requires com.google.common;
  requires com.sun.xml.bind;

  requires com.github.benmanes.caffeine;
  requires org.apache.commons.lang3;
  requires org.apache.commons.text;

  // Required for the deprecated org.mule.runtime.extension.api.runtime.operation.ComponentExecutor<T>
  // that has its API defined in terms of org.reactivestreams.Publisher<Object>.
  requires transitive org.reactivestreams;

  exports org.mule.runtime.extension.api;
  exports org.mule.runtime.extension.api.annotation;
  exports org.mule.runtime.extension.api.annotation.connectivity;
  exports org.mule.runtime.extension.api.annotation.connectivity.oauth;
  exports org.mule.runtime.extension.api.annotation.deprecated;
  exports org.mule.runtime.extension.api.annotation.dsl.xml;
  exports org.mule.runtime.extension.api.annotation.execution;
  exports org.mule.runtime.extension.api.annotation.error;
  exports org.mule.runtime.extension.api.annotation.license;
  exports org.mule.runtime.extension.api.annotation.metadata;
  exports org.mule.runtime.extension.api.annotation.metadata.fixed;
  exports org.mule.runtime.extension.api.annotation.notification;
  exports org.mule.runtime.extension.api.annotation.values;
  exports org.mule.runtime.extension.api.annotation.param;
  exports org.mule.runtime.extension.api.annotation.param.display;
  exports org.mule.runtime.extension.api.annotation.param.reference;
  exports org.mule.runtime.extension.api.annotation.param.stereotype;
  exports org.mule.runtime.extension.api.annotation.privileged;
  exports org.mule.runtime.extension.api.annotation.source;
  exports org.mule.runtime.extension.api.connectivity;
  exports org.mule.runtime.extension.api.connectivity.oauth;
  exports org.mule.runtime.extension.api.client;
  exports org.mule.runtime.extension.api.client.params;
  exports org.mule.runtime.extension.api.client.source;
  exports org.mule.runtime.extension.api.data.sample;
  exports org.mule.runtime.extension.api.declaration.fluent.util;
  exports org.mule.runtime.extension.api.error;
  exports org.mule.runtime.extension.api.exception;
  exports org.mule.runtime.extension.api.metadata;
  exports org.mule.runtime.extension.api.model;
  exports org.mule.runtime.extension.api.model.config;
  exports org.mule.runtime.extension.api.model.construct;
  exports org.mule.runtime.extension.api.model.connection;
  exports org.mule.runtime.extension.api.model.deprecated;
  exports org.mule.runtime.extension.api.model.function;
  exports org.mule.runtime.extension.api.model.nested;
  exports org.mule.runtime.extension.api.model.notification;
  exports org.mule.runtime.extension.api.model.operation;
  exports org.mule.runtime.extension.api.model.parameter;
  exports org.mule.runtime.extension.api.model.source;
  exports org.mule.runtime.extension.api.notification;
  exports org.mule.runtime.extension.api.loader;
  exports org.mule.runtime.extension.api.loader.util;
  exports org.mule.runtime.extension.api.resources;
  exports org.mule.runtime.extension.api.resources.spi;
  exports org.mule.runtime.extension.api.ocs;
  exports org.mule.runtime.extension.api.property;
  exports org.mule.runtime.extension.api.provider;
  exports org.mule.runtime.extension.api.runtime;
  exports org.mule.runtime.extension.api.runtime.config;
  exports org.mule.runtime.extension.api.runtime.connectivity;
  exports org.mule.runtime.extension.api.runtime.exception;
  exports org.mule.runtime.extension.api.runtime.operation;
  exports org.mule.runtime.extension.api.runtime.parameter;
  exports org.mule.runtime.extension.api.runtime.process;
  exports org.mule.runtime.extension.api.runtime.route;
  exports org.mule.runtime.extension.api.runtime.source;
  exports org.mule.runtime.extension.api.runtime.streaming;
  exports org.mule.runtime.extension.api.security;
  exports org.mule.runtime.extension.api.stereotype;
  exports org.mule.runtime.extension.api.tx;
  exports org.mule.runtime.extension.api.util;
  exports org.mule.runtime.extension.api.values;
  exports org.mule.runtime.extension.api.declaration.type;
  exports org.mule.runtime.extension.api.declaration.type.annotation;
  exports org.mule.runtime.extension.api.dsl.syntax;
  exports org.mule.runtime.extension.api.dsl.syntax.resolver;
  exports org.mule.runtime.extension.api.dsl.syntax.resources.spi;
  exports org.mule.runtime.extension.api.dsql;

  // Being {@code privileged}, this is not intended to be used outside of the scope of crafted extension declarations.
  exports org.mule.runtime.extension.privileged.loader;
  exports org.mule.runtime.extension.privileged.semantic;
  exports org.mule.runtime.extension.privileged.spi;
  exports org.mule.runtime.extension.privileged.util;

  // W-15399821 Add visibility for legacy code
  exports org.mule.runtime.extension.internal.client to
      org.mule.runtime.extensions.support;

  exports org.mule.runtime.extension.internal.dsl to
      org.mule.runtime.extensions.api.test;
  exports org.mule.runtime.extension.internal.dsl.xml to
      org.mule.runtime.artifact.ast.xmlParser;
  exports org.mule.runtime.extension.internal.loader to
      org.mule.runtime.extensions.api.test;
  exports org.mule.runtime.extension.internal.loader.util to
      org.mule.runtime.ast.extension,
      org.mule.runtime.extensions.api.test;

  exports org.mule.runtime.extension.internal.loader.enricher to
      org.mule.runtime.extensions.api.test;

  exports org.mule.runtime.extension.internal.loader.validator to
      org.mule.runtime.extensions.api.test;

  exports org.mule.runtime.extension.internal.property to
      org.mule.runtime.extensions.api.test;

  exports org.mule.runtime.extension.internal.semantic to
      org.mule.runtime.extensions.api.test;

  // Introspection by kryo used by mule serializer
  opens org.mule.runtime.extension.api.exception to
      kryo.shaded;

  // Allow introspection for serialization/deserialization by Gson
  opens org.mule.runtime.extension.api.connectivity.oauth to
      com.google.gson;
  opens org.mule.runtime.extension.api.declaration.type.annotation to
      com.google.gson;
  opens org.mule.runtime.extension.api.error to
      com.google.gson;
  opens org.mule.runtime.extension.api.model to
      com.google.gson;
  opens org.mule.runtime.extension.api.model.config to
      com.google.gson;
  opens org.mule.runtime.extension.api.model.connection to
      com.google.gson;
  opens org.mule.runtime.extension.api.model.construct to
      com.google.gson;
  opens org.mule.runtime.extension.api.model.deprecated to
      com.google.gson;
  opens org.mule.runtime.extension.api.model.function to
      com.google.gson;
  opens org.mule.runtime.extension.api.model.nested to
      com.google.gson;
  opens org.mule.runtime.extension.api.model.notification to
      com.google.gson;
  opens org.mule.runtime.extension.api.model.operation to
      com.google.gson;
  opens org.mule.runtime.extension.api.model.parameter to
      com.google.gson;
  opens org.mule.runtime.extension.api.model.source to
      com.google.gson;
  opens org.mule.runtime.extension.api.property to
      com.google.gson;
  opens org.mule.runtime.extension.api.values to
      com.google.gson;
  opens org.mule.runtime.extension.internal.util to
      com.google.gson;

  uses org.mule.runtime.extension.api.dsql.DsqlParser;
  uses org.mule.runtime.extension.api.dsl.syntax.resources.spi.DslResourceFactory;
  uses org.mule.runtime.extension.api.dsl.syntax.resources.spi.ExtensionSchemaGenerator;
  uses org.mule.runtime.extension.api.loader.ExtensionModelLoaderProvider;
  uses org.mule.runtime.extension.api.provider.RuntimeExtensionModelProvider;
  uses org.mule.runtime.extension.api.resources.spi.GeneratedResourceFactory;
  uses org.mule.runtime.extension.api.metadata.ComponentMetadataConfigurerFactoryDelegate;

}
