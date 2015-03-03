package org.mule.extension.introspection;

import org.mule.extension.introspection.declaration.Construct;
import org.mule.extension.introspection.spi.DescriberPostProcessor;

/**
 * A factory that can take a {@link Construct} and transform it into an actual
 * {@link Extension}. It does not simply performs a transformation between the
 * two representation models. It also discovers available {@link DescriberPostProcessor}
 * and executes them to enrich the model before doing the transformation.
 *
 * @since 1.0
 */
public interface ExtensionFactory
{

    /**
     * Creates a {@link Extension} from the given {@link Construct}
     *
     * @param construct a {@link Construct}. Can't be {@code null}
     * @return a {@link Extension}
     */
    Extension createFrom(Construct construct);
}
