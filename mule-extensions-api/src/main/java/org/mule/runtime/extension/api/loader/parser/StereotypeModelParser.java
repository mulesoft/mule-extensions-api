/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.loader.parser;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;

import java.util.Optional;

/**
 * Parses the syntactic definition of a {@link StereotypeModel}.
 *
 * @since 1.10.0
 */
@NoImplement
public interface StereotypeModelParser {

  Optional<StereotypeModel> getStereotype(StereotypeModelFactory factory);

}
