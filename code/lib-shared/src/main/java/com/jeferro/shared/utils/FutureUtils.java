package com.jeferro.shared.utils;

import com.jeferro.shared.ddd.domain.exceptions.InternalException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class FutureUtils {

  public static <T1, T2, R> R async(Supplier<T1> fn1, Supplier<T2> fn2, BiFunction<T1, T2, R> combiner) {
    try {
      var fn1Future = CompletableFuture.supplyAsync(fn1);
      var fn2Future = CompletableFuture.supplyAsync(fn2);

      CompletableFuture.allOf(fn1Future, fn2Future).get();

      return combiner.apply(fn1Future.get(), fn2Future.get());
    } catch (ExecutionException executionException) {
      var cause = executionException.getCause();

      if(cause instanceof RuntimeException runtimeException){
        throw runtimeException;
      }

      throw InternalException.createOf(cause);
    }
    catch (Throwable cause) {
      if(cause instanceof RuntimeException runtimeException){
        throw runtimeException;
      }

      throw InternalException.createOf(cause);
    }
  }

  public static <T> T async(Supplier<T> supplier) {
    try {
      return CompletableFuture.supplyAsync(supplier).get();
    } catch (ExecutionException executionException) {
      var cause = executionException.getCause();

      if(cause instanceof RuntimeException runtimeException){
        throw runtimeException;
      }

      throw InternalException.createOf(cause);
    }
    catch (Throwable cause) {
      if(cause instanceof RuntimeException runtimeException){
        throw runtimeException;
      }

      throw InternalException.createOf(cause);
    }
  }
}
