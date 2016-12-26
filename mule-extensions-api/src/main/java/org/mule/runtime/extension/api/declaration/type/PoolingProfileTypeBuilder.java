/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.config.PoolingProfile.DEFAULT_EVICTION_INTERVAL_MILLIS;
import static org.mule.runtime.api.config.PoolingProfile.DEFAULT_MAX_POOL_ACTIVE;
import static org.mule.runtime.api.config.PoolingProfile.DEFAULT_MAX_POOL_IDLE;
import static org.mule.runtime.api.config.PoolingProfile.DEFAULT_MAX_POOL_WAIT;
import static org.mule.runtime.api.config.PoolingProfile.DEFAULT_MIN_EVICTION_MILLIS;
import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.config.PoolingProfile;

/**
 * Creates instances of {@link MetadataType} which represent a {@link PoolingProfile}.
 *
 * Notice that this representation of the type matches how the pooling profile is exposed
 * through the tooling and the DSL. It's not an exact match of what you'd get running the
 * pooling profile through a {@link ClassTypeLoader}
 *
 * @since 1.0
 */
public final class PoolingProfileTypeBuilder extends InfrastructureTypeBuilder {

  /**
   * @return a new {@link MetadataType} representation of a {@link PoolingProfile}
   */
  public MetadataType buildPoolingProfileType() {
    final ObjectTypeBuilder objectType = create(JAVA).objectType().id(PoolingProfile.class.getName());
    final BaseTypeBuilder typeBuilder = create(JAVA);

    objectType
        .description("A pooling profile is used to configure the pooling behaviour of Mule components. Each component can have its own pooling profile.");

    addIntField(objectType, typeBuilder, "maxActive",
                "Controls the maximum number of Mule components that can be borrowed from a session at one time. "
                    + "When set to a negative value, there is no limit to the number of components that may be active at one time. "
                    + "When maxActive is exceeded, the pool is said to be exhausted.",
                DEFAULT_MAX_POOL_ACTIVE);

    addIntField(objectType, typeBuilder, "maxIdle",
                "Controls the maximum number of Mule components that can sit idle in the pool at any time. "
                    + "When set to a negative value, there is no limit to the number of Mule components that may be idle at one time.",
                DEFAULT_MAX_POOL_IDLE);
    addLongField(objectType, typeBuilder, "maxWait",
                 "Specifies the number of milliseconds to wait for a pooled component to become available when the pool is exhausted "
                     + "and the exhaustedAction is set to WHEN_EXHAUSTED_WAIT.",
                 DEFAULT_MAX_POOL_WAIT);
    addIntField(objectType, typeBuilder, "minEvictionMillis",
                "Determines the minimum amount of time an object may sit idle in the pool before it is eligible for eviction. "
                    + "When non-positive, no objects will be evicted from the pool due to idle time alone.",
                DEFAULT_MIN_EVICTION_MILLIS);
    addIntField(objectType, typeBuilder, "evictionCheckIntervalMillis",
                "Specifies the number of milliseconds between runs of the object evictor. When non-positive, no object evictor is executed.",
                DEFAULT_EVICTION_INTERVAL_MILLIS);

    addEnumField(objectType, typeBuilder, "exhaustedAction",
                 "Specifies the behavior of the Mule component pool when the pool is exhausted. Possible values are: \"WHEN_EXHAUSTED_FAIL\", "
                     + "which will throw a NoSuchElementException, \"WHEN_EXHAUSTED_WAIT\", which will block by invoking Object.wait(long) until a new "
                     + "or idle object is available, or WHEN_EXHAUSTED_GROW, which will create a new Mule instance and return it, essentially "
                     + "making maxActive meaningless. If a positive maxWait value is supplied, it will block for at most that many milliseconds, "
                     + "after which a NoSuchElementException will be thrown. If maxThreadWait is a negative value, it will block indefinitely.",
                 "WHEN_EXHAUSTED_GROW",
                 "WHEN_EXHAUSTED_GROW", "WHEN_EXHAUSTED_WAIT", "WHEN_EXHAUSTED_FAIL");

    addEnumField(objectType, typeBuilder, "initialisationPolicy",
                 "Determines how components in a pool should be initialized. The possible values are: INITIALISE_NONE "
                     + "(will not load any components into the pool on startup), INITIALISE_ONE (will load one initial component into "
                     + "the pool on startup), or INITIALISE_ALL (will load all components in the pool on startup)",
                 "INITIALISE_ONE",
                 "INITIALISE_NONE", "INITIALISE_ONE", "INITIALISE_ALL");

    addBooleanField(objectType, typeBuilder, "disabled", "Whether pooling should be disabled", false);

    return objectType.build();
  }
}
