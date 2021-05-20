/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.annotation.param;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import org.mule.runtime.api.meta.model.ComponentModel;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.parameter.ParameterModel;
import org.mule.runtime.extension.api.runtime.source.Source;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Binds the annotated member with the {@link ParameterModel} of the same {@link ParameterModel#getName() name} and
 * {@link ParameterModel#getType() type} that exists in the {@link ConfigurationModel config} associated to the
 * {@link ComponentModel container} of the annotated member. <br>
 * When annotated with {@link ConfigOverride}, the {@link ParameterModel parameter} will be injected with the same value of the
 * bound {@link ParameterModel parameter} of the {@link ConfigurationModel config} that's been associated to the execution. <br>
 * For example, if we declare the operation:
 * <p>
 * {@code
 *  public void request(@Config ConfigType config, @ConfigOverride int maxRetries)
 * }
 * <p>
 * Where the configuration {@code ConfigType} declares a parameter as:
 * 
 * <pre>
 * <code>
 *  {@literal @}Parameter
 *  {@literal @}Optional(defaultValue = "10")
 *  {@literal @}int maxRetries;
 * </code>
 * </pre>
 * <p>
 * Then we can have three different cases:
 * <p>
 * 1) We use the default value of the {@code maxRetries} parameter provided by the config. In this case, the operation
 * {@code request} will be injected with {@code 10} as the value for {@code maxRetries}
 * 
 * <pre>
 * {@code
 * <mule>
 *  <ns:config name="sampleConfig">
 *
 *  <flow>
 *     <ns:request config-ref="sampleConfig">
 *  </flow>
 * </mule>
 * }
 * </pre>
 *
 * 2) We provide a value for the {@code maxRetries} parameter in the config. In this case, the operation {@code request} will be
 * injected with {@code 2} as the value for {@code maxRetries}
 * 
 * <pre>
 * {@code
 * <mule>
 *  <ns:config name="sampleConfig" maxRetries=2>
 *
 *  <flow>
 *     <ns:request config-ref="sampleConfig">
 *  </flow>
 * </mule>
 * }
 * </pre>
 *
 * 3) We provide a value for the {@code maxRetries} parameter in the operation. In this case, the operation {@code request} will
 * be injected with {@code 5} as the value for {@code maxRetries}
 * 
 * <pre>
 * {@code
 * <mule>
 *  <ns:config name="sampleConfig" maxRetries=2>
 *
 *  <flow>
 *     <ns:request config-ref="sampleConfig" maxRetries=5>
 *  </flow>
 * </mule>
 * }
 * </pre>
 * <p>
 * This annotation can either be applied to an argument of an operation method or to a field of a class which extends the
 * {@link Source} class. It is not to be used on {@link ConfigurationModel configurations} nor {@link ConnectionProviderModel
 * connections}.
 *
 * @since 1.0
 */
@Target(value = {PARAMETER, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigOverride {

}
