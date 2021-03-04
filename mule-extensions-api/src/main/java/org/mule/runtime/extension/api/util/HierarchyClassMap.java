/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A utility implementation of {@link Map} which keys are instances of {@link Class} and values are instances of generic type
 * {@code V}.
 * <p>
 * This classes' value comes from its {@link #get(Object)} and {@link #containsKey(Object)} methods being redefined to support
 * hierarchical lookups. For example, consider types {@code Dog} and {@code RabidDog}, such that {@code RabidDog} extends
 * {@code Dog}. If the map contains both keys, then the behaviour is the same as in a standard map. However, if
 * {@link #get(Object)} is invoked the key {@code Dog} but only {@code RabidDog} is mapped, this implementation will still find a
 * value for {@code Dog}, since it will recursively traverse across all entries looking for keys which are assignable from that
 * key. When no value is found, then the search is retried using the key's superclass. Recursion ends when the {@link Object}
 * class is reached or when no superclass is available (if you searched for Object in the first place) A consistent behaviour will
 * occur when using the {@link #containsKey(Object)} method.
 * <p>
 * This also works when the key is an interface, with the difference that no recursion is performed in that case.
 * <p>
 * <h1>Interfaces</h1> If an interface key is used and no explicit mapping is defined, no special behaviour is used.
 * <h1>Ordering considerations</h1>
 * <ul>
 * <li>Exact matches are always privileged. Meaning that if the {@code Dog} key has a specific value mapped, it will be preferred
 * over a {@code RabidDog} key</li>
 * <li>This implementation works by wrapping an existing map (look at the constructors Javadocs). When performing searches, the
 * iteration order depends on the rules of the underlying match. When multiple entries could match a specific query, no guarantees
 * are offered regarding which value will be returned on each invokation</li>
 * </ul>
 * <p>
 * <h1>Performance considerations</h1> When there's an explicit mapping for a given key, the performance is the same as in the
 * backing map. When a deep search occurs, then the performance drops to O(n^n) (worst case).
 * <h1>Other methods</h1> Other than {@link #get(Object)} and {@link #containsKey(Object)}, no other method has been overridden.
 * Behaviour will be that of the backing map.
 *
 * @param <V> the generic type of the mapped values
 * @since 1.0
 */
public class HierarchyClassMap<V> implements Map<Class<?>, V> {

  private final Map<Class<?>, V> delegate;

  /**
   * Creates a new instance which behaves like a {@link HashMap}
   */
  public HierarchyClassMap() {
    this(new LinkedHashMap<>());
  }

  /**
   * Creates a new instance which wraps the given {@code delegate}, inheriting its rules.
   *
   * @param delegate a backing {@link Map} with predictable iteration order
   */
  public HierarchyClassMap(Map<Class<?>, V> delegate) {
    this.delegate = delegate;
  }

  /**
   * Fetches a value associated to the given {@code key} following the rules described on the class javadoc
   *
   * @param key the key
   * @return the associated value or {@code null}
   */
  @Override
  public V get(Object key) {
    if (delegate.containsKey(key)) {
      return delegate.get(key);
    }

    Class<?> searchKey = (Class<?>) key;
    while (searchKey != null && !Object.class.equals(searchKey)) {
      V value = searchAssignableFrom(searchKey);
      if (value == null) {
        searchKey = searchKey.getSuperclass();
      } else {
        return value;
      }
    }

    return null;
  }

  private V searchAssignableFrom(Class<?> searchKey) {
    return entrySet().stream()
        .filter(entry -> searchKey.isAssignableFrom(entry.getKey()))
        .map(Entry::getValue)
        .findFirst()
        .orElse(null);
  }

  @Override
  public int size() {
    return delegate.size();
  }

  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }

  /**
   * Determines if there's a matching mapping by the rules described on the class javadoc.
   *
   * @param key the considered key
   * @return whether there's a mapping for this key or not
   */
  @Override
  public boolean containsKey(Object key) {
    if (delegate.containsKey(key)) {
      return true;
    }

    Class<?> searchKey = (Class<?>) key;
    while (searchKey != null && !Object.class.equals(searchKey)) {
      final Class<?> lambdaKey = searchKey;
      Optional<Class<?>> foundKey = delegate.keySet().stream()
          .filter(clazz -> lambdaKey.isAssignableFrom(clazz))
          .findFirst();

      if (foundKey.isPresent()) {
        return true;
      } else {
        searchKey = searchKey.getSuperclass();
      }
    }

    return false;
  }

  @Override
  public boolean containsValue(Object value) {
    return delegate.containsValue(value);
  }


  @Override
  public V put(Class<?> key, V value) {
    return delegate.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return delegate.remove(key);
  }

  @Override
  public void putAll(Map<? extends Class<?>, ? extends V> m) {
    delegate.putAll(m);
  }

  @Override
  public void clear() {
    delegate.clear();
  }

  @Override
  public Set<Class<?>> keySet() {
    return delegate.keySet();
  }

  @Override
  public Collection<V> values() {
    return delegate.values();
  }

  @Override
  public Set<Entry<Class<?>, V>> entrySet() {
    return delegate.entrySet();
  }

  @Override
  public boolean equals(Object o) {
    return delegate.equals(o);
  }

  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  @Override
  public V getOrDefault(Object key, V defaultValue) {
    return containsKey(key)
        ? get(key)
        : defaultValue;
  }

  @Override
  public void forEach(BiConsumer<? super Class<?>, ? super V> action) {
    delegate.forEach(action);
  }

  @Override
  public void replaceAll(BiFunction<? super Class<?>, ? super V, ? extends V> function) {
    delegate.replaceAll(function);
  }

  @Override
  public V putIfAbsent(Class<?> key, V value) {
    return delegate.putIfAbsent(key, value);
  }

  @Override
  public boolean remove(Object key, Object value) {
    return delegate.remove(key, value);
  }

  @Override
  public boolean replace(Class<?> key, V oldValue, V newValue) {
    return delegate.replace(key, oldValue, newValue);
  }

  @Override
  public V replace(Class<?> key, V value) {
    return delegate.replace(key, value);
  }

  @Override
  public V computeIfAbsent(Class<?> key, Function<? super Class<?>, ? extends V> mappingFunction) {
    return delegate.computeIfAbsent(key, mappingFunction);
  }

  @Override
  public V computeIfPresent(Class<?> key, BiFunction<? super Class<?>, ? super V, ? extends V> remappingFunction) {
    return delegate.computeIfPresent(key, remappingFunction);
  }

  @Override
  public V compute(Class<?> key, BiFunction<? super Class<?>, ? super V, ? extends V> remappingFunction) {
    return delegate.compute(key, remappingFunction);
  }

  @Override
  public V merge(Class<?> key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
    return delegate.merge(key, value, remappingFunction);
  }
}
