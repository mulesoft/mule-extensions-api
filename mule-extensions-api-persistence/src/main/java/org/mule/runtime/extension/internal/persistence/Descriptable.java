/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.persistence;


/**
 * Contract for all data transfer objects that represent any metadata descriptor.
 *
 * @since 1.0
 */
interface Descriptable<T> {

  T toDescriptor();

}
