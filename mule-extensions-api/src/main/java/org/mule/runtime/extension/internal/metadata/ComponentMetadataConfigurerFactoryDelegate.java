/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.metadata;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.extension.api.metadata.ComponentMetadataConfigurer;

/**
 * Factory of {@link ComponentMetadataConfigurer}.
 * 
 * @since 4.8
 * 
 * @deprecated Use org.mule.runtime.extension.api.metadata.ComponentMetadataConfigurerFactoryDelegate instead.
 */
@NoImplement
@Deprecated
public interface ComponentMetadataConfigurerFactoryDelegate
    extends org.mule.runtime.extension.api.metadata.ComponentMetadataConfigurerFactoryDelegate {

}
