/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
