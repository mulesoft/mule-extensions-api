/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

import static java.lang.String.format;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModelVisitor;
import org.mule.runtime.api.meta.model.nested.NestedChainModel;
import org.mule.runtime.api.meta.model.nested.NestedComponentModel;
import org.mule.runtime.api.meta.model.nested.NestedRouteModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.util.Reference;
import org.mule.runtime.extension.api.model.nested.ImmutableNestedChainModel;
import org.mule.runtime.extension.api.model.nested.ImmutableNestedComponentModel;
import org.mule.runtime.extension.api.model.nested.ImmutableNestedRouteModel;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * A {@link TypeAdapter} for serializing instances of {@link OperationModel} and all its child interfaces
 *
 * @since 1.0
 */
public class NestedElementModelTypeAdapter extends KindEnrichedTypeAdapter<NestableElementModel> {

  private static final String COMPONENT_KIND = "component";
  private static final String CHAIN_KIND = "chain";
  private static final String ROUTE_KIND = "route";

  public NestedElementModelTypeAdapter(TypeAdapterFactory typeAdapterFactory, Gson gson) {
    super(typeAdapterFactory, gson);
  }

  @Override
  protected String getKind(NestableElementModel value) {
    Reference<String> kind = new Reference<>();
    value.accept(new NestableElementModelVisitor() {

      @Override
      public void visit(NestedComponentModel component) {
        kind.set(COMPONENT_KIND);
      }

      @Override
      public void visit(NestedChainModel component) {
        kind.set(CHAIN_KIND);
      }

      @Override
      public void visit(NestedRouteModel component) {
        kind.set(ROUTE_KIND);
      }
    });

    return kind.get();
  }

  @Override
  protected TypeAdapter<NestableElementModel> getDelegateAdapter(String kind) {
    Class clazz;
    switch (kind) {
      case ROUTE_KIND:
        clazz = ImmutableNestedRouteModel.class;
        break;
      case CHAIN_KIND:
        clazz = ImmutableNestedChainModel.class;
        break;
      case COMPONENT_KIND:
        clazz = ImmutableNestedComponentModel.class;
        break;
      default:
        throw new IllegalArgumentException(format("Unknown kind [%s] for a NestedElementModel", kind));
    }

    return gson.getDelegateAdapter(typeAdapterFactory, TypeToken.get(clazz));
  }
}
