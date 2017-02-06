/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.dsl.syntax;

import static org.mule.metadata.api.utils.MetadataTypeUtils.getTypeId;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.isMap;
import static org.mule.runtime.extension.api.util.XmlModelUtils.supportsTopLevelDeclaration;
import org.mule.metadata.api.model.AnyType;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.ParameterDslConfiguration;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.FlattenedTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.LayoutTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.api.dsl.syntax.resolver.DslSyntaxResolver;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utils class with helper methods for the {@link DslSyntaxResolver}
 *
 * @since 1.0
 */
public final class DslSyntaxUtils {

  private DslSyntaxUtils() {}

  static boolean isValidBean(ObjectType objectType) {
    return isInstantiable(objectType) && !objectType.getFields().isEmpty();
  }

  public static boolean isFlattened(ObjectFieldType field, MetadataType fieldValue) {
    return fieldValue instanceof ObjectType && field.getAnnotation(FlattenedTypeAnnotation.class).isPresent();
  }

  static String getTypeKey(MetadataType type, String namespace, String namespaceUri) {
    return getId(type) + namespace + namespaceUri;
  }

  public static String getId(MetadataType type) {
    return getTypeId(type)
        .orElseGet(() -> type.getAnnotation(ClassInformationAnnotation.class)
            .map(ClassInformationAnnotation::getName)
            .orElse(""));
  }

  static boolean isText(ParameterModel parameter) {
    return parameter.getLayoutModel().map(LayoutModel::isText).orElse(false);
  }

  static boolean isText(MetadataType type) {
    return type.getAnnotation(LayoutTypeAnnotation.class).map(layoutTypeAnnotation -> layoutTypeAnnotation.isText())
        .orElse(false);
  }

  static boolean isInstantiable(MetadataType metadataType) {
    Optional<ClassInformationAnnotation> classInformation = metadataType.getAnnotation(ClassInformationAnnotation.class);
    return classInformation.map(ClassInformationAnnotation::isInstantiable).orElse(false);
  }

  static boolean isExtensible(MetadataType metadataType) {
    return metadataType.getAnnotation(ExtensibleTypeAnnotation.class).isPresent();
  }

  static boolean supportTopLevelElement(MetadataType metadataType) {
    return supportTopLevelElement(metadataType, metadataType.getAnnotation(XmlHintsAnnotation.class)
        .map(XmlHintsAnnotation::allowsReferences).orElse(true));
  }

  static boolean supportTopLevelElement(MetadataType metadataType, ParameterDslConfiguration dslConfiguration) {
    return supportTopLevelElement(metadataType, dslConfiguration.allowsReferences());
  }

  static boolean supportTopLevelElement(MetadataType metadataType, boolean allowsReferences) {
    if (!allowsReferences) {
      return false;
    }

    final AtomicBoolean supporstGlobalDeclaration = new AtomicBoolean(false);
    metadataType.accept(new MetadataTypeVisitor() {

      @Override
      public void visitObject(ObjectType objectType) {
        supporstGlobalDeclaration.set(supportsTopLevelDeclaration(objectType) && isValidBean(objectType));
      }
    });

    return supporstGlobalDeclaration.get();
  }

  static boolean supportsInlineDeclaration(MetadataType metadataType, ExpressionSupport expressionSupport) {
    return supportsInlineDeclaration(metadataType, expressionSupport, ParameterDslConfiguration.getDefaultInstance(), false);
  }

  static boolean supportsInlineDeclaration(MetadataType metadataType, ExpressionSupport expressionSupport, boolean isContent) {
    return supportsInlineDeclaration(metadataType, expressionSupport, ParameterDslConfiguration.getDefaultInstance(), isContent);
  }

  static boolean supportsInlineDeclaration(MetadataType metadataType, ExpressionSupport expressionSupport,
                                           ParameterDslConfiguration dslModel, boolean isContent) {
    final AtomicBoolean supportsChildDeclaration = new AtomicBoolean(false);

    if (isContent) {
      return true;
    } else if (REQUIRED == expressionSupport) {
      return false;
    }

    metadataType.accept(new MetadataTypeVisitor() {

      @Override
      protected void defaultVisit(MetadataType metadataType) {
        supportsChildDeclaration.set(true);
      }

      @Override
      public void visitAnyType(AnyType anyType) {
        supportsChildDeclaration.set(false);
      }

      @Override
      public void visitArrayType(ArrayType arrayType) {
        final MetadataTypeVisitor currentVisitor = this;
        arrayType.getType().accept(new MetadataTypeVisitor() {

          @Override
          public void visitObject(ObjectType objectType) {
            if (isMap(objectType)) {
              supportsChildDeclaration.set(false);
            } else {
              objectType.accept(currentVisitor);
            }
          }

          @Override
          protected void defaultVisit(MetadataType metadataType) {
            metadataType.accept(currentVisitor);
          }
        });
      }

      @Override
      public void visitObject(ObjectType objectType) {
        if (!dslModel.allowsInlineDefinition()) {
          supportsChildDeclaration.set(false);
        } else if (isMap(objectType)) {
          supportsChildDeclaration.set(true);
        } else {
          supportsChildDeclaration.set(isValidBean(objectType));
        }
      }

      @Override
      public void visitUnion(UnionType unionType) {
        supportsChildDeclaration.set(false);
      }
    });

    return supportsChildDeclaration.get();
  }

}
