/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.mule.api.extension.introspection.DataQualifier;
import org.mule.api.extension.introspection.DataType;
import org.mule.api.extension.introspection.ExtensionModel;
import org.mule.api.extension.introspection.ParameterModel;
import org.mule.api.extension.introspection.declaration.fluent.Declaration;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class DataTypeTestCase
{

    @Test
    public void simpleType()
    {
        DataType type = DataType.of(String.class);
        assertEquals(String.class, type.getRawType());
        assertNoGenericTypes(type);
        assertThat(type.getQualifier(), is(DataQualifier.STRING));
    }

    @Test
    public void typeWithGeneric()
    {
        DataType type = DataType.of(Map.class, String.class, ExtensionModel.class);
        assertMap(type, String.class, DataQualifier.STRING, ExtensionModel.class, DataQualifier.POJO);
        assertNoGenericTypes(type.getGenericTypes()[0]);
        assertNoGenericTypes(type.getGenericTypes()[1]);
    }

    @Test
    public void complexTypeWithManyGenerics()
    {
        DataType introspectionMap = DataType.of(Map.class, ExtensionModel.class, ParameterModel.class);
        DataType declarationList = DataType.of(List.class, Declaration.class);
        DataType type = DataType.of(Map.class, introspectionMap, declarationList);

        assertMap(type, Map.class, DataQualifier.MAP, List.class, DataQualifier.LIST);
        assertMap(type.getGenericTypes()[0], ExtensionModel.class, DataQualifier.POJO, ParameterModel.class, DataQualifier.POJO);
        assertNoGenericTypes(type.getGenericTypes()[0].getGenericTypes()[0]);
        assertNoGenericTypes(type.getGenericTypes()[0].getGenericTypes()[1]);

        assertList(type.getGenericTypes()[1], Declaration.class, DataQualifier.POJO);
        assertNoGenericTypes(type.getGenericTypes()[1].getGenericTypes()[0]);
    }

    @Test
    public void equalsOfNoGenericType()
    {
        DataType type1 = DataType.of(String.class);
        DataType type2 = DataType.of(String.class);

        assertThat(type1, equalTo(type2));
    }

    @Test
    public void equalsWithGenericType()
    {
        DataType type1 = DataType.of(Map.class, String.class, Long.class);
        DataType type2 = DataType.of(Map.class, String.class, Long.class);

        assertThat(type1, equalTo(type2));
    }

    @Test
    public void notEqualsWithoutGenericTypes()
    {
        DataType type1 = DataType.of(ExtensionModel.class);
        DataType type2 = DataType.of(String.class);

        assertThat(type1, not(equalTo(type2)));
    }

    @Test
    public void notEqualsWithGenericTypes()
    {
        DataType type1 = DataType.of(Map.class, String.class, Long.class);
        DataType type2 = DataType.of(Map.class, String.class, ExtensionModel.class);

        assertThat(type1, not(equalTo(type2)));
    }

    @Test
    public void hashCodeOnEqualTypes()
    {
        DataType type1 = DataType.of(Map.class, String.class, Long.class);
        DataType type2 = DataType.of(Map.class, String.class, Long.class);

        assertThat(type1.hashCode(), equalTo(type2.hashCode()));
    }

    @Test
    public void hashCodeOnUnequalTypes()
    {
        DataType type1 = DataType.of(Map.class, String.class, Long.class);
        DataType type2 = DataType.of(Map.class, String.class, ExtensionModel.class);

        assertThat(type1.hashCode(), not(equalTo(type2.hashCode())));
    }

    private void assertList(DataType type, Class<?> valueType, DataQualifier valueQualifierType)
    {
        assertEquals(List.class, type.getRawType());
        assertThat(type.getGenericTypes().length, is(1));
        assertThat(type.getQualifier(), is(DataQualifier.LIST));
        assertEquals(valueType, type.getGenericTypes()[0].getRawType());
        assertThat(type.getGenericTypes()[0].getQualifier(), is(valueQualifierType));
    }


    private void assertMap(DataType type, Class<?> keyType, DataQualifier keyQualifier, Class<?> valueType, DataQualifier valueQualifier)
    {
        assertEquals(Map.class, type.getRawType());
        assertThat(type.getGenericTypes().length, is(2));
        assertThat(type.getQualifier(), is(DataQualifier.MAP));

        assertEquals(keyType, type.getGenericTypes()[0].getRawType());
        assertThat(type.getGenericTypes()[0].getQualifier(), is(keyQualifier));

        assertEquals(valueType, type.getGenericTypes()[1].getRawType());
        assertThat(type.getGenericTypes()[1].getQualifier(), is(valueQualifier));
    }

    private void assertNoGenericTypes(DataType type)
    {
        assertThat(type.getGenericTypes(), is(notNullValue()));
        assertThat(type.getGenericTypes().length, is(0));
    }
}
