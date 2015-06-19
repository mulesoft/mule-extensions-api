package org.mule.extension.introspection;

import org.mule.extension.introspection.declaration.DescribingContext;
import org.mule.extension.introspection.declaration.fluent.Descriptor;
import org.mule.extension.introspection.declaration.spi.DescriberPostProcessor;

/**
 * A factory that can take a {@link Descriptor} and transform it into an actual
 * {@link Extension}. It does not simply performs a transformation between the
 * two representation models. It also discovers available {@link DescriberPostProcessor}
 * and executes them to enrich the model before doing the transformation.
 *
 * @since 1.0
 */
public interface ExtensionFactory
{

    /**
     * Creates a {@link Extension} from the given {@link Descriptor}
     *
     * @param descriptor a {@link Descriptor}. Cannot be {@code null}
     * @return an {@link Extension}
     */
    Extension createFrom(Descriptor descriptor);

    /**
     * Creates a {@link Extension} from the given {@link Descriptor}
     * using a specifing {@code describingContext}
     *
     * @param descriptor         a {@link Descriptor}. Cannot be {@code null}
     * @param describingContext a {@link DescribingContext}, useful to specify custom settings
     * @return an {@link Extension}
     */
    Extension createFrom(Descriptor descriptor, DescribingContext describingContext);
}
