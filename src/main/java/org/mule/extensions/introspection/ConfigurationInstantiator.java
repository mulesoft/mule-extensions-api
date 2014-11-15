package org.mule.extensions.introspection;

/**
 * Creates instances of objects which are compliant
 * with the model described by the owning {@link Configuration} object.
 *
 * @since 1.0
 */
public interface ConfigurationInstantiator
{

    /**
     * Returns a new instance of an object which is compliant
     * with the model described by the owning {@link Configuration} object.
     * @return a new object
     */
    public Object newInstance();

    /**
     * Returns the type of the object to be returned by this instance
     * @return a {@link Class}
     */
    public Class<?> getObjectType();

}
