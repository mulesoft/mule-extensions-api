/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.api.runtime.source;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.annotation.execution.OnError;
import org.mule.runtime.extension.api.annotation.execution.OnSuccess;
import org.mule.runtime.extension.api.annotation.source.EmitsResponse;
import org.mule.sdk.api.annotation.MinMuleVersion;

/**
 * Base class to write message sources compliant with a given {@link SourceModel}.
 * <p>
 * This class acts as an adapter between the extensions API representation of a message source and Mule's actual MessageSource
 * concept.
 * <p>
 * This class relies on generics to specify the payload and attribute types that the source is going to generate. Although the
 * java compiler does allow for raw uses of the class, this API forbids that since those generics are needed for metadata
 * purposes. For the case of Sources which don't generate a response, the {@code Payload} is to be assigned to {@link Void}.
 * <p>
 * If the source emits responses back to a client, then it must be annotated with {@link EmitsResponse}. Notice that although such
 * annotation is required, the mere fact of using that annotation doesn't make the source automatically send the response, that
 * logic must be coded on each source.
 * <p>
 * Implementations can contain methods annotated with {@link OnSuccess} and/or {@link OnError} to listen for the results of
 * processing the generated messages. As specified, on the javadoc of those annotations, the annotated methods support parameter
 * resolution just like the operations do. If the source is also annotated with {@link EmitsResponse}, these methods will be very
 * useful to actually sending those responses. Notice however, that you can still have these methods and perform actions other
 * than emitting responses if needed.
 *
 * @param <T> the generic type for the generated message's payload
 * @param <A> the generic type for the generated message's attributes
 * @since 1.0
 */
@MinMuleVersion("4.1")
public abstract class Source<T, A> {

  /**
   * This method will be invoked by the runtime to make the source start producing messages.
   * <p>
   * Each generated message will be passed back to the runtime through the given {@code sourceCallback} for processing.
   * <p>
   * This method should throw an exception if the source fails to start, but any other exception encountered during the process of
   * generating messages, should be communicated to the runtime through the
   * {@link SourceCallback#onConnectionException(ConnectionException)}} method.
   * <p>
   * Only the runtime should invoke this method. Do not do it manually
   *
   * @param sourceCallback a {@link SourceCallback}
   * @throws MuleException If the source fails to start.
   */
  public abstract void onStart(SourceCallback<T, A> sourceCallback) throws MuleException;

  /**
   * This method will be invoked by the runtime to make the source stop producing messages.
   * <p>
   * This method should not fail. Any exceptions found during the stop process should be logged and correctly handled by the
   * source, but after invoking this method the source must:
   * <ul>
   * <li>Stop producing messages</li>
   * <li>Free any allocated resources</li>
   * <li>Be capable of being restarted</li>
   * </ul>
   * <p>
   * Only the runtime should invoke this method. Do not do it manually
   */
  public abstract void onStop();

}
