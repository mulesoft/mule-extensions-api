/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
/**
 * API for Mule Extensions to add DateSense Query Language support.
 * 
 * @moduleGraph
 * @since 1.5
 */
module org.mule.runtime.extensions.api.dsql {
  
  requires org.mule.runtime.extensions.api;
  
  requires antlr.runtime;

  provides org.mule.runtime.extension.api.dsql.DsqlParser
      with org.mule.runtime.extension.internal.dsql.DefaultDsqlParser;

}
