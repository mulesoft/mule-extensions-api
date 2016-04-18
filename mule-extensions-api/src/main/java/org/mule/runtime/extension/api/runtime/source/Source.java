/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.runtime.extension.api.introspection.source.SourceModel;

import java.io.Serializable;

/**
 * Base class to write message sources compliant with a given {@link SourceModel}.
 * <p>
 * This class acts as an adapter between the extensions API representation of a message source
 * and Mule's actual MessageSource concept.
 * <p>
 * The source is configured through a {@link SourceContext} which is set using the
 * {@link #setSourceContext(SourceContext)} method. Implementations can access
 * the set value through the {@link #sourceContext} field.
 * <p>
 * This class relies on generics to specify the payload and attribute types that the source
 * is going to generate. Although the java compiler does allow for raw uses of the class,
 * this API forbids that since those generics are needed for metadata purposes. For the case
 * of Sources which don't generate a response, the {@code Payload} is to be assigned to
 * {@link Void}.
 *
 * @param <Payload>    the generic type for the generated message's payload
 * @param <Attributes> the generic type for the generated message's attributes
 * @since 1.0
 */
public abstract class Source<Payload, Attributes extends Serializable>
{

    /**
     * The configured {@link SourceContext}. The platform is to have set
     * this through the {@link #setSourceContext(SourceContext)} method
     * before {@link #start()} is to be invoked
     */
    protected SourceContext<Payload, Attributes> sourceContext;

    //TODO: MULE-8946, should actually implement Startable
    public abstract void start();

    //TODO: MULE-8946, should actually implement Stoppable
    public abstract void stop();

    /**
     * Configures {@code this} instances with {@code sourceContext}.
     * This method should only be used by the runtime, which also
     * guarantees to have used it before {@link #start()} is invoked
     *
     * @param sourceContext a {@link SourceContext}
     */
    public void setSourceContext(SourceContext<Payload, Attributes> sourceContext)
    {
        this.sourceContext = sourceContext;
    }
}
