/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import static java.lang.String.format;
import static org.mule.runtime.api.util.Preconditions.checkState;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.operation.RouterModel;
import org.mule.runtime.api.meta.model.operation.ScopeModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.api.meta.model.util.ComponentModelVisitor;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.extension.api.model.operation.ImmutableOperationModel;
import org.mule.runtime.extension.api.model.operation.ImmutableRouterModel;
import org.mule.runtime.extension.api.model.operation.ImmutableScopeModel;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * A {@link TypeAdapter} for serializing instances of {@link OperationModel} and all its
 * child interfaces
 *
 * @since 1.0
 */
public class OperationModelTypeAdapter extends KindEnrichedTypeAdapter<OperationModel> {

  private static final String OPERATION_KIND = "operation";
  private static final String SCOPE_KIND = "scope";
  private static final String ROUTER_KIND = "router";

  public OperationModelTypeAdapter(TypeAdapterFactory typeAdapterFactory, Gson gson) {
    super(typeAdapterFactory, gson);
  }

  @Override
  protected String getKind(OperationModel value) {
    Reference<String> kind = new Reference<>();
    value.accept(new ComponentModelVisitor() {

      @Override
      public void visit(OperationModel operationModel) {
        kind.set(OPERATION_KIND);
      }

      @Override
      public void visit(ScopeModel scopeModel) {
        kind.set(SCOPE_KIND);
      }

      @Override
      public void visit(RouterModel routerModel) {
        kind.set(ROUTER_KIND);
      }

      @Override
      public void visit(SourceModel sourceModel) {}
    });

    checkState(kind.get() != null, format("Cannot infer '%s' property while serializing model '%s''", KIND, value.getName()));
    return kind.get();
  }

  @Override
  protected TypeAdapter<OperationModel> getDelegateAdapter(String kind) {
    Class operationClass;
    if (OPERATION_KIND.equals(kind)) {
      operationClass = ImmutableOperationModel.class;
    } else if (SCOPE_KIND.equals(kind)) {
      operationClass = ImmutableScopeModel.class;
    } else if (ROUTER_KIND.equals(kind)) {
      operationClass = ImmutableRouterModel.class;
    } else {
      throw new IllegalArgumentException("Invalid json. Operation specifies unknown kind: " + kind);
    }

    return gson.getDelegateAdapter(typeAdapterFactory, TypeToken.get(operationClass));
  }
}
