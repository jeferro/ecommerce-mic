package com.jeferro.products.shared.domain.utils;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class FutureUtils {

  public static <T1, T2, R> R executeInParallel(
      Supplier<T1> fn1, Supplier<T2> fn2, BiFunction<T1, T2, R> combiner) {
    var fn1Future = CompletableFuture.supplyAsync(fn1);
    var fn2Future = CompletableFuture.supplyAsync(fn2);

    CompletableFuture.allOf(fn1Future, fn2Future).join();

    return combiner.apply(fn1Future.join(), fn2Future.join());
  }
}
