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
 * This class provides helper methods to deal with repeatable streaming resources which are contained into structures of
 * arbitrary complexity.
 * <p>
 * As you know, when a parameter is resolved to a {@link CursorProvider}, the runtime automatically obtains a {@link Cursor}
 * and injects that value instead. However, if that provider is embedded as the value of a {@link Map} or an arbitrary pojo,
 * the runtime can't guess that's the case and go resolve it.
 * <p>
 * Same thing applies when a component is producing a result. The runtime automatically converts returned {@link InputStream}
 * or {@link PagingProvider} instances into {@link CursorProvider} ones. However, if such instances are contained in some other
 * value, that resolution won't happen automatically either.
 * <p>
 * For these border cases, this class provides some utilities to adapt the {@link CursorProvider providers} into
 * {@link Cursor cursors} and vice versa
 *
 * @see CursorProvider
 * @see CursorStreamProvider
 * @see CursorIteratorProvider
 * @since 1.0
 * @deprecated use {@link org.mule.sdk.api.extension.runtime.streaming.StreamingHelper} instead.
 */
@Deprecated
@NoImplement
public interface StreamingHelper {

  /**
   * Inspects the values of the given {@code map} looking for instances of {@link CursorProvider}.
   * A new equivalent map is returned, except that the {@link CursorProvider} values have been replaced by
   * obtained {@link Cursor cursors}. No side effect is applied on the original {@code map}.
   * <p>
   * A best effort will be made for the returned map to be of the same class as the original one. If that's not possible
   * (most likely because the class doesn't have an accessible default constructor), then the runtime will choose its
   * own map implementation, but guarantying that the iterator order is respected.
   * <p>
   * If the {@code map} contains a value of type {@link Map} and {@code recursive} is set to {@code true},
   * then that {@link Map} value will also be replaced by a new map which comes from recursively applying this
   * same method.
   *
   * @param map       a {@link Map} which may contain values of {@link CursorProvider} type
   * @param recursive Whether to also use this method to replace values of type {@link Map}
   * @param <K>       the generic type of the map's keys
   * @return a new {@link Map} which doesn't have values of {@link CursorProvider} type
   */
  <K> Map<K, Object> resolveCursors(Map<K, Object> map, boolean recursive);

  /**
   * Inspects the values of the given {@code map} looking for repeatable streaming resources such as {@link InputStream},
   * {@link Cursor}, streaming iterators, etc., A new equivalent map is returned, except that such values
   * have been replaced by {@link CursorProvider} instances. No side effect is applied on the original {@code map}.
   * <p>
   * For {@link Cursor} values, the same {@link CursorProvider} that already owns that {@link Cursor} is used. For other
   * streaming values, the operation's repeatable streaming strategy will be used. If that strategy doesn't apply
   * (e.g: the operation returns a {@link PagingProvider} but the streaming resource is an {@link InputStream}), then the
   * system's default matching strategy will be used instead.
   * <p>
   * A best effort will be made for the returned map to be of the same class as the original one. If that's not possible
   * (most likely because the class doesn't have an accessible default constructor), then the runtime will choose its
   * own map implementation, but guarantying that the iterator order is respected.
   * <p>
   * If the {@code map} contains a value of type {@link Map} and {@code recursive} is set to {@code true},
   * then that {@link Map} value will also be replaced by a new map which comes from recursively applying this
   * same method.
   *
   * @param map       a {@link Map} which may contain streaming values
   * @param recursive Whether to also use this method to replace values of type {@link Map}
   * @param <K>       the generic type of the map's keys
   * @return a new {@link Map} which streaming values may have been replaced by {@link CursorProvider} instances, depending
   * on the component's configuration.
   */
  <K> Map<K, Object> resolveCursorProviders(Map<K, Object> map, boolean recursive);

  /**
   * If the {@code value} is a {@link CursorProvider}, a corresponding {@link Cursor} is returned. The same
   * {@code value} is returned otherwise.
   *
   * @param value a value which may be a {@link CursorProvider}
   * @return a {@link Cursor} or the input value
   */
  Object resolveCursor(Object value);

  /**
   * If the {@code value} is a repeatable streaming resource such as {@link InputStream}, {@link Cursor},
   * streaming iterators, etc., then an equivalent {@link CursorProvider} is returned. If the {@code value} is not a
   * repeatable streaming resource or the owning component is configured not to use repeatable streams, then the same
   * {@code value} is returned.
   * <p>
   * For {@link Cursor} values, the same {@link CursorProvider} that already owns that {@link Cursor} is used. For other
   * streaming values, the operation's repeatable streaming strategy will be used. If that strategy doesn't apply
   * (e.g: the operation returns a {@link PagingProvider} but the streaming resource is an {@link InputStream}), then the
   * system's default matching strategy will be used instead.
   *
   * @param value a value which may be a repeatable streaming resource.
   * @return a {@link CursorProvider} or the same input {@code value}
   */
  Object resolveCursorProvider(Object value);
}
