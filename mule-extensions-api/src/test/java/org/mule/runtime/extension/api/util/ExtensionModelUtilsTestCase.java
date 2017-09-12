/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.extension.api.util;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.extension.api.util.ExtensionModelUtils.*;

import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.api.meta.model.source.SourceModel;

import org.junit.Test;

public class ExtensionModelUtilsTestCase {

  @Test
  public void componentWithImplicitAssociatedConfig() {
    ExtensionModel em = mock(ExtensionModel.class);
    OperationModel ope = mock(OperationModel.class);
    ConfigurationModel c = mock(ConfigurationModel.class);
    ConnectionProviderModel cp = mock(ConnectionProviderModel.class);
    ParameterModel param = mock(ParameterModel.class);
    when(param.isRequired()).thenReturn(false);
    when(cp.getAllParameterModels()).thenReturn(singletonList(param));
    when(c.getAllParameterModels()).thenReturn(emptyList());
    when(c.getOperationModels()).thenReturn(singletonList(ope));
    when(c.getConnectionProviders()).thenReturn(singletonList(cp));
    when(em.getConfigurationModels()).thenReturn(singletonList(c));

    assertThat(componentHasAnImplicitConfiguration(em, ope), is(true));
  }

  @Test
  public void componentWithoutImplicitAssociatedConfig() {
    ExtensionModel em = mock(ExtensionModel.class);
    SourceModel source = mock(SourceModel.class);
    ConfigurationModel c = mock(ConfigurationModel.class);
    when(c.getAllParameterModels()).thenReturn(emptyList());
    when(c.getSourceModels()).thenReturn(singletonList(source));
    when(em.getConfigurationModels()).thenReturn(singletonList(c));
    ConnectionProviderModel cp = mock(ConnectionProviderModel.class);
    ParameterModel param = mock(ParameterModel.class);
    when(param.isRequired()).thenReturn(true);
    when(cp.getAllParameterModels()).thenReturn(singletonList(param));
    when(c.getConnectionProviders()).thenReturn(singletonList(cp));

    assertThat(componentHasAnImplicitConfiguration(em, source), is(false));
  }

}
