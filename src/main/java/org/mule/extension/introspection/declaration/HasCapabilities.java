package org.mule.extension.introspection.declaration;

/**
 * A contract interface for an object capable of registering capabilities
 * in behalf of a {@link Construct}
 *
 * @param <T> the type of {@link Construct} for which capabilities are being received
 * @since 1.0
 */
public interface HasCapabilities<T extends Construct>
{

    /**
     * Adds the given {@code capability}
     *
     * @param capability a capability
     * @return {@value this}
     */
    public T withCapability(Object capability);


}
