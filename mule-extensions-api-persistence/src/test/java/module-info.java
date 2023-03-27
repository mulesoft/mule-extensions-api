/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

/**
 * test module with the requires clauses needed only for the tests but not the productive code. 
 */
module org.mule.runtime.extensions.api.persistence.test {
  
  requires org.mule.runtime.extensions.api.persistence;
  
  requires org.mule.runtime.api;
  requires org.mule.runtime.extensions.api;
  requires org.mule.runtime.metadata.model.api;
  requires org.mule.runtime.metadata.model.java;
  requires org.mule.runtime.metadata.model.json;
  requires org.mule.runtime.metadata.model.persistence;
  
  requires com.google.gson;
  requires net.bytebuddy;
  requires net.bytebuddy.agent;

  exports org.mule.runtime.extension.api.persistence.test to
      junit;
}
