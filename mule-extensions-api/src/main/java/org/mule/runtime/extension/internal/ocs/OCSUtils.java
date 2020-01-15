/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.ocs;

import static org.mule.runtime.extension.internal.ocs.OCSConstants.OCS_SERVICE_URL;

import org.mule.runtime.api.component.ConfigurationProperties;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;

public final class OCSUtils {

  public static boolean isOCSEnabled(ExtensionLoadingContext loadingContext) {
    return loadingContext.getConfigurationProperties().map(OCSUtils::isOCSEnabled).orElse(false);
  }

  public static boolean isOCSEnabled(ConfigurationProperties configurationProperties) {
    return configurationProperties.resolveStringProperty(OCS_SERVICE_URL).isPresent();
  }

  private OCSUtils() {}
}
