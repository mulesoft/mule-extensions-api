/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
/**
 * API for Mule SOAP Based Extensions to integrate with the Mule Runtime in a decoupled way.
 * 
 * @moduleGraph
 * @since 1.5
 */
module org.mule.runtime.extensions.soap.api {

  requires org.mule.runtime.api;
  requires org.mule.runtime.extensions.api;
  requires org.mule.runtime.metadata.model.api;

  requires org.apache.commons.io;
  requires com.google.common;

  exports org.mule.runtime.extension.api.soap;
  exports org.mule.runtime.extension.api.soap.message;
  exports org.mule.runtime.extension.api.soap.metadata;
  exports org.mule.runtime.extension.api.soap.security;
  exports org.mule.runtime.extension.api.soap.security.config;
  exports org.mule.runtime.extension.api.soap.annotation;

}
