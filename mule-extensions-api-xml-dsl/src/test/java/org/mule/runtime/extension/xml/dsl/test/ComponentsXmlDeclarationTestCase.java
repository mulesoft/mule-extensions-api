/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.test;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.extension.api.util.NameUtils.hyphenize;
import org.mule.runtime.extension.xml.dsl.api.DslElementDeclaration;
import org.mule.runtime.extension.xml.dsl.api.resolver.DslElementResolver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComponentsXmlDeclarationTestCase extends BaseXmlDeclarationTestCase
{

    @Test
    public void testOperationDeclaration()
    {
        DslElementDeclaration result = new DslElementResolver(extension).resolve(operation);

        assertThat(result.getAttributeName(), is(EMPTY));
        assertThat(result.getElementName(), is(hyphenize(OPERATION_NAME)));
        assertThat(result.getElementNamespace(), is(NAMESPACE));
        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testSourceDeclaration()
    {
        DslElementDeclaration result = new DslElementResolver(extension).resolve(source);

        assertThat(result.getAttributeName(), is(EMPTY));
        assertThat(result.getElementName(), is(hyphenize(SOURCE_NAME)));
        assertThat(result.getElementNamespace(), is(NAMESPACE));
        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testConfigurationDeclaration()
    {
        DslElementDeclaration result = new DslElementResolver(extension).resolve(configuration);

        assertThat(result.getAttributeName(), is(EMPTY));
        assertThat(result.getElementName(), is(hyphenize(CONFIGURATION_NAME)));
        assertThat(result.getElementNamespace(), is(NAMESPACE));
        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

    @Test
    public void testConnectionProviderDeclaration()
    {
        DslElementDeclaration result = new DslElementResolver(extension).resolve(connectionProvider);

        assertThat(result.getAttributeName(), is(EMPTY));
        assertThat(result.getElementName(), is(hyphenize(CONNECTION_PROVIDER_NAME)));
        assertThat(result.getElementNamespace(), is(NAMESPACE));
        assertChildElementDeclarationIs(false, result);
        assertIsWrappedElement(false, result);
    }

}
