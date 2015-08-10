package org.mule.extension.runtime;

/**
 * An entity which can or cannot be considered elegible for expiration
 * according to a {@link ExpirationPolicy}
 *
 * @since 1.0
 */
public interface Expirable
{

    /**
     * Determins if {@code this} instance should be expired based on the given
     * {@code expirationPolicy}
     *
     * @param expirationPolicy a {@link ExpirationPolicy}
     * @return {@code true} if {@code this} can be expired. {@code false} otherwise
     */
    boolean isExpired(ExpirationPolicy expirationPolicy);
}
