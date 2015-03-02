/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection;

/**
 * Visitor interface to be used with
 * {@link DataQualifier}
 * <p/>
 * Because the qualifier is an enum, it's not a traditional implementation of visitor
 * in which the dynamic linking relies on the concrete class of the visitable object. Instead,
 * a direct dispatch is done at a qualifier level.
 *
 * @since 1.0
 */
public interface DataQualifierVisitor
{

    /**
     * Invoked when visiting {@link DataQualifier#BOOLEAN}
     */
    void onBoolean();

    /**
     * Invoked when visiting {@link DataQualifier#INTEGER}
     */
    void onInteger();

    /**
     * Invoked when visiting {@link DataQualifier#DOUBLE}
     */
    void onDouble();

    /**
     * Invoked when visiting {@link DataQualifier#DECIMAL}
     */
    void onDecimal();

    /**
     * Invoked when visiting {@link DataQualifier#STRING}
     */
    void onString();

    /**
     * Invoked when visiting {@link DataQualifier#LONG}
     */
    void onLong();

    /**
     * Invoked when visiting {@link DataQualifier#ENUM}
     */
    void onEnum();

    /**
     * Invoked when visiting {@link DataQualifier#DATE_TIME}
     */
    void onDateTime();

    /**
     * Invoked when visiting {@link DataQualifier#POJO}
     */
    void onPojo();

    /**
     * Invoked when visiting {@link DataQualifier#LIST}
     */
    void onList();

    /**
     * Invoked when visiting {@link DataQualifier#MAP}
     */
    void onMap();

    /**
     * Invoked when visiting {@link DataQualifier#OPERATION}
     */
    void onOperation();

}
