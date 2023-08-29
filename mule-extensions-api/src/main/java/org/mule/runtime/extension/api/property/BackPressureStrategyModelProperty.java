/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.property;

import static org.mule.runtime.extension.api.runtime.source.BackPressureMode.WAIT;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.annotation.source.BackPressure;
import org.mule.runtime.extension.api.runtime.source.BackPressureMode;

/**
 * Private {@link ModelProperty} which describes the back pressure support of a given {@link SourceModel}
 *
 * @since 1.1
 */
public class BackPressureStrategyModelProperty implements ModelProperty {

  private BackPressureMode defaultMode;
  private Set<BackPressureMode> supportedModes;

  public static BackPressureStrategyModelProperty getDefault() {
    BackPressureStrategyModelProperty property = new BackPressureStrategyModelProperty();
    property.defaultMode = WAIT;
    property.supportedModes = EnumSet.of(WAIT);

    return property;
  }

  public static BackPressureStrategyModelProperty of(BackPressure annotation) {
    BackPressureStrategyModelProperty settings = new BackPressureStrategyModelProperty();
    settings.defaultMode = annotation.defaultMode();
    settings.supportedModes = new LinkedHashSet<>();
    for (BackPressureMode supported : annotation.supportedModes()) {
      settings.supportedModes.add(supported);
    }

    return settings;
  }

  private BackPressureStrategyModelProperty() {}

  public BackPressureStrategyModelProperty(BackPressureMode defaultMode, Set<BackPressureMode> supportedModes) {
    this.defaultMode = defaultMode;
    this.supportedModes = supportedModes;
  }

  public BackPressureMode getDefaultMode() {
    return defaultMode;
  }

  public Set<BackPressureMode> getSupportedModes() {
    return supportedModes;
  }

  /**
   * @return {@code backPressure}
   */
  @Override
  public String getName() {
    return "backPressure";
  }

  /**
   * @return {@code false}
   */
  @Override
  public boolean isPublic() {
    return false;
  }
}
