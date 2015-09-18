/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.api.extension.introspection;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Factory pattern implementation that provides a
 * {@link DataQualifier} for a given
 * {@link java.lang.Class}
 *
 * @since 1.0
 */
public final class DataQualifierFactory
{

    private interface DataTypeQualifierEvaluator
    {

        boolean isEvaluable(Class<?> c);

        DataQualifier evaluate(Class<?> c);
    }

    private static class DefaultQualifierEvaluator implements DataTypeQualifierEvaluator
    {

        private final Class<?>[] parentClasses;
        private final DataQualifier qualifier;

        private DefaultQualifierEvaluator(Class<?> parentClass, DataQualifier qualifier)
        {
            this(new Class[] {parentClass}, qualifier);
        }

        private DefaultQualifierEvaluator(Class<?>[] parentClasses, DataQualifier qualifier)
        {
            this.parentClasses = parentClasses;
            this.qualifier = qualifier;
        }

        @Override
        public DataQualifier evaluate(Class<?> c)
        {
            if (isEvaluable(c))
            {
                return qualifier;
            }
            return null;
        }

        @Override
        public boolean isEvaluable(Class<?> c)
        {
            for (Class<?> parentClass : parentClasses)
            {
                if (parentClass.isAssignableFrom(c))
                {
                    return true;
                }
            }
            return false;
        }
    }

    private static class EnumDataTypeQualifierEvaluator extends DefaultQualifierEvaluator
    {

        private EnumDataTypeQualifierEvaluator()
        {
            super(new Class<?>[] {Enumeration.class, Enum.class}, DataQualifier.ENUM);
        }

        @Override
        public boolean isEvaluable(Class<?> c)
        {
            return c.isEnum() || super.isEvaluable(c);
        }
    }

    private static class PojoTypeQualifierEvaluator extends DefaultQualifierEvaluator
    {

        private PojoTypeQualifierEvaluator()
        {
            super(Object.class, DataQualifier.POJO);
        }

        @Override
        public boolean isEvaluable(Class<?> c)
        {
            return !c.isPrimitive();
        }
    }

    private static final DataTypeQualifierEvaluator BOOLEAN_EVALUATOR = new DefaultQualifierEvaluator(
            new Class[] {boolean.class, Boolean.class}, DataQualifier.BOOLEAN);

    private static final DataTypeQualifierEvaluator STRING_EVALUATOR = new DefaultQualifierEvaluator(
            new Class[] {String.class, char.class, Character.class}, DataQualifier.STRING);

    private static final DataTypeQualifierEvaluator INTEGER_EVALUATOR = new DefaultQualifierEvaluator(
            new Class[] {int.class, short.class, Integer.class, Short.class}, DataQualifier.INTEGER);

    private static final DataTypeQualifierEvaluator DOUBLE_EVALUATOR = new DefaultQualifierEvaluator(
            new Class[] {double.class, float.class, Double.class, Float.class}, DataQualifier.DOUBLE);

    private static final DataTypeQualifierEvaluator LONG_EVALUATOR = new DefaultQualifierEvaluator(
            new Class[] {long.class, Long.class}, DataQualifier.LONG);

    private static final DataTypeQualifierEvaluator DECIMAL_EVALUATOR = new DefaultQualifierEvaluator(
            new Class[] {BigDecimal.class, BigInteger.class}, DataQualifier.DECIMAL);

    private static final DataTypeQualifierEvaluator DATE_TIME_EVALUATOR = new DefaultQualifierEvaluator(
            new Class[] {Date.class, java.sql.Date.class, Calendar.class, XMLGregorianCalendar.class, java.sql.Time.class,
                    java.sql.Timestamp.class}, DataQualifier.DATE_TIME);

    private static final DataTypeQualifierEvaluator ENUM_EVALUATOR = new EnumDataTypeQualifierEvaluator();

    private static final DataTypeQualifierEvaluator LIST_EVALUATOR = new DefaultQualifierEvaluator(
            new Class[] {List.class, Set.class, Object[].class}, DataQualifier.LIST);

    private static final DataTypeQualifierEvaluator MAP_EVALUATOR = new DefaultQualifierEvaluator(
            Map.class, DataQualifier.MAP);

    private static final DataTypeQualifierEvaluator OPERATION_EVALUATOR = new DefaultQualifierEvaluator(
            OperationModel.class, DataQualifier.OPERATION);

    private static final DataTypeQualifierEvaluator POJO_EVALUATOR = new PojoTypeQualifierEvaluator();

    private static final DataTypeQualifierEvaluator[] evaluators = new DataTypeQualifierEvaluator[] {
            BOOLEAN_EVALUATOR,
            STRING_EVALUATOR,
            INTEGER_EVALUATOR,
            DOUBLE_EVALUATOR,
            LONG_EVALUATOR,
            DECIMAL_EVALUATOR,
            DATE_TIME_EVALUATOR,
            ENUM_EVALUATOR,
            LIST_EVALUATOR,
            MAP_EVALUATOR,
            OPERATION_EVALUATOR,
            POJO_EVALUATOR
    };

    /**
     * Returns a {@link DataQualifier}
     * that corresponds to the given {@link java.lang.Class}
     *
     * @param clazz a not {@code null} {@link java.lang.Class}
     * @return a not {@code null} {@link DataQualifier}
     * @throws java.lang.IllegalArgumentException if the argument is {@code null}
     * @throws java.lang.IllegalArgumentException If no qualifier can be assigned to the given type
     */
    public static DataQualifier getQualifier(Class<?> clazz)
    {
        if (clazz == null)
        {
            throw new IllegalArgumentException("Can't get qualifier for a null class");
        }

        for (DataTypeQualifierEvaluator evaluator : evaluators)
        {
            DataQualifier qualifier = evaluator.evaluate(clazz);
            if (qualifier != null)
            {
                return qualifier;
            }
        }

        throw new IllegalArgumentException(String.format("Data Qualifier for class %s could not be found.", clazz.getName()));
    }
}

