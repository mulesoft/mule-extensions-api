/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.operation;

import org.mule.runtime.api.message.Attributes;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.metadata.MediaType;

import java.util.Optional;

/**
 * Represents the result of an operation execution. Extensions can use this class
 * for cases in which the operation not only needs to return a value to be set
 * on the message payload but also wants to specify message attributes and/or
 * {@link MediaType}.
 * <p>
 * The {@link #getOutput()} value is always taken at face value, meaning that
 * if it's {@code null}, then the value that the operation returns to the runtime
 * will in fact be {@code null}. However, if the {@link #getAttributes()} or
 * {@link #getMediaType()} are {@link Optional#empty}, then the runtime will interpret
 * that as the operation not interested in setting those values, keeping the input message's
 * attributes and/or media type untouched.
 *
 * @param <T> the generic type of the output value
 * @param <A> the generic type of the message attributes
 * @since 1.0
 */
public interface Result<T, A extends Attributes> {

  /**
   * Creates a new {@link Builder}
   *
   * @param <T> the generic type of the output value
   * @param <A> the generic type of the message attributes
   * @return a new {@link Builder}
   */
  static <T, A extends Attributes> Builder<T, A> builder() {
    return OperationResultBuilderFactory.getDefaultFactory().create();
  }

  /**
   * Creates a new {@link Builder} initialises with a state that matched
   * the one of the given {@code muleMessage}
   *
   * @param muleMessage a reference {@link Message}
   * @param <T>         the generic type of the output value
   * @param <A>         the generic type of the message attributes
   * @return a new {@link Builder}
   */
  static <T, A extends Attributes> Builder<T, A> builder(Message muleMessage) {
    return (Builder<T, A>) OperationResultBuilderFactory.getDefaultFactory().create()
        .output(muleMessage.getPayload().getValue())
        .attributes(muleMessage.getAttributes())
        .mediaType(muleMessage.getPayload().getDataType().getMediaType());
  }

  /**
   * @return The operation's output
   */
  T getOutput();

  /**
   * The new value that the operation wants to set on {@link Message#getAttributes()}.
   * <p>
   * The operation might not be interested in changing that value, in which case
   * this method would return {@link Optional#empty()}
   *
   * @return an {@link Optional} {@code Attributes} value
   */
  Optional<A> getAttributes();

  /**
   * The new {@link MediaType} that the operation wants to set on {@link Message#getDataType()}.
   * <p>
   * The operation might not be interested in changing that value, in which case
   * this method would return {@link Optional#empty()}
   *
   * @return an {@link Optional} {@link MediaType} value
   */
  Optional<MediaType> getMediaType();

  /**
   * Builds instances of {@link Result}
   *
   * @param <T> the generic type of the output value
   * @param <A> the generic type of the message attributes
   */
  interface Builder<T, A extends Attributes> {

    /**
     * Sets the output value
     *
     * @param output the new output value
     * @return {@code this} builder
     */
    Builder<T, A> output(T output);

    /**
     * Sets the output attributes value
     *
     * @param attributes the new attributes value
     * @return {@code this} builder
     */
    Builder<T, A> attributes(A attributes);

    /**
     * Sets the output {@link MediaType}
     *
     * @param mediaType the new {@link MediaType}
     * @return {@code this} builder
     */
    Builder<T, A> mediaType(MediaType mediaType);

    /**
     * @return the build {@link Result}
     */
    Result<T, A> build();
  }

}
