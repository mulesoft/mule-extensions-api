/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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

  exports org.mule.runtime.extension.api.soap;
  exports org.mule.runtime.extension.api.soap.message;
  exports org.mule.runtime.extension.api.soap.metadata;
  exports org.mule.runtime.extension.api.soap.security;
  exports org.mule.runtime.extension.api.soap.security.config;
  exports org.mule.runtime.extension.api.soap.annotation;

}
