/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence.metadata;

import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.metadata.MetadataAttributes;
import org.mule.runtime.api.metadata.descriptor.ComponentMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;

import java.util.List;
import java.util.Objects;

class ComponentMetadataResult<T extends ComponentModel>
    implements Descriptable<MetadataResult<ComponentMetadataDescriptor<T>>> {

  static final String TYPE = "type";
  static final String COMPONENT = "component";
  static final String OPERATION = "OPERATION";
  static final String SOURCE = "SOURCE";

  private final T model;
  private final String type;
  private final MetadataAttributes metadataAttributes;
  private final List<MetadataFailure> failures;

  ComponentMetadataResult(MetadataResult<ComponentMetadataDescriptor<T>> result) {
    ComponentMetadataDescriptor<T> descriptor = result.get();
    this.model = descriptor != null ? descriptor.getModel() : null;
    this.metadataAttributes = descriptor != null ? descriptor.getMetadataAttributes() : null;

    if (model != null) {
      if (model instanceof OperationModel) {
        this.type = OPERATION;
      } else if (model instanceof SourceModel) {
        this.type = SOURCE;
      } else {
        throw new IllegalArgumentException("Unknown model type");
      }
    } else {
      this.type = null;
    }

    this.failures = result.getFailures();
  }

  public boolean isOperation() {
    return Objects.equals(type, OPERATION);
  }

  public boolean isSource() {
    return Objects.equals(type, SOURCE);
  }

  public T getModel() {
    return model;
  }

  public MetadataAttributes getMetadataAttributes() {
    return metadataAttributes;
  }

  public List<MetadataFailure> getFailures() {
    return failures;
  }

  @Override
  public MetadataResult<ComponentMetadataDescriptor<T>> toDescriptor() {
    ComponentMetadataDescriptor<T> descriptor =
        ComponentMetadataDescriptor.builder(model).withAttributes(metadataAttributes).build();
    return failures.isEmpty() ? success(descriptor) : failure(descriptor, failures);
  }
}
