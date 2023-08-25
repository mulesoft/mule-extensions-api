/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.loader.enricher;

import static java.util.Optional.of;
import static org.mule.metadata.api.utils.MetadataTypeUtils.getTypeId;
import static org.mule.runtime.extension.api.loader.DeclarationEnricherPhase.WIRING;
import static org.mule.runtime.extension.internal.loader.util.InfrastructureParameterBuilder.addRedeliveryPolicy;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.meta.model.ImportedTypeModel;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.store.ObjectStore;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.DeclarationEnricherPhase;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.api.loader.IdempotentDeclarationEnricherWalkDelegate;
import org.mule.runtime.extension.api.loader.WalkingDeclarationEnricher;
import org.mule.runtime.extension.api.property.NoRedeliveryPolicyModelProperty;

import java.util.Optional;

/**
 * A {@link DeclarationEnricher} which adds a redelivery policy parameter to all sources
 *
 * @since 1.5.0
 */
public final class RedeliveryPolicyDeclarationEnricher implements WalkingDeclarationEnricher {

  @Override
  public DeclarationEnricherPhase getExecutionPhase() {
    return WIRING;
  }

  @Override
  public Optional<DeclarationEnricherWalkDelegate> getWalkDelegate(ExtensionLoadingContext extensionLoadingContext) {
    return of(new IdempotentDeclarationEnricherWalkDelegate() {

      ExtensionDeclaration extension = extensionLoadingContext.getExtensionDeclarer().getDeclaration();
      boolean hasObjectStoreParams = false;

      @Override
      protected void onSource(SourceDeclaration declaration) {
        if (declaration.getModelProperty(NoRedeliveryPolicyModelProperty.class).isPresent()) {
          return;
        }

        addRedeliveryPolicy(declaration);
        hasObjectStoreParams = true;
      }

      @Override
      public void onWalkFinished() {
        if (hasObjectStoreParams && !isObjectStoreAlreadyImported(extension)) {
          ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();
          extension.getImportedTypes().add(new ImportedTypeModel((ObjectType) typeLoader.load(ObjectStore.class)));
        }
      }
    });
  }

  private boolean isObjectStoreAlreadyImported(ExtensionDeclaration extension) {
    return extension.getImportedTypes().stream().anyMatch(model -> isObjectStore(model.getImportedType()));
  }

  private boolean isObjectStore(MetadataType type) {
    return getTypeId(type)
        .filter(typeId -> ObjectStore.class.getName().equals(typeId))
        .isPresent();
  }
}
