/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.introspection.declaration.type;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.ObjectType;

import org.junit.Test;

public class ExtensionFieldHandlerTestCase
{

    private ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

    @Test
    public void interfaceWithGetter()
    {
        ObjectType type = (ObjectType) typeLoader.load(HasGetter.class);
        assertThat(type.getFields(), hasSize(0));
    }

    interface HasGetter
    {

        String getSomeString();
    }
}
