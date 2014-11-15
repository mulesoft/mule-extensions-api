/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.mule.extensions.introspection.declaration.Declaration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Test;

public class DataQualifierFactoryTestCase
{

    @Test
    public void bool()
    {
        doAssert(DataQualifier.BOOLEAN, boolean.class, Boolean.class);
    }

    @Test
    public void string()
    {
        doAssert(DataQualifier.STRING, String.class, char.class, Character.class);
    }

    @Test
    public void integer()
    {
        doAssert(DataQualifier.INTEGER, int.class, short.class, Integer.class, Short.class);
    }

    @Test
    public void doubleQualifier()
    {
        doAssert(DataQualifier.DOUBLE, double.class, float.class, Double.class, Float.class);
    }

    @Test
    public void longQualifier()
    {
        doAssert(DataQualifier.LONG, long.class, Long.class);
    }

    @Test
    public void decimal()
    {
        doAssert(DataQualifier.DECIMAL, BigDecimal.class, BigInteger.class);
    }

    @Test
    public void dateTime()
    {
        doAssert(DataQualifier.DATE_TIME, java.sql.Time.class, java.sql.Timestamp.class, Calendar.class, XMLGregorianCalendar.class);
    }

    @Test
    public void enumQualifier()
    {
        doAssert(DataQualifier.ENUM, Enumeration.class, Enum.class, DataQualifier.class);
    }

    @Test
    public void list()
    {
        doAssert(DataQualifier.LIST, List.class, Set.class, Object[].class);
    }

    @Test
    public void map()
    {
        doAssert(DataQualifier.MAP, Map.class);
    }

    @Test
    public void operation()
    {
        doAssert(DataQualifier.OPERATION, Operation.class);
    }

    @Test
    public void bean()
    {
        doAssert(DataQualifier.POJO, Extension.class, Declaration.class, Object.class);
    }

    private void doAssert(DataQualifier expected, Class<?>... types)
    {
        for (Class<?> type : types)
        {
            DataQualifier evaluated = DataQualifierFactory.getQualifier(type);
            assertThat(String.format("was expecting %s but type %s returned %s instead", expected, type.getName(),  evaluated),
                       evaluated, equalTo(expected));
        }
    }
}
