/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type.annotation;

import static org.mule.runtime.api.util.Preconditions.checkState;
import org.mule.metadata.api.annotation.TypeAnnotation;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.runtime.api.meta.model.stereotype.StereotypeModel;
import org.mule.runtime.extension.api.stereotype.StereotypeDefinition;

import java.util.function.Function;

/**
 * A public {@link TypeAnnotation} intended to be used on {@link ObjectFieldType} types in order to
 * communicate an associated {@link StereotypeModel}
 *
 * @since 1.0
 */
public class StereotypeTypeAnnotation implements TypeAnnotation {

  public static final String NAME = "stereotype";
  private StereotypeModel stereotypeModel;
  private transient Class<? extends StereotypeDefinition> definitionClass;

  /**
   * Creates a new instance which only holds a reference to the type class. That class is to later
   * be resolved into a {@link StereotypeModel} through an invokation to the
   * @param definitionClass
   */
  public StereotypeTypeAnnotation(Class<? extends StereotypeDefinition> definitionClass) {
    this.definitionClass = definitionClass;
  }

  /**
   * Creates a new instance
   *
   * @param stereotypeModel the reference to be communicated
   */
  public StereotypeTypeAnnotation(StereotypeModel stereotypeModel) {
    this.stereotypeModel = stereotypeModel;
  }

  /**
   * @return An {@link StereotypeModel}
   */
  public StereotypeModel getStereotypeModel() {
    checkState(stereotypeModel != null, "The stereotypeModel has not yet been resolved");
    return stereotypeModel;
  }

  public void resolveStereotype(Function<Class<? extends StereotypeDefinition>, StereotypeModel> resolver) {
    checkState(stereotypeModel == null, "The stereotypeModel has already been resolved or provided");

    stereotypeModel = resolver.apply(definitionClass);
    definitionClass = null;
  }

  /**
   * {@inheritDoc}
   *
   * @return {@link #NAME}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * {@inheritDoc}
   *
   * @return {@code true}
   */
  @Override
  public boolean isPublic() {
    return true;
  }
}
