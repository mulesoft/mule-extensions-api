package org.mule.runtime.extension.api.client.source;

import org.mule.runtime.extension.api.runtime.operation.Result;

import java.util.concurrent.CompletableFuture;

public interface SourceResultCallback<T, A> {

  Result<T, A> getResult();

  CompletableFuture<Void> completeWithSuccess();

  CompletableFuture<Void> completeWithError();
}
