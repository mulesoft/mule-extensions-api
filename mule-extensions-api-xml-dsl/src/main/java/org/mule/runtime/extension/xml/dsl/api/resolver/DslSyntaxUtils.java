/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.api.resolver;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;
import static org.mule.metadata.internal.utils.MetadataTypeUtils.getTypeId;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;
import static org.mule.runtime.extension.xml.dsl.api.XmlModelUtils.supportsTopLevelDeclaration;
import org.mule.metadata.api.model.AnyType;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.DictionaryType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.NumberType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.StringType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.api.meta.model.ElementDslModel;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.api.meta.model.display.LayoutModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.FlattenedTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.LayoutTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.api.util.SubTypesMappingContainer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utils class with helper methods for the {@link DslSyntaxResolver}
 *
 * @since 1.0
 */
class DslSyntaxUtils {

  static boolean isValidBean(ObjectType objectType) {
    return isInstantiable(objectType) && !objectType.getFields().isEmpty();
  }

  static boolean isFlattened(ObjectFieldType field, MetadataType fieldValue) {
    return fieldValue instanceof ObjectType && field.getAnnotation(FlattenedTypeAnnotation.class).isPresent();
  }

  static String getTypeKey(MetadataType type, String namespace, String namespaceUri) {
    return getId(type) + namespace + namespaceUri;
  }

  static String getId(MetadataType type) {
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

  static Map<MetadataType, XmlDslModel> loadImportedTypes(ExtensionModel extension, DslResolvingContext context) {
    return extension.getImportedTypes().stream().collect(toMap(ImportedTypeModel::getImportedType, imported -> {
      ExtensionModel extensionModel = context.getExtension(imported.getOriginExtensionName())
          .orElseThrow(() -> new IllegalArgumentException(format(
                                                                 "The Extension [%s] is not present in the current context",
                                                                 imported.getOriginExtensionName())));
      return extensionModel.getXmlDslModel();
    }));
  }

  static SubTypesMappingContainer loadSubTypes(ExtensionModel extension) {
    return new SubTypesMappingContainer(extension.getSubTypes());
  }

  static boolean supportTopLevelElement(MetadataType metadataType) {
    return supportTopLevelElement(metadataType, metadataType.getAnnotation(XmlHintsAnnotation.class)
        .map(XmlHintsAnnotation::allowsReferences).orElse(true));
  }

  static boolean supportTopLevelElement(MetadataType metadataType, ElementDslModel elementDslModel) {
    return supportTopLevelElement(metadataType, elementDslModel.allowsReferences());
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
    return supportsInlineDeclaration(metadataType, expressionSupport, ElementDslModel.getDefaultInstance(), false);
  }

  static boolean supportsInlineDeclaration(MetadataType metadataType, ExpressionSupport expressionSupport, boolean isContent) {
    return supportsInlineDeclaration(metadataType, expressionSupport, ElementDslModel.getDefaultInstance(), isContent);
  }

  static boolean supportsInlineDeclaration(MetadataType metadataType, ExpressionSupport expressionSupport,
                                           ElementDslModel dslModel, boolean isContent) {
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
          public void visitDictionary(DictionaryType dictionaryType) {
            supportsChildDeclaration.set(false);
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
        } else {
          supportsChildDeclaration.set(isValidBean(objectType));
        }
      }

      @Override
      public void visitUnion(UnionType unionType) {
        supportsChildDeclaration.set(false);
      }

      @Override
      public void visitDictionary(DictionaryType dictionaryType) {
        dictionaryType.getKeyType().accept(new MetadataTypeVisitor() {

          @Override
          public void visitString(StringType stringType) {
            supportsChildDeclaration.set(true);
          }

          @Override
          public void visitNumber(NumberType numberType) {
            supportsChildDeclaration.set(true);
          }

          @Override
          protected void defaultVisit(MetadataType metadataType) {
            supportsChildDeclaration.set(false);
          }
        });
      }
    });

    return supportsChildDeclaration.get();
  }

}
