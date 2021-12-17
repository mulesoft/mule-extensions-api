/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;

import org.junit.Before;
import org.junit.Test;

public class BooleanParameterDeclarationEnricherTestCase {

  private ExtensionDeclaration declaration = null;

  @Before
  public void setUp() {
    // ExtensionDeclarer declarer =
    // new DefaultExtensionModelLoaderDelegate(HeisenbergExtension.class, MuleManifest.getProductVersion())
    // .declare(new DefaultExtensionLoadingContext(getClass().getClassLoader(), getDefault(emptySet())));
    // new BooleanParameterDeclarationEnricher()
    // .enrich(new DefaultExtensionLoadingContext(declarer, this.getClass().getClassLoader(), getDefault(emptySet())));
    // declaration = declarer.getDeclaration();
  }

  @Test
  public void verifyConfigurationModelPropertyOnOperation() {
    // ParameterDeclaration booleanDeclaration =
    // MuleExtensionUtils.getNamedObject(declaration.getConfigurations().get(0).getAllParameters(), "lovesMinerals");
    // assertThat(booleanDeclaration.isRequired(), is(FALSE));
    // assertThat(booleanDeclaration.getDefaultValue(), is(valueOf(FALSE)));
    //
    // booleanDeclaration =
    // MuleExtensionUtils.getNamedObject(declaration.getConfigurations().get(0).getAllParameters(), "worksAtDEA");
    // assertThat(booleanDeclaration.isRequired(), is(FALSE));
    // assertThat(booleanDeclaration.getDefaultValue(), is(valueOf(TRUE)));
  }
}
