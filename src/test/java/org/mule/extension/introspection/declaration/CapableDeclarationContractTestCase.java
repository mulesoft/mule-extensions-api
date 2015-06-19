/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection.declaration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import org.mule.extension.introspection.declaration.fluent.CapableDeclaration;

import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public abstract class CapableDeclarationContractTestCase<T extends CapableDeclaration>
{

    private static final Object testCapability1 = new Object();
    private static final Object testCapability2 = "hello";

    protected T declaration;

    @Before
    public void before()
    {
        declaration = createDeclaration();
    }

    protected abstract T createDeclaration();

    @Test
    public void getCapabilities()
    {
        addCapabilities();
        Collection<Object> capabilities = declaration.getCapabilities();

        assertThat(capabilities.contains(testCapability1), is(true));
        assertThat(capabilities.contains(testCapability2), is(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void capabilitiesAreImmutable()
    {
        declaration.getCapabilities().add(new Object());
    }

    @Test
    public void getCapabilitiesByType()
    {
        addCapabilities();

        Collection<String> matchingCapabilities = declaration.getCapabilities(String.class);
        assertThat(matchingCapabilities, hasSize(1));
        assertThat(matchingCapabilities, contains(testCapability2));
    }

    @Test
    public void isCapableOf()
    {
        addCapabilities();

        assertThat(declaration.isCapableOf(String.class), is(true));
        assertThat(declaration.isCapableOf(Date.class), is(false));
    }

    private void addCapabilities()
    {
        declaration.addCapability(testCapability1);
        declaration.addCapability(testCapability2);
    }
}
