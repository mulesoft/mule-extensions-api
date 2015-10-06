/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.runtime;

import java.nio.charset.Charset;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

/**
 * Groups information which qualifies content.
 * <p>
 * Instances of this class are immutable
 *
 * @since 1.0
 */
public final class ContentType
{

    public static final String ANY_MIME_TYPE = "*/*";

    private final Charset encoding;
    private final String mimeType;

    private static Charset parseEncoding(String encoding)
    {
        if (encoding == null)
        {
            return Charset.defaultCharset();
        }

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
     * Creates a new instance for the given {@code encoding} using
     * the default &quot;{@link #ANY_MIME_TYPE}&quot; mime type
     *
     * @param encoding the name of a supported {@link Charset}
     * @throws IllegalArgumentException if {@code encoding} refers to an unsupported {@link Charset}
     */
    public ContentType(String encoding)
    {
        this(parseEncoding(encoding), ANY_MIME_TYPE);
    }

    /**
     * Creates a new instance for the given {@code encoding} and {@code mimeType}
     *
     * @param encoding a supported {@link Charset}
     * @param mimeType a mimeType. Cannot be {@code null}
     */
    public ContentType(Charset encoding, String mimeType)
    {
        if (encoding == null)
        {
            throw new IllegalArgumentException("encoding cannot be null");
        }

        this.encoding = encoding;

        if (mimeType == null)
        {
            throw new IllegalArgumentException("mimeType cannot be null");
        }

        if (ANY_MIME_TYPE.equals(mimeType))
        {
            this.mimeType = mimeType;
        }
        else
        {
            try
            {
                this.mimeType = new MimeType(mimeType).toString();
            }
            catch (MimeTypeParseException e)
            {
                throw new IllegalArgumentException(String.format("MimeType '%s' is not valid", mimeType), e);
            }
        }
    }

    /**
     * @return a {@link Charset}
     */
    public Charset getEncoding()
    {
        return encoding;
    }

    /**
     * @return a {@link String} which represents a mimeType. Can be {code null}
     */
    public String getMimeType()
    {
        return mimeType;
    }
}
