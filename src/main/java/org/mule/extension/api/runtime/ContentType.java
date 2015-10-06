/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.runtime;

import java.nio.charset.Charset;

/**
 * Groups information which qualifies content.
 * <p>
 * An instance of this class is expected to exist per every operation execution.
 * Therefore, this class is mutable, assuming that each operation should have a
 * mechanism to get a hold on {@code this} instance and mutate conveniently. Changes
 * to {code this} instance should be reflected on the underlying {@code MuleMessage}
 *
 * @since 1.0
 */
public class ContentType
{

    private Charset encoding;
    private String mimeType;

    private static Charset parseEncoding(String encoding)
    {
        try
        {
            return Charset.forName(encoding);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(String.format("'%s' is not a valid encoding", encoding), e);
        }
    }

    /**
     * Creates a new instance for the given {@code encoding} and {@code mimeType}
     *
     * @param encoding the name of a supported {@link Charset}
     * @param mimeType a mimeType. Can be {@code null}
     * @throws IllegalArgumentException if {@code encoding} refers to an unsupported {@link Charset}
     */
    public ContentType(String encoding, String mimeType)
    {
        this(parseEncoding(encoding), mimeType);
    }

    /**
     * Creates a new instance for the given {@code encoding} and {@code mimeType}
     *
     * @param encoding a supported {@link Charset}
     * @param mimeType a mimeType. Can be {@code null}
     */
    public ContentType(Charset encoding, String mimeType)
    {
        this.encoding = encoding;
        this.mimeType = mimeType;
    }

    /**
     * @return a {@link Charset}
     */
    public Charset getEncoding()
    {
        return encoding;
    }

    /**
     * Sets a new encoding value
     *
     * @param encoding the new {@link Charset}
     */
    public void setEncoding(Charset encoding)
    {
        this.encoding = encoding;
    }

    /**
     * @return a {@link String} which represents a mimeType. Can be {code null}
     */
    public String getMimeType()
    {
        return mimeType;
    }

    /**
     * Sets a new mimeType
     *
     * @param mimeType a {@link String} which represents a mimeType. Can be {code null}
     */
    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }
}
