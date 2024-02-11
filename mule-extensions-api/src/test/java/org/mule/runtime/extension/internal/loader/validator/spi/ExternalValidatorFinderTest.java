package org.mule.runtime.extension.internal.loader.validator.spi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.extension.internal.loader.ExtensionModelFactory.COMPILATION_MODE_FLAG;

import org.mule.runtime.extension.ExternalValidator;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.ExtensionModelValidator;

import java.util.List;

import org.junit.Test;

public class ExternalValidatorFinderTest {

    @Test
    public void getExternalValidatorsOnCompilationMode() {
        ExtensionLoadingContext extensionLoadingContext = mock(ExtensionLoadingContext.class);
        when(extensionLoadingContext.getParameter(COMPILATION_MODE_FLAG)).thenReturn(java.util.Optional.of(true));
        List<ExtensionModelValidator> validators = ExternalValidatorFinder.find(extensionLoadingContext);
        assertThat(validators, hasItem(instanceOf(ExternalValidator.class)));
    }

    @Test
    public void getExternalValidatorsOnRuntimeMode() {
        ExtensionLoadingContext extensionLoadingContext = mock(ExtensionLoadingContext.class);
        List<ExtensionModelValidator> validators = ExternalValidatorFinder.find(extensionLoadingContext);
        assertThat(validators, empty());
    }
}