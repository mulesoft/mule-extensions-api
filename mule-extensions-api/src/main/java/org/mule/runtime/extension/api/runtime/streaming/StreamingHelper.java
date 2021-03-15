/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.streaming;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.streaming.Cursor;
import org.mule.runtime.api.streaming.CursorProvider;
import org.mule.runtime.api.streaming.bytes.CursorStreamProvider;
import org.mule.runtime.api.streaming.object.CursorIteratorProvider;

import java.io.InputStream;
import java.util.Map;

/**
 * This class provides helper methods to deal with repeatable streaming resources which are contained into structures of arbitrary
 * complexity.
 * <p>
 * As you know, when a parameter is resolved to a {@link CursorProvider}, the runtime automatically obtains a {@link Cursor} and
 * injects that value instead. However, if that provider is embedded as the value of a {@link Map} or an arbitrary pojo, the
 * runtime can't guess that's the case and go resolve it.
 * <p>
 * Same thing applies when a component is producing a result. The runtime automatically converts returned {@link InputStream} or
 * {@link PagingProvider} instances into {@link CursorProvider} ones. However, if such instances are contained in some other
 * value, that resolution won't happen automatically either.
 * <p>
 * For these border cases, this class provides some utilities to adapt the {@link CursorProvider providers} into {@link Cursor
 * cursors} and vice versa
 *
 * @see CursorProvider
 * @see CursorStreamProvider
 * @see CursorIteratorProvider
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.runtime.streaming.StreamingHelper} instead.
 */
@Deprecated
@NoImplement
public interface StreamingHelper extends org.mule.sdk.api.runtime.streaming.StreamingHelper {

}
