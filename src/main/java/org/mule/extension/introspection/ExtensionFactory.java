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
     * @param construct a {@link Construct}. Cannot be {@code null}
     * @return an {@link Extension}
     */
    Extension createFrom(Construct construct);

    /**
     * Creates a {@link Extension} from the given {@link Construct}
     * using a specifing {@code describingContext}
     *
     * @param construct         a {@link Construct}. Cannot be {@code null}
     * @param describingContext a {@link DescribingContext}, useful to specify custom settings
     * @return an {@link Extension}
     */
    Extension createFrom(Construct construct, DescribingContext describingContext);
}
