/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection;

/**
 * Provides a high level definition about the &quot;family&quot;
 * a given {@link DataType}
 *
 * @since 1.0
 */
public enum DataQualifier
{

    /**
     * A boolean type.
     */
    BOOLEAN
            {
                @Override
                public void accept(DataQualifierVisitor visitor)
                {
                    visitor.onBoolean();
                }
            },

    /**
     * A number with no decimal part
     */
    INTEGER
            {
                @Override
                public void accept(DataQualifierVisitor visitor)
                {
                    visitor.onInteger();
                }
            },

    /**
     * A double precision number
     */
    DOUBLE
            {
                @Override
                public void accept(DataQualifierVisitor visitor)
                {
                    visitor.onDouble();
                }
            },

    /**
     * A floating point number
     */
    DECIMAL
            {
                @Override
                public void accept(DataQualifierVisitor visitor)
                {
                    visitor.onDecimal();
                }
            },

    /**
     * A text type
     */
    STRING
            {
                @Override
                public void accept(DataQualifierVisitor visitor)
                {
                    visitor.onString();
                }
            },


    /**
     * A long integer
     */
    LONG
            {
                @Override
                public void accept(DataQualifierVisitor visitor)
                {
                    visitor.onLong();
                }
            },

    /**
     * An {@link java.lang.Enum} type
     */
    ENUM
            {
                @Override
                public void accept(DataQualifierVisitor visitor)
                {
                    visitor.onEnum();
                }
            },

    /**
     * A date with time
     */
    DATE_TIME
            {
                @Override
                public void accept(DataQualifierVisitor visitor)
                {
                    visitor.onDateTime();
                }
            },

    /**
     * A pojo implementing the bean contract
     */
    POJO
            {
                @Override
                public void accept(DataQualifierVisitor visitor)
                {
                    visitor.onPojo();
                }
            },

    /**
     * A java {@link java.util.Collection} type
     */
    LIST
            {
                @Override
                public void accept(DataQualifierVisitor visitor)
                {
                    visitor.onList();
                }
            },

    /**
     * A java {@link java.util.Map}
     */
    MAP
            {
                @Override
                public void accept(DataQualifierVisitor visitor)
                {
                    visitor.onMap();
                }
            },

    /**
     * A reference to another operation which will in turn
     * return another type. Consider this as a level of indirection
     */
    OPERATION
            {
                @Override
                public void accept(DataQualifierVisitor visitor)
                {
                    visitor.onOperation();
                }
            };

    public abstract void accept(DataQualifierVisitor visitor);
}
