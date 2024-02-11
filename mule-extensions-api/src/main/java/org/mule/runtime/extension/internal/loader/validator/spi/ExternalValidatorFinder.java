package org.mule.runtime.extension.internal.loader.validator.spi;

import static java.util.Collections.emptyList;
import static org.mule.runtime.extension.internal.loader.ExtensionModelFactory.COMPILATION_MODE_FLAG;

import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Finder of {@link ExtensionModelValidator} that are contributed using SPI.
 *
 * @since 1.3.0
 */
public class ExternalValidatorFinder {

    /**
     * Looks and find the available external {@link ExtensionModelValidator}
     *
     * @param extensionLoadingContext Loading context
     * @return All the {@link ExtensionModelValidator} that are contributed using SPI in the current class loader.
     */
    public static List<ExtensionModelValidator> find(ExtensionLoadingContext extensionLoadingContext) {
       return extensionLoadingContext.getParameter(COMPILATION_MODE_FLAG).map(flag -> {
            if (flag instanceof Boolean && (Boolean) flag) {
                List<ExtensionModelValidator> validators = new ArrayList<>();
                ServiceLoader<ExtensionModelValidator> load = ServiceLoader.load(ExtensionModelValidator.class);
                load.iterator().forEachRemaining(validators::add);
                return validators;
            } else {
                return Collections.<ExtensionModelValidator>emptyList();
            }
        }).orElse(emptyList());
    }
}
