/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
/**
 * Persistence API for Mule Extensions.
 * 
 * @moduleGraph
 * @since 1.5
 */
module org.mule.runtime.extensions.api.persistence {
  
  requires org.mule.runtime.api;
  requires org.mule.runtime.extensions.api;
  requires org.mule.runtime.metadata.model.api;
  requires org.mule.runtime.metadata.model.persistence;
  
  requires java.xml.bind;
  requires com.google.common;
  requires com.google.gson;
  requires org.apache.commons.lang3;

  exports org.mule.runtime.extension.api.persistence;
  exports org.mule.runtime.extension.api.persistence.metadata;
  exports org.mule.runtime.extension.api.persistence.value;

  exports org.mule.runtime.extension.internal.persistence to
      org.mule.runtime.extensions.api.persistence.test;
  exports org.mule.runtime.extension.internal.xml to
      org.mule.runtime.extensions.support;
  
  provides org.mule.metadata.persistence.api.TypeAnnotationSerializerExtender
      with org.mule.runtime.extension.internal.persistence.ExtensionTypeAnnotationSerializerExtender;

  // Allow introspection for serialization/deserialization by Gson
  opens org.mule.runtime.extension.api.persistence.metadata to
      com.google.gson;
  opens org.mule.runtime.extension.internal.persistence.metadata to
      com.google.gson;

}
