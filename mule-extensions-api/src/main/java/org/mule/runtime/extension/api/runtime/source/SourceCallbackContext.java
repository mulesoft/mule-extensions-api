/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.api.annotation.NoImplement;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.notification.ExtensionNotification;
import org.mule.runtime.api.tx.TransactionException;
import org.mule.runtime.extension.api.connectivity.TransactionalConnection;
import org.mule.runtime.extension.api.notification.NotificationActionDefinition;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.tx.TransactionHandle;
import org.mule.sdk.api.annotation.MinMuleVersion;

import java.util.Optional;

/**
 * A context interface to correlate a message generated by a {@link Source} to a particular state
 *
 * @since 1.0
 */
@MinMuleVersion("4.1")
@NoImplement
public interface SourceCallbackContext {

  /**
   * Associates the given {@code connection} to {@code this} context. As a result of this binding, the runtime will automatically
   * take care of releasing the {@code connection} once the source has finished processing the response and will also take care of
   * resolving the associated transaction (if any).
   * <p>
   * If the connection is a {@link TransactionalConnection} and the source was configured to be transactional, then this method
   * will start such transaction.
   *
   * @param connection the connection to be bound
   * @return a {@link TransactionHandle} produced as a result of the binding
   * @throws ConnectionException  if the connection is not valid or cannot be used
   * @throws TransactionException if a transaction was needed but couldn't be started.
   */
  TransactionHandle bindConnection(Object connection) throws ConnectionException, TransactionException;

  /**
   * Returns the connection that was bound through {@link #bindConnection(Object)}.
   * <p>
   * If that method was not called, then it will throw {@link IllegalStateException}
   *
   * @param <T> the generic type of the connection
   * @return the bound connection
   * @throws IllegalStateException if no connection bound
   */
  <T> T getConnection() throws IllegalStateException;

  /**
   * A handle to the current on-going transaction. If no transaction is active, then you get a handle which represents a void
   * transaction, but this method will never return null.
   * <p>
   * This method can be used without the need for {@link #bindConnection(Object)} to had been invoked on {@code this} instance
   * first. However if it has, then the result of this method will be the exact same instance that {@link #bindConnection(Object)}
   * returned.
   *
   * @return the current {@link TransactionHandle}
   */
  TransactionHandle getTransactionHandle();

  /**
   * Returns whether a variable of name {@code variableName} has a value associated to it through the
   * {@link #addVariable(String, Object)} method
   *
   * @param variableName the name of a variable associated to a particular {@link SourceCallback}
   * @return {@code true} if the parameter is present.
   */
  boolean hasVariable(String variableName);

  /**
   * Returns the value associated to a variable of name {@code variableName}
   *
   * @param variableName the name of a variable of a {@link SourceCallback}
   * @param <T>          the returned value's generic type
   * @return an {@link Optional} of the variable's value.
   */
  <T> Optional<T> getVariable(String variableName);

  /**
   * Adds a variable
   *
   * @param variableName the name of the variable
   * @param value        the variable's value
   */
  void addVariable(String variableName, Object value);

  /**
   * Sets the correlationId of the event that will be passed to the flow when the
   * {@link SourceCallback#handle(Result, SourceCallbackContext)} method is invoked.
   * <p>
   * This method can only be invoked <b>BEFORE</b> {@code this} instance has been used on a
   * {@link SourceCallback#handle(Result, SourceCallbackContext)} operation. Otherwise, an {@link IllegalStateException} will be
   * thrown.
   *
   * @param correlationId a correlationId
   * @throws IllegalArgumentException if invoked once {@code this} context has already been used to push a message
   * @since 1.1
   */
  void setCorrelationId(String correlationId);

  /**
   * @return Optionally returns the correlationId set through {@link #setCorrelationId(String)} (if any).
   * @since 1.1
   */
  Optional<String> getCorrelationId();

  /**
   * @param <T> the generic type of the output values of the generated results
   * @param <A> the generic type of the attributes of the generated results
   * @return The {@link SourceCallback} that generated {@code this} context
   */
  <T, A> SourceCallback<T, A> getSourceCallback();

  /**
   * Indicates that an {@link ExtensionNotification} should be fired with the desired information when the runtime takes the
   * source result to process it.
   *
   * @param action the {@link NotificationActionDefinition} to use.
   * @param data   the {@link TypedValue} data to use.
   * @since 4.1
   */
  void fireOnHandle(NotificationActionDefinition<?> action, TypedValue<?> data);
}
