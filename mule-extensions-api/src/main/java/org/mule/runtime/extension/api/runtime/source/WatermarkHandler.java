package org.mule.runtime.extension.api.runtime.source;

import org.mule.runtime.extension.api.runtime.operation.Result;

import java.io.Serializable;
import java.util.Comparator;

public interface WatermarkHandler<T, A, W extends Serializable> {

  W getWatermark(Result<T, A> result);

  Comparator<W> getComparator();

  W getInitialWatermark();
}
