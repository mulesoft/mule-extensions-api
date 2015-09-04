/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.introspection;

import java.util.Set;

/**
 * An object is capable if it may provide different facets of additional behavior.
 * <p/>
 * Capabilities provide a mechanism for specific extensions to the API that do not break clients but may provide additional
 * new (non-mandatory) aspects.
 * <p/>
 * When building a new Capability, you should consider that it may not be recognized or used at all by existing
 * consumers. This limits the use of this feature to non-critical stuff or to extended contracts targeting a specific
 * consumer.
 * <p/>
 * Consumers of a capabilities must be prepared to deal with he fact that the object may not implement it.
 * <p/>
 * Capabilities are used to provide a future-proof path to incorporate changes that may otherwise
 * break backwards compatibility. New information can be provided in a new class exposed when specifically queried
 * as a capability. Hence, clients that were not compiled with the new class in scope will not fail, and only
 * clients aware of a specific capability will use it (that's the reason why there's no enumeration of
 * capabilities in the interface)
 *
 * @since 1.0
 */
public interface Capable
{

    /**
     * Returns a {@link java.util.Set} with the capabilities
     * which are an instance of the given {@code capabilityType}.
     * If no match is found, then an empty {@link java.util.Set} is
     * returned
     *
     * @param capabilityType the capability to be obtained.
     * @return a {@link java.util.Set}. Might be empty but will never be {@code null}
     */
    <T> Set<T> getCapabilities(Class<T> capabilityType);


    /**
     * Tells if this instance is capable of the given capability
     *
     * @param capabilityType a capability type
     * @return a boolean
     */
    boolean isCapableOf(Class<?> capabilityType);
}
