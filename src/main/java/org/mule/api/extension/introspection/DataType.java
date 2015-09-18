/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.api.extension.introspection;

import java.util.Arrays;
import java.util.Objects;

/**
 * A definition of an abstract data type, which provides information
 * that goes beyond it's actual {@link java.lang.reflect.Type}, but also
 * provides information about its parametrized generic types and
 * {@link DataQualifier}
 *
 * @since 1.0
 */
public final class DataType
{

    private final Class<?> type;
    private final DataType[] genericTypes;
    private final DataQualifier qualifier;

    /**
     * Returns a new {@link DataType} that
     * represents the given type. The returned instance will return an empty array
     * when queried for {@link #getGenericTypes()}
     *
     * @param clazz a not {@code null} {@link java.lang.Class}
     * @return a new {@link DataType}
     * @throws java.lang.IllegalArgumentException if the argument is null
     */
    public static DataType of(Class<?> clazz)
    {
        return of(clazz, (DataType[]) null);
    }

    /**
     * Returns a new {@link DataType} that
     * represents the given type with the optional generic types.
     *
     * @param clazz        a not {@code null} {@link java.lang.Class}
     * @param genericTypes an optional array of generic types accessible through {@link #getGenericTypes()}
     * @return a new {@link DataType}
     * @throws java.lang.IllegalArgumentException if the argument is null
     */
    public static DataType of(Class<?> clazz, Class<?>... genericTypes)
    {
        DataType[] types;
        if (genericTypes == null || genericTypes.length == 0)
        {
            types = new DataType[] {};
        }
        else
        {
            types = new DataType[genericTypes.length];
            for (int i = 0; i < genericTypes.length; i++)
            {
                types[i] = of(genericTypes[i]);
            }
        }

        return of(clazz, types);
    }

    /**
     * Returns a new {@link DataType} that
     * represents the given class and has the already provided {@link DataType}s
     * as {@link #getGenericTypes()}
     *
     * @param clazz        a not {@code null} {@link java.lang.Class}
     * @param genericTypes an optional array of {@link DataType} types accessible through {@link #getGenericTypes()}
     * @return a new {@link DataType}
     * @throws java.lang.IllegalArgumentException if the argument is null
     */
    public static DataType of(Class<?> clazz, DataType... genericTypes)
    {
        if (genericTypes == null)
        {
            genericTypes = new DataType[] {};
        }

        return new DataType(clazz, genericTypes, DataQualifierFactory.getQualifier(clazz));
    }

    private DataType(Class<?> type, DataType[] genericTypes, DataQualifier qualifier)
    {
        if (type == null)
        {
            throw new IllegalArgumentException("Can't build a DataType for a null type");
        }

        this.type = type;
        this.genericTypes = genericTypes;
        this.qualifier = qualifier;
    }

    /**
     * Returns the type's name
     *
     * @return a not {@code null} {@link java.lang.String}
     */
    public String getName()
    {
        return type.getName();
    }

    /**
     * Similar to {@link Class#isAssignableFrom(Class)}
     * but considering the {@link #getRawType()} of
     * {@code this} instance and {@code dataType}
     */
    public boolean isAssignableFrom(DataType dataType)
    {
        return type.isAssignableFrom(dataType.getRawType());
    }

    /**
     * Returns the {@link java.lang.Class} for the type described by this instance
     *
     * @return a non {@code null} {@link java.lang.Class}
     */
    public Class<?> getRawType()
    {
        return type;
    }

    /**
     * An array of nested {@link DataType}s which represent the
     * parametrized types for the type returned by {@link #getRawType()}
     *
     * @return an array of {@link DataType}. It might be empty but it will not be
     * {@code null}
     */
    public DataType[] getGenericTypes()
    {
        return genericTypes;
    }

    /**
     * A qualifier for the represented type
     *
     * @return a {@link DataQualifier}
     */
    public DataQualifier getQualifier()
    {
        return qualifier;
    }

    /**
     * Defines equality by checking that the given object is a
     * {@link DataType} with matching
     * {@link #getRawType()} and {@link #getQualifier()}, which also
     * returns a {@link #getGenericTypes()} which every element (if any) also matches
     * the one in this instance
     *
     * @param obj the object to test equality against
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof DataType)
        {
            DataType other = (DataType) obj;
            return type.equals(other.getRawType()) &&
                   Arrays.equals(genericTypes, other.getGenericTypes()) &&
                   qualifier.equals(other.getQualifier());
        }

        return false;
    }

    /**
     * Calculates this instance's hash code by considering
     * the {@link #getRawType()}, {@link #getQualifier()} and the individual
     * hashCode of each element in {@link #getGenericTypes()}. If the generic types
     * array is empty, then it's not considered.
     */
    @Override
    public int hashCode()
    {
        int genericTypesHash = Arrays.hashCode(genericTypes);

        if (genericTypesHash == 0)
        {
            genericTypesHash = 1; //neutralize factor
        }
        return genericTypesHash * Objects.hash(type, qualifier) * 31;
    }


}
