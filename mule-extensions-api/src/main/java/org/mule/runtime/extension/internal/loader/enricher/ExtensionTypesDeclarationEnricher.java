/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.lang.String.format;
import static org.mule.metadata.api.utils.MetadataTypeUtils.getTypeId;
import static org.mule.runtime.extension.api.util.NameUtils.getComponentDeclarationTypeName;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.IntersectionType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.runtime.api.meta.model.declaration.fluent.ComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.extension.api.declaration.fluent.util.IdempotentDeclarationWalker;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;

import java.util.Set;

/**
 * Navigates all the components of the extension and automatically declares all complex types.
 *
 * @since 1.0
 */
public final class ExtensionTypesDeclarationEnricher implements DeclarationEnricher {

  @Override
  public void enrich(ExtensionLoadingContext extensionLoadingContext) {
    final ExtensionDeclarer declarer = extensionLoadingContext.getExtensionDeclarer();

    declareDefaultTypes(declarer);
    declareSubTypes(declarer);
  }

  private void declareSubTypes(ExtensionDeclarer declarer) {
    declarer.getDeclaration().getSubTypes().forEach(type -> registerTypes(declarer, type.getSubTypes()));
  }

  private void declareDefaultTypes(final ExtensionDeclarer declarer) {
    new IdempotentDeclarationWalker() {

      @Override
      protected void onParameter(ParameterGroupDeclaration parameterGroup, ParameterDeclaration declaration) {
        registerType(declarer, declaration.getType());
      }

      @Override
      public void onSource(SourceDeclaration declaration) {
        registerType(declarer, declaration);
      }

      @Override
      public void onOperation(OperationDeclaration declaration) {
        registerType(declarer, declaration);
      }

    }.walk(declarer.getDeclaration());
  }

  private void registerTypes(ExtensionDeclarer declarer, Set<ObjectType> types) {
    types.forEach(type -> registerType(declarer, type));
  }

  private void registerType(ExtensionDeclarer declarer, ComponentDeclaration declaration) {
    if (declaration.getOutput() == null) {
      throw new IllegalModelDefinitionException(format("%s '%s' doesn't specify an output type",
                                                       getComponentDeclarationTypeName(declaration), declaration.getName()));
    }

    if (declaration.getOutputAttributes() == null) {
      throw new IllegalModelDefinitionException(format("%s '%s' doesn't specify output attributes types",
                                                       getComponentDeclarationTypeName(declaration), declaration.getName()));
    }

    registerType(declarer, declaration.getOutput().getType());
    registerType(declarer, declaration.getOutputAttributes().getType());
  }

  private void registerType(ExtensionDeclarer declarer, MetadataType type) {
    if (!getTypeId(type).isPresent()) {
      return;
    }

    type.accept(new MetadataTypeVisitor() {

      @Override
      public void visitObject(ObjectType objectType) {
        declarer.withType(objectType);
        objectType.getOpenRestriction().ifPresent(type -> type.accept(this));
      }

      @Override
      public void visitArrayType(ArrayType arrayType) {
        arrayType.getType().accept(this);
      }

      @Override
      public void visitIntersection(IntersectionType intersectionType) {
        intersectionType.getTypes().forEach(type -> type.accept(this));
      }

      @Override
      public void visitUnion(UnionType unionType) {
        unionType.getTypes().forEach(type -> type.accept(this));
      }

      @Override
      public void visitObjectField(ObjectFieldType objectFieldType) {
        objectFieldType.getValue().accept(this);
      }
    });
  }
}
