package org.mule.extensions.introspection;

import java.util.concurrent.Future;

/**
 * A facade interface which hides the details of how an
 * operation is actually implemented. It aims to decouple
 * the abstract meta model that the extension's API proposes from
 * the implementation details of the underlying environment.
 *
 * @since 1.0
 */
public interface OperationImplementation
{

    /**
     * Executes the owning operation using the given {@code operationContext}.
     * It returns a future to allow implementations on top of non-blocking execution engines.
     * This doesn't mean that it has to be executed in a non-blocking manner. Synchronous environments
     * can always return an immediate future.
     *
     * @param operationContext a {@link OperationContext} with information about the execution
     * @return a {@link Future}
     */
    Future<Object> execute(OperationContext operationContext) throws Exception;
}
