/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.annotation.license;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines if the extension license requires a custom entitlement.
 * <p/>
 * This annotation can be used by connector developers to require a custom entitlement defined by the {@link #name()} attribute.
 * <p/>
 * Usually defining an entitlement also requires the connector developer to get a license generator from MuleSoft that can later
 * be used to create license for users of the connector. When requesting MuleSoft for a license generator a provider name must be
 * supplied which is usually the name of the company developing the connector. Such value must be also set as the extension vendor
 * in the {@link Extension#vendor()} annotation attribute.
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresEntitlement {

  /**
   * @return identifier of the required entitlement in the license
   */
  String name();

  /**
   * @return a description for the entitlement.
   */
  String description() default "";

}
