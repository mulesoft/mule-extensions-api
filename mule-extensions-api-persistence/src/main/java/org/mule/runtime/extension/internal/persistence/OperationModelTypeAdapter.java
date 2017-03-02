/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.persistence;

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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * A {@link TypeAdapter} for serializing instances of {@link OperationModel} and all its
 * child interfaces
 *
 * @since 1.0
 */
public class OperationModelTypeAdapter extends TypeAdapter<OperationModel> {

  private static final String KIND = "kind";
  private static final String OPERATION_KIND = "operation";
  private static final String SCOPE_KIND = "scope";
  private static final String ROUTER_KIND = "router";


  private final TypeAdapterFactory typeAdapterFactory;
  private final Gson gson;

  public OperationModelTypeAdapter(TypeAdapterFactory typeAdapterFactory, Gson gson) {
    this.typeAdapterFactory = typeAdapterFactory;
    this.gson = gson;
  }

  @Override
  public void write(JsonWriter out, OperationModel value) throws IOException {
    JsonObject json = getDelegateAdapter(value).toJsonTree(value).getAsJsonObject();
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

    json.addProperty(KIND, kind.get());
    gson.toJson(json, out);
  }

  @Override
  public OperationModel read(JsonReader in) throws IOException {
    JsonObject json = new JsonParser().parse(in).getAsJsonObject();
    if (!json.has(KIND)) {
      throw new IllegalArgumentException("Invalid json. Operation doesn't specify " + KIND);
    }

    String kind = json.get(KIND).getAsString();
    Class<? extends OperationModel> operationClass;
    if (OPERATION_KIND.equals(kind)) {
      operationClass = ImmutableOperationModel.class;
    } else if (SCOPE_KIND.equals(kind)) {
      operationClass = ImmutableScopeModel.class;
    } else if (ROUTER_KIND.equals(kind)) {
      operationClass = ImmutableRouterModel.class;
    } else {
      throw new IllegalArgumentException("Invalid json. Operation specifies unknown kind: " + kind);
    }

    return gson.getDelegateAdapter(typeAdapterFactory, TypeToken.get(operationClass)).fromJsonTree(json);
  }

  private TypeAdapter<OperationModel> getDelegateAdapter(OperationModel value) {
    Class operationClass;
    if (value instanceof ScopeModel) {
      operationClass = ImmutableScopeModel.class;
    } else if (value instanceof RouterModel) {
      operationClass = ImmutableRouterModel.class;
    } else {
      operationClass = ImmutableOperationModel.class;
    }

    return gson.getDelegateAdapter(typeAdapterFactory, TypeToken.get(operationClass));
  }
}
