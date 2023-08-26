/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.metadata;

import static java.util.Collections.emptySet;
import static org.mule.runtime.extension.api.metadata.NullMetadataResolver.NULL_CATEGORY_NAME;
import static org.mule.runtime.extension.api.metadata.NullMetadataResolver.NULL_RESOLVER_NAME;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.VoidType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;
import org.mule.runtime.api.metadata.resolving.QueryEntityResolver;
import org.mule.runtime.extension.api.annotation.param.Query;

import java.util.Set;

/**
 * Null implementation of {@link OutputTypeResolver} used to represent the absence of output resolver in a {@link Query} annotated
 * operation.
 *
 * @since 1.0
 */
public final class NullQueryMetadataResolver implements OutputTypeResolver<String>, QueryEntityResolver {

  @Override
  public String getCategoryName() {
    return NULL_CATEGORY_NAME;
  }

  @Override
  public String getResolverName() {
    return NULL_RESOLVER_NAME;
  }

  /**
   * Null implementation of {@link OutputTypeResolver} for {@link Query} operations, used when no implementation is provided by
   * the connector developer. Represents the absence of a custom {@link OutputTypeResolver}, returning a {@link VoidType} instead
   * of resolving a dynamic {@link MetadataType} from a native {@link Query} for the component's output.
   *
   * @param context {@link MetadataContext} of the MetaData resolution
   * @param key     {@link MetadataKey} of string type, representing the native query created by the user.
   * @return a {@link VoidType} instance.
   */
  @Override
  public MetadataType getOutputType(MetadataContext context, String key) throws MetadataResolvingException, ConnectionException {
    return context.getTypeBuilder().voidType().build();
  }

  @Override
  public Set<MetadataKey> getEntityKeys(MetadataContext context) throws MetadataResolvingException, ConnectionException {
    return emptySet();
  }

  @Override
  public MetadataType getEntityMetadata(MetadataContext context, String key)
      throws MetadataResolvingException, ConnectionException {
    return context.getTypeBuilder().voidType().build();
  }
}
