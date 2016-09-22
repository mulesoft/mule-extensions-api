/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.xml.dsl.api.resolver;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static org.mule.metadata.utils.MetadataTypeUtils.getTypeId;
import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.REQUIRED;
import static org.mule.runtime.extension.xml.dsl.api.XmlModelUtils.supportsTopLevelDeclaration;
import org.mule.metadata.api.model.AnyType;
import org.mule.metadata.api.model.ArrayType;
import org.mule.metadata.api.model.DictionaryType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.metadata.api.model.UnionType;
import org.mule.metadata.api.visitor.MetadataTypeVisitor;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.ExtensibleTypeAnnotation;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.FlattenedTypeAnnotation;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.TextTypeAnnotation;
import org.mule.runtime.extension.api.introspection.declaration.type.annotation.XmlHintsAnnotation;
import org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.property.ImportedTypesModelProperty;
import org.mule.runtime.extension.api.introspection.property.LayoutModelProperty;
import org.mule.runtime.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.runtime.extension.api.util.SubTypesMappingContainer;
import org.mule.runtime.extension.xml.dsl.api.property.XmlHintsModelProperty;
import org.mule.runtime.extension.xml.dsl.api.property.XmlModelProperty;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
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
    return getTypeId(type)
        .orElse((type.getAnnotation(ClassInformationAnnotation.class).map(ClassInformationAnnotation::getName).orElse("")))
        + namespace + namespaceUri;
  }

  static boolean isText(ParameterModel parameter) {
    return parameter.getModelProperty(LayoutModelProperty.class).map(LayoutModelProperty::isText).orElse(false);
  }

  static boolean isText(MetadataType type) {
    return type.getAnnotation(TextTypeAnnotation.class).isPresent();
  }

  static boolean isInstantiable(MetadataType metadataType) {
    Optional<ClassInformationAnnotation> classInformation = metadataType.getAnnotation(ClassInformationAnnotation.class);
    return classInformation.map(ClassInformationAnnotation::isInstantiable).orElse(false);
  }

  static boolean isExtensible(MetadataType metadataType) {
    return metadataType.getAnnotation(ExtensibleTypeAnnotation.class).isPresent();
  }

  static Map<MetadataType, XmlModelProperty> loadImportedTypes(ExtensionModel extension, DslResolvingContext context) {
    final Map<MetadataType, XmlModelProperty> xmlByType = new HashMap<>();

    extension.getModelProperty(ImportedTypesModelProperty.class)
        .map(ImportedTypesModelProperty::getImportedTypes)
        .ifPresent(imports -> imports
            .forEach((type, ownerExtension) -> {
              ExtensionModel extensionModel = context.getExtension(ownerExtension)
                  .orElseThrow(
                               () -> new IllegalArgumentException(format("The Extension [%s] is not present in the current context",
                                                                         ownerExtension)));
              XmlModelProperty xml = extensionModel.getModelProperty(XmlModelProperty.class)
                  .orElseThrow(() -> new IllegalArgumentException(
                                                                  format("The Extension [%s] doesn't have the required model property [%s]",
                                                                         ownerExtension, XmlModelProperty.NAME)));
              xmlByType.put(type, xml);
            }));

    return xmlByType;
  }

  static SubTypesMappingContainer loadSubTypes(ExtensionModel extension) {
    return new SubTypesMappingContainer(extension.getModelProperty(SubTypesModelProperty.class)
        .map(SubTypesModelProperty::getSubTypesMapping)
        .orElse(ImmutableMap.of()));
  }

  static XmlModelProperty loadXmlProperties(ExtensionModel extension) {
    return extension.getModelProperty(XmlModelProperty.class)
        .orElseThrow(() -> new IllegalArgumentException(
                                                        format("The extension [%s] does not have the [%s], required for its Xml Dsl Resolution",
                                                               extension.getName(), XmlModelProperty.class.getSimpleName())));
  }

  static boolean supportTopLevelElement(MetadataType metadataType) {
    return supportTopLevelElement(metadataType, metadataType.getAnnotation(XmlHintsAnnotation.class)
        .map(XmlHintsAnnotation::allowsReferences).orElse(true));
  }

  static boolean supportTopLevelElement(MetadataType metadataType, Optional<XmlHintsModelProperty> ownerXmlHints) {
    return supportTopLevelElement(metadataType, ownerXmlHints.map(XmlHintsModelProperty::allowsReferences).orElse(true));
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
    return supportsInlineDeclaration(metadataType, expressionSupport, empty());
  }

  static boolean supportsInlineDeclaration(MetadataType metadataType, ExpressionSupport expressionSupport,
                                           Optional<XmlHintsModelProperty> xmlHints) {
    final AtomicBoolean supportsChildDeclaration = new AtomicBoolean(false);

    if (REQUIRED == expressionSupport) {
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
        arrayType.getType().accept(this);
      }

      @Override
      public void visitObject(ObjectType objectType) {
        if (xmlHints.isPresent() && !xmlHints.get().allowsInlineDefinition()) {
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
        supportsChildDeclaration.set(true);
      }
    });

    return supportsChildDeclaration.get();
  }

}
