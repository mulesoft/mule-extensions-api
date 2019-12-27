package org.mule.runtime.extension.api.runtime.route;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.component.location.ComponentLocation;

import java.util.Optional;

@NoImplement
public interface ChainContext {

  ComponentLocation getComponentLocation();

  Optional<ChainState> getState(String extensionNamespace, String componentName);
  
}
