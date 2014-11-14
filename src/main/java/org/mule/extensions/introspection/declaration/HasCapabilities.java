package org.mule.extensions.introspection.declaration;

public interface HasCapabilities<T extends Construct>
{

    public T withCapability(Object capability);


}
