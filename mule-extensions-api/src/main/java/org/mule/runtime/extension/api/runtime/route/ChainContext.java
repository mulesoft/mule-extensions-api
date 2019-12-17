package org.mule.runtime.extension.api.runtime.route;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.component.location.ComponentLocation;

import java.util.Map;
import java.util.Optional;

@NoImplement
public interface ChainContext {

  ComponentLocation getComponentLocation();

  Map<String, Object> getVariables();

  <T> Optional<T> getVariable(String key);

  boolean hasVariable(String key);

  void addVariable(String key, Object value);
  
}
