/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.operation;

import static java.util.Optional.ofNullable;

import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.metadata.MediaType;

import java.util.Optional;
import java.util.OptionalLong;

/**
 * Represents the result of a component's execution. Extensions can use this class
 * for cases in which the component not only needs to return a value to be set
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
 * @deprecated use {@link org.mule.sdk.api.runtime.operation.Result} instead.
 */
@Deprecated
public class Result<T, A> {

  /**
   * Builds instances of {@link Result}
   *
   * @param <T> the generic type of the output value
   * @param <A> the generic type of the message attributes
   */
  public static class Builder<T, A> {

    private final Result<T, A> product = new Result<>();

    protected Builder() {}

    /**
     * Sets the output value
     *
     * @param output the new output value
     * @return {@code this} builder
     */
    public Builder<T, A> output(T output) {
      product.output = output;
      return this;
    }

    /**
     * Sets the output attributes value
     *
     * @param attributes the new attributes value
     * @return {@code this} builder
     */
    public Builder<T, A> attributes(A attributes) {
      product.attributes = attributes;
      return this;
    }

    /**
     * Sets the output {@link MediaType} for the payload
     *
     * @param mediaType the new {@link MediaType}
     * @return {@code this} builder
     */
    public Builder<T, A> mediaType(MediaType mediaType) {
      product.mediaType = mediaType;
      return this;
    }

    /**
     * Sets the output {@link MediaType} for the attributes
     *
     * @param mediaType the new {@link MediaType}
     * @return {@code this} builder
     */
    public Builder<T, A> attributesMediaType(MediaType mediaType) {
      product.attributesMediaType = mediaType;
      return this;
    }

    /**
     * Sets the length in bytes of the payload
     *
     * @param length
     * @return
     *
     * @deprecated Use {@link #length(long)} instead.
     */
    @Deprecated
    public Builder<T, A> length(Long length) {
      if (length != null) {
        product.length = OptionalLong.of(length);
      } else {
        product.length = OptionalLong.empty();
      }

      return this;
    }

    /**
     * Sets the length in bytes of the payload
     *
     * @param length
     * @return
     */
    public Builder<T, A> length(long length) {
      product.length = OptionalLong.of(length);
      return this;
    }

    /**
     * Sets the length in bytes of the payload
     *
     * @param length
     * @return
     */
    private Builder<T, A> length(OptionalLong length) {
      product.length = length;
      return this;
    }

    /**
     * @return the build {@link Result}
     */
    public Result<T, A> build() {
      return product;
    }
  }

  /**
   * Creates a new {@link Builder}
   *
   * @param <T> the generic type of the output value
   * @param <A> the generic type of the message attributes
   * @return a new {@link Builder}
   */
  public static <T, A> Builder<T, A> builder() {
    return new Builder<>();
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
  public static <T, A> Builder<T, A> builder(Message muleMessage) {
    return new Builder<T, A>()
        .output((T) muleMessage.getPayload().getValue())
        .attributes((A) muleMessage.getAttributes().getValue())
        .mediaType(muleMessage.getPayload().getDataType().getMediaType())
        .attributesMediaType(muleMessage.getAttributes().getDataType().getMediaType());
  }

  private T output;
  private A attributes = null;
  private MediaType mediaType = null;
  private MediaType attributesMediaType = null;
  private OptionalLong length = OptionalLong.empty();

  protected Result() {}

  /**
   * Creates a new {@link Builder} initialises with a state that matched
   * {@code this} result
   *
   * @return a new {@link Builder}
   */
  public Builder<T, A> copy() {
    final Builder<T, A> builder = new Builder<T, A>().output(output);

    builder.attributes(attributes);
    builder.mediaType(mediaType);
    builder.attributesMediaType(attributesMediaType);
    builder.attributesMediaType(attributesMediaType);
    builder.length(getByteLength());
    return builder;
  }

  /**
   * @return The operation's output
   */
  public T getOutput() {
    return output;
  }

  /**
   * The new value that the operation wants to set on {@link Message#getAttributes()}.
   * <p>
   * The operation might not be interested in changing that value, in which case
   * this method would return {@link Optional#empty()}
   *
   * @return an {@link Optional} {@code Attributes} value
   */
  public Optional<A> getAttributes() {
    return ofNullable(attributes);
  }

  /**
   * The new {@link MediaType} that the operation wants to set on {@link Message} payload.
   * <p>
   * The operation might not be interested in changing that value, in which case
   * this method would return {@link Optional#empty()}
   *
   * @return an {@link Optional} {@link MediaType} value
   */
  public Optional<MediaType> getMediaType() {
    return ofNullable(mediaType);
  }

  /**
   * The length of the payload in bytes if known.
   *
   * @return an {@link Optional} payload length
   *
   * @deprecated Use {@link #getByteLength()} instead.
   */
  @Deprecated
  public Optional<Long> getLength() {
    if (length.isPresent()) {
      return Optional.of(length.getAsLong());
    } else {
      return Optional.empty();
    }
  }

  /**
   * The length of the payload in bytes if known.
   *
   * @return an {@link Optional} payload length
   */
  public OptionalLong getByteLength() {
    return length;
  }

  /**
   * The new {@link MediaType} that the operation wants to set on {@link Message} attributes.
   * <p>
   * The operation might not be interested in changing that value, in which case
   * this method would return {@link Optional#empty()}
   *
   * @return an {@link Optional} {@link MediaType} value
   */
  public Optional<MediaType> getAttributesMediaType() {
    return ofNullable(attributesMediaType);
  }
}
