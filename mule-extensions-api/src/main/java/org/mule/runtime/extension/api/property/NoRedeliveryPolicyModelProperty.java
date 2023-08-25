/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.property;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.internal.loader.enricher.RedeliveryPolicyDeclarationEnricher;

/**
 * Marker {@link ModelProperty} meant to be applied on {@link SourceModel} instances. Signals that the annotated source should not
 * be enriched with a redelivery policy parameter.
 * <p>
 * This class is stateless, whenever possible use {@link #INSTANCE} instead of creating new instances for memory efficiency.
 *
 * @see RedeliveryPolicyDeclarationEnricher
 * @since 1.5.0
 */
public class NoRedeliveryPolicyModelProperty implements ModelProperty {

  public static final NoRedeliveryPolicyModelProperty INSTANCE = new NoRedeliveryPolicyModelProperty();

  @Override
  public String getName() {
    return "NoRedeliveryPolicy";
  }

  @Override
  public boolean isPublic() {
    return false;
  }
}
